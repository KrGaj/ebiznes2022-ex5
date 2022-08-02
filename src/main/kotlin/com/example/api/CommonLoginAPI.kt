package com.example.api

import com.example.model.session.UserSession
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*

object CommonLoginAPI {
    suspend fun getLoginStatus(call: ApplicationCall) {
        val session = call.sessions.get<UserSession>()
        println("Session: $session")
        var userSession = UserSession(false, "")

        if (session != null) {
            userSession = UserSession(true, session.accessToken)
        }

        println("Login status get: $userSession")

        call.respond(HttpStatusCode.OK, userSession)
    }
}