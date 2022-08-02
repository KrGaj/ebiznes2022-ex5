package com.example

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*
import com.example.plugins.configureAuth

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureSessions()
        configureCors()
        configureAuth()
        configureRouting()
        configureSerialization()
    }.start(wait = true)
}
