package com.example.plugins.routing

import com.example.api.PaymentAPI
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.paymentRouting() {
    routing {
        route("/payments") {
            get {
                PaymentAPI.getById(call)
            }

            post {
                PaymentAPI.addPayment(call)
            }
        }
    }
}