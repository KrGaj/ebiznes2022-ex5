package com.example.api.login

import com.example.model.session.OauthUserInfoGitlab
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*

object GitlabLoginAPI {
    suspend fun callback(call: ApplicationCall) {
        val principal = call.authentication.principal<OAuthAccessTokenResponse.OAuth2>()

        if(principal != null) {
            val accessToken = principal.accessToken

            val info: String = HttpClient().get("https://gitlab.com/api/v4/user") {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $accessToken")
                    append(HttpHeaders.Accept, "application/json")
                }
            }.body()

            val objectMapper = ObjectMapper().apply {
                configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            }

            val userInfo = objectMapper.readValue(info, OauthUserInfoGitlab::class.java)

            println("GitLab user info")
            println(userInfo)

            LoginCommon.respond(call, accessToken, userInfo.email)
        }
    }
}