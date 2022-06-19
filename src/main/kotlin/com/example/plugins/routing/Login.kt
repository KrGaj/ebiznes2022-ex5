package com.example.plugins.routing

import com.example.api.GoogleLoginAPI
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Application.loginRouting() {
    routing {
        authenticate("auth-oauth-google") {
            get("/login/google") {
                // Redirects to 'authorizeUrl' automatically
            }

            get("/callback/google") {
                GoogleLoginAPI.callback(call)
            }
        }
    }
}