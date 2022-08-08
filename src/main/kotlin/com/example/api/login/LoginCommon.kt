package com.example.api.login

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.config.Config
import com.example.database.Database
import com.example.model.User
import com.example.model.Users
import com.example.model.session.JwtUserInfo
import com.example.model.session.UserSession
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import org.ktorm.dsl.*
import org.ktorm.support.postgresql.insertReturning

object LoginCommon {
    private val database = Database.instance

    suspend fun respond(call: ApplicationCall, accessToken: String, email: String) {
        lateinit var user: User

        val query = database.from(Users)
            .joinReferencesAndSelect()
            .where {
                Users.email eq email
            }

        val users = query.map { row ->
            Users.createEntity(row)
        }

        user = if (users.isEmpty()) {
            val userId = database.insertReturning(Users, Users.id) {
                set(it.username, email)
                set(it.email, email)
                set(it.accessToken, accessToken)
            }

            val userQuery = database.from(Users)
                .select()
                .where {
                    Users.id eq userId!!
                }

            userQuery.map {row ->
                Users.createEntity(row)
            }.first()
        } else {
            println(users.first())
            println(users.first().id)
            users.first()
        }

        val token = generateToken(JwtUserInfo(user.id.toString(), user.username, user.email))

        call.sessions.set(UserSession(loggedIn = true, token))
        call.response.cookies.append(
            Cookie(
                name = "user_info",
                path = "/",
                value = UserSession(loggedIn = true, token).toString()
            )
        )
        call.respondRedirect("http://localhost:3000/")
    }

    private fun generateToken(user: JwtUserInfo): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(Config.jwt_issuer)
        .withClaim("userId", user.userId)
        .withClaim("username", user.username)
        .withClaim("email", user.email)
        .sign(Algorithm.HMAC256(Config.jwt_secret))
}