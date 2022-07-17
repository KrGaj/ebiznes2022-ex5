package com.example.api

import com.example.database.Database
import com.example.model.Carts
import com.example.model.User
import com.example.model.Users
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.ktorm.dsl.*
import org.ktorm.support.postgresql.insertReturning
import java.util.*

object UserAPI {
    private val database = Database.instance
    suspend fun get(call: ApplicationCall) {
        val userId = UUID.fromString(call.parameters["user_id"])
        val query = database
            .from(Users)
            .select()
            .where {
                Users.id eq userId
            }

        val users = query.map { row ->
            Users.createEntity(row)
        }

        when (users.isEmpty()) {
            true -> call.respond(HttpStatusCode.NotFound)
            false -> call.respond(HttpStatusCode.OK, users.first())
        }
    }

    suspend fun addUser(call: ApplicationCall) {
        val user = call.receive<User>()

        val id = database.insertReturning(Users, Users.id) {
            set(it.username, user.username)
            set(it.email, user.email)
            set(it.passwordAsHash, user.passwordAsHash)
            set(it.accessToken, user.accessToken)
        }

        val cart = database.insertReturning(Carts, Carts.id) {
            set(it.userId, id)
        }

        if (id == null || cart == null) {
            call.respond(HttpStatusCode.InternalServerError)
        }
        else {
            call.respond(HttpStatusCode.OK, mapOf(
                "userId" to id,
                "cartId" to cart
            ))
        }
    }

    suspend fun updateUser(call: ApplicationCall) {
        val user = call.receive<User>()

        val updated = database
            .update(Users) {
                set(it.email, user.email)
                set(it.passwordAsHash, user.passwordAsHash)
                set(it.accessToken, user.accessToken)

                where {
                    Users.id eq user.id
                }
            }

        call.respond(HttpStatusCode.OK, updated)
    }
}