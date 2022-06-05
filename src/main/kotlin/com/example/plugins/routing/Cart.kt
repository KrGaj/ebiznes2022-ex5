package com.example.plugins.routing

import com.example.api.CartAPI
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.cartRouting() {
    routing {
        route("/cart") {
            get {
                CartAPI.get(call)
            }

            post {
                CartAPI.addProduct(call)
            }

            delete {
                CartAPI.removeProduct(call)
            }

            put {
                CartAPI.updateProduct(call)
            }
        }
    }
}