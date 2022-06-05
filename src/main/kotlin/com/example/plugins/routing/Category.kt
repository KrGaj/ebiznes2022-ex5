package com.example.plugins.routing

import com.example.api.CategoryAPI
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.categoryRouting() {
    routing {
        route("/categories") {
            get {
                CategoryAPI.get(call)
            }

            post {
                CategoryAPI.addCategory(call)
            }

            delete {
                CategoryAPI.removeCategory(call)
            }
        }

        get("/categories/all") {
            CategoryAPI.getAll(call)
        }
    }
}