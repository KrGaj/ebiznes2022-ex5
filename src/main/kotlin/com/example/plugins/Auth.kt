package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.config.Config
import com.example.model.session.JwtUserInfo
import io.ktor.client.*
import io.ktor.client.engine.java.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureAuth() {
    install(Authentication) {
        jwt("auth-jwt") {
            realm = Config.jwt_realm

            verifier(JWT
                .require(Algorithm.HMAC256(Config.jwt_secret))
                .withIssuer(Config.jwt_issuer)
                .build()
            )

            validate {
                val userId = it.payload.getClaim("userId").asString()
                val username = it.payload.getClaim("username").asString()
                val email = it.payload.getClaim("email").asString()

                if (userId != null && username != null && email != null) {
                    JwtUserInfo(userId, username, email)
                }
                else {
                    null
                }
            }
        }

        oauth("auth-oauth-google") {
            urlProvider = { "http://localhost:8080/callback/google" }

            providerLookup = {
                OAuthServerSettings.OAuth2ServerSettings(
                    name = "google",
                    authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
                    accessTokenUrl = "https://accounts.google.com/o/oauth2/token",
                    requestMethod = HttpMethod.Post,
                    clientId = Config.googleClientId,
                    clientSecret = Config.googleClientSecret,
                    defaultScopes = listOf(
                        "https://www.googleapis.com/auth/userinfo.profile",
                        "https://www.googleapis.com/auth/userinfo.email"
                    )
                )
            }

            client = HttpClient(Java)
        }

        oauth("auth-oauth-github") {
            urlProvider = { "http://localhost:8080/callback/github" }

            providerLookup = {
                OAuthServerSettings.OAuth2ServerSettings(
                    name = "github",
                    authorizeUrl = "https://github.com/login/oauth/authorize",
                    accessTokenUrl = "https://github.com/login/oauth/access_token",
                    requestMethod = HttpMethod.Post,
                    clientId = Config.githubClientId,
                    clientSecret = Config.githubClientSecret,
                    defaultScopes = listOf(
                        "read:user",
                        "user:email"
                    )
                )
            }

            client = HttpClient(Java)
        }

        oauth("auth-oauth-gitlab") {
            urlProvider = { "http://localhost:8080/callback/gitlab" }

            providerLookup = {
                OAuthServerSettings.OAuth2ServerSettings(
                    name = "facebook",
                    authorizeUrl = "https://gitlab.com/oauth/authorize",
                    accessTokenUrl = "https://gitlab.com/oauth/token",
                    requestMethod = HttpMethod.Post,
                    clientId = Config.gitlabClientId,
                    clientSecret = Config.gitlabClientSecret,
                    defaultScopes = listOf(
                        "read_user",
                        "profile",
                        "email"
                    )
                )
            }

            client = HttpClient(Java)
        }

//        oauth("auth-oauth-facebook") {
//            urlProvider = { "http://localhost:8080/callback/facebook" }
//
//            providerLookup = {
//                OAuthServerSettings.OAuth2ServerSettings(
//                    name = "facebook",
//                    authorizeUrl = "https://www.facebook.com/v14.0/dialog/oauth",
//                    accessTokenUrl = "https://graph.facebook.com/v14.0/oauth/access_token",
//                    requestMethod = HttpMethod.Post,
//                    clientId = Config.facebookClientId,
//                    clientSecret = Config.facebookClientSecret,
//                    defaultScopes = listOf(
//                        "public_profile",
//                        "email"
//                    )
//                )
//            }
//
//            client = HttpClient(Java)
//        }
    }
}