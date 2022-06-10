package com.example.plugins.routing

import com.example.api.UserAPI
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.userRouting() {
    routing {
        route("/users") {
            get {
                UserAPI.get(call)
            }

            post {
                UserAPI.addUser(call)
            }

            put {
                UserAPI.updateUser(call)
            }
        }
    }
}