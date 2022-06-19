package com.example

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*
import com.example.plugins.configureOauth

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureCors()
        configureRouting()
        configureOauth()
        configureSerialization()
    }.start(wait = true)
}
