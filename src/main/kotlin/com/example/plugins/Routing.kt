package com.example.plugins

import com.example.plugins.routing.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*

fun Application.configureRouting() {
    loginRouting()
    productRouting()
    categoryRouting()
    cartRouting()
    userRouting()
    purchaseRouting()
    paymentRouting()

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
    }
}
