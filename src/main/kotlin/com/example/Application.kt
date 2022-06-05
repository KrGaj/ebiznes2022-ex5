package com.example

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
//        install(CORS) {
//            allowHost("localhost:3000")
//            allowHeader(HttpHeaders.ContentType)
//        }

        configureRouting()
        configureSerialization()
    }.start(wait = true)
}
