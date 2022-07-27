package com.example.api.login

import com.example.database.Database
import com.example.model.Users
import com.example.model.session.UserSession
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import org.ktorm.dsl.*
import org.ktorm.support.postgresql.insertReturning
import java.util.*

object LoginCommon {
    private val database = Database.instance

    suspend fun respond(call: ApplicationCall, accessToken: String, email: String) {
        val userId: UUID?

        val query = database.from(Users)
            .joinReferencesAndSelect()
            .where {
                Users.email eq email
            }

        val users = query.map { row ->
            Users.createEntity(row)
        }

        userId = if (users.isEmpty()) {
            database.insertReturning(Users, Users.id) {
                set(it.username, email)
                set(it.email, email)
                set(it.accessToken, accessToken)
            }
        } else {
            println(users.first())
            println(users.first().id)
            users.first().id
        }

        call.sessions.set(UserSession(loggedIn = true, accessToken, userId!!))
        call.response.cookies.append(
            Cookie(
                name = "user_info",
                path = "/",
                value = UserSession(loggedIn = true, accessToken, userId).toString()
            )
        )
        call.respondRedirect("http://localhost:3000/")
    }
}