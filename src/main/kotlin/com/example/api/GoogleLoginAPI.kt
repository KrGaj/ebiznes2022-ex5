package com.example.api

import com.example.database.Database
import com.example.model.User
import com.example.model.Users
import com.example.model.session.UserSession
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

            val query = database.from(Users)
                .select()
                .where {
                    Users.accessToken eq accessToken
                }

            val users = query.map { row ->
                Users.createEntity(row)
            }

            if (users.isEmpty())
            {
                val info = HttpClient().get("https://www.googleapis.com/oauth2/v2/userinfo") {
                    headers {
                        append(HttpHeaders.Authorization, "Bearer $accessToken")
                        append(HttpHeaders.Accept, "application/json")
                    }
                }

                val infoStr: String = info.body()

                print(infoStr)

                val userInfo: User = info.body()

                userId = database.insertReturning(Users, Users.id) {
                    set(it.username, userInfo.email)
                    set(it.email, userInfo.email)
                    set(it.accessToken, accessToken)
                }
            }
            else {
                userId = users.first().id
            }

            call.sessions.set(UserSession(loggedIn = true, principal.accessToken, userId!!))
            call.respondRedirect("/")
        }
    }

    suspend fun getLoginStatus(call: ApplicationCall) {
        val session = call.sessions.get<UserSession>()
        var userSession = UserSession(false, "", null)

        if (session != null) {
            userSession = UserSession(true, session.accessToken, session.userId)
        }

        call.respond(HttpStatusCode.OK, userSession)
    }
}