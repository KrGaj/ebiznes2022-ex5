package com.example.api

import com.example.database.Database
import com.example.model.Users
import com.example.model.session.OauthUserInfo
import com.example.model.session.UserSession
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

object GoogleLoginAPI {
    private val database = Database.instance

    suspend fun callback(call: ApplicationCall) {
        val principal = call.authentication.principal<OAuthAccessTokenResponse.OAuth2>()

        if (principal != null) {
            val accessToken = principal.accessToken
            val userId: UUID?

            val info: String = HttpClient().get("https://www.googleapis.com/oauth2/v2/userinfo") {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $accessToken")
                    append(HttpHeaders.Accept, "application/json")
                }
            }.body()

            val objectMapper = ObjectMapper()
            val userInfo = objectMapper.readValue(info, OauthUserInfo::class.java)

            val query = database.from(Users)
                .joinReferencesAndSelect()
                .where {
                    Users.email eq userInfo.email
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
                    value = UserSession(loggedIn = true, principal.accessToken, userId!!).toString()
                )
            )
            call.respondRedirect("http://localhost:3000/")
        }
    }
}