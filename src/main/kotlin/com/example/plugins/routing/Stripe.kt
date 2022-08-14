package com.example.plugins.routing

import com.example.api.StripeAPI
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Application.stripeRouting() {
    routing {
        authenticate("auth-jwt") {
            route("/secret/") {
                get {
                    StripeAPI.getSecret(call)
                }
            }
        }
    }
}