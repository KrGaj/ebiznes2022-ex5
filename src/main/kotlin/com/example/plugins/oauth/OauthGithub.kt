package com.example.plugins.oauth

import io.ktor.server.application.*
import io.ktor.server.auth.*

fun Application.configureOauthGithub() {
    install(Authentication) {
        oauth() {

        }
    }
}