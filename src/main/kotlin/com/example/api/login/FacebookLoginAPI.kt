package com.example.api.login

import com.example.model.session.OauthUserInfoFacebook
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.server.application.*
import io.ktor.server.auth.*

object FacebookLoginAPI {
    suspend fun callback(call: ApplicationCall) {
        val principal = call.authentication.principal<OAuthAccessTokenResponse.OAuth2>()

        if (principal != null) {
            val accessToken = principal.accessToken

            val info: String = HttpClient()
                .get("https://graph.facebook.com/me?fields=id,name,email&access_token=$accessToken")
                .body()

            println("Facebook info: $info")

            val objectMapper = ObjectMapper().apply {
                configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            }

            val userInfo = objectMapper.readValue(info, OauthUserInfoFacebook::class.java)

            LoginCommon.respond(call, accessToken, userInfo.email)
        }
    }
}