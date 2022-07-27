package com.example.api.login

import com.example.model.session.OauthUserInfoGoogle
import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*

object GoogleLoginAPI {
    suspend fun callback(call: ApplicationCall) {
        val principal = call.authentication.principal<OAuthAccessTokenResponse.OAuth2>()

        if (principal != null) {
            val accessToken = principal.accessToken

            val info: String = HttpClient().get("https://www.googleapis.com/oauth2/v2/userinfo") {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $accessToken")
                    append(HttpHeaders.Accept, "application/json")
                }
            }.body()

            val objectMapper = ObjectMapper()
            val userInfo = objectMapper.readValue(info, OauthUserInfoGoogle::class.java)

            LoginCommon.respond(call, accessToken, userInfo.email)
        }
    }
}