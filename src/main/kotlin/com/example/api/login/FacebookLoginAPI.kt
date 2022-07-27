package com.example.api.login

import io.ktor.server.application.*
import io.ktor.server.auth.*

object FacebookLoginAPI {
    suspend fun callback(call: ApplicationCall) {
        val principal = call.authentication.principal<OAuthAccessTokenResponse.OAuth2>()
    }
}