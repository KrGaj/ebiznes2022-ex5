package com.example.plugins

import com.example.plugins.oauth.configureOauthGoogle
import io.ktor.server.application.*

fun Application.configureOauth() {
    configureOauthGoogle()
}