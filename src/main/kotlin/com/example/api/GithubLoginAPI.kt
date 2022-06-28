package com.example.api

import com.example.database.Database
import com.example.model.Users
import com.example.model.session.OauthUserEmailsGithub
import com.example.model.session.OauthUserInfoGithub
import com.example.model.session.UserSession
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import org.ktorm.dsl.*
import org.ktorm.support.postgresql.insertReturning
import java.util.*

object GithubLoginAPI {
    private val database = Database.instance

    suspend fun callback(call: ApplicationCall) {
        val principal = call.authentication.principal<OAuthAccessTokenResponse.OAuth2>()

        if (principal != null) {
            val accessToken = principal.accessToken
            val userId: UUID?

            val info: String = HttpClient().get("https://api.github.com/user") {
                headers {
                    append(HttpHeaders.Authorization, "token $accessToken")
                    append(HttpHeaders.Accept, "application/json")
                }
            }.body()

            val objectMapper = ObjectMapper()
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            var userInfo = objectMapper.readValue(info, OauthUserInfoGithub::class.java)

            if (userInfo.email == null) {
                val emails: String = HttpClient().get("https://api.github.com/user/emails") {
                    headers {
                        append(HttpHeaders.Authorization, "token $accessToken")
                        append(HttpHeaders.Accept, "application/json")
                    }
                }.body()

                val userEmails: List<OauthUserEmailsGithub> = objectMapper.readValue(
                    emails, objectMapper.typeFactory.constructCollectionType(
                        List::class.java, OauthUserEmailsGithub::class.java))

                userInfo = userInfo.copy(email = userEmails.first().email)
            }

            val query = database.from(Users)
                .joinReferencesAndSelect()
                .where {
                    Users.email eq userInfo.email!!
                }

            val users = query.map { row ->
                Users.createEntity(row)
            }

            userId = if (users.isEmpty()) {
                println(info)

                database.insertReturning(Users, Users.id) {
                    set(it.username, userInfo.email)
                    set(it.email, userInfo.email)
                    set(it.accessToken, accessToken)
                }
            } else {
                println(users.first())
                println(users.first().id)
                users.first().id
            }

            call.sessions.set(UserSession(loggedIn = true, principal.accessToken, userId!!))
            call.response.cookies.append(
                Cookie(
                    name = "user_info",
                    path = "/",
                    value = UserSession(loggedIn = true, principal.accessToken, userId).toString()
                )
            )
            call.respondRedirect("http://localhost:3000/")
        }
    }
}