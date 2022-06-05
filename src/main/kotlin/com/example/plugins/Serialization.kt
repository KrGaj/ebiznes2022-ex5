package com.example.plugins

import io.ktor.serialization.jackson.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.jackson.KtormModule

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        jackson {
            registerModule(KtormModule())
        }
    }

    routing {
        get("/json/gson") {
            call.respond(mapOf("hello" to "world"))
        }
    }
}
