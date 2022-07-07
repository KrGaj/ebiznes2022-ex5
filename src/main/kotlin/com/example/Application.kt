package com.example

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*
import com.example.plugins.configureOauth

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureSessions()
        configureCors()
        configureOauth()
        configureRouting()
        configureSerialization()
    }.start(wait = true)
}
