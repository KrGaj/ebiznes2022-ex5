package com.example.api.login

import com.example.model.session.OauthUserEmailsGithub
import com.example.model.session.OauthUserInfoGithub
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*

object GithubLoginAPI {
    suspend fun callback(call: ApplicationCall) {
        val principal = call.authentication.principal<OAuthAccessTokenResponse.OAuth2>()

        if (principal != null) {
            val accessToken = principal.accessToken

            val info: String = HttpClient().get("https://api.github.com/user") {
                headers {
                    append(HttpHeaders.Authorization, "token $accessToken")
                    append(HttpHeaders.Accept, "application/json")
                }
            }.body()

            val objectMapper = ObjectMapper().apply {
                configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            }

            var userInfo = objectMapper.readValue(info, OauthUserInfoGithub::class.java)

            if (userInfo.email == null) {
                val emails: String = HttpClient().get("https://api.github.com/user/emails") {
                    headers {
                        append(HttpHeaders.Authorization, "token $accessToken")
                        append(HttpHeaders.Accept, "application/json")
                    }
                }.body()

                val userEmails: List<OauthUserEmailsGithub> = objectMapper.readValue(
                    emails, objectMapper.typeFactory.constructCollectionType(
                        List::class.java, OauthUserEmailsGithub::class.java))

                userInfo = userInfo.copy(email = userEmails.first().email)
            }

            LoginCommon.respond(call, accessToken, userInfo.email!!)
        }
    }
}