package com.example.plugins

import com.example.database.Config
import io.ktor.client.*
import io.ktor.client.engine.java.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*

fun Application.configureOauth() {
    install(Authentication) {
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