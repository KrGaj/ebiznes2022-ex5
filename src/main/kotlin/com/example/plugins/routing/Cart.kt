package com.example.plugins.routing

import com.example.api.CartAPI
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Application.cartRouting() {
    routing {
        authenticate("auth-jwt") {
            route("/cart") {
                route("add_multiple") {
                    post {
                        CartAPI.addMultipleProducts(call)
                    }
                }

                get {
                    CartAPI.getByUserId(call)
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
}