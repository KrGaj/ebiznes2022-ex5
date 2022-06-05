package com.example.plugins

import com.example.plugins.routing.cartRouting
import com.example.plugins.routing.categoryRouting
import com.example.plugins.routing.productRouting
import com.example.plugins.routing.userRouting
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*

fun Application.configureRouting() {
    productRouting()
    categoryRouting()
    cartRouting()
    userRouting()

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
    }
}
