package com.example.plugins.routing

import com.example.api.CommonLoginAPI
import com.example.api.login.FacebookLoginAPI
import com.example.api.login.GithubLoginAPI
import com.example.api.login.GitlabLoginAPI
import com.example.api.login.GoogleLoginAPI
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

        authenticate("auth-oauth-github") {
            get("/login/github") {
                // Redirects to 'authorizeUrl' automatically
            }

            get("/callback/github") {
                GithubLoginAPI.callback(call)
            }
        }

        authenticate("auth-oauth-gitlab") {
            get("/login/gitlab") {
                // Redirects to 'authorizeUrl' automatically
            }

            get("/callback/gitlab") {
                GitlabLoginAPI.callback(call)
            }
        }

        authenticate("auth-oauth-facebook") {
            get("/login/facebook") {
                // Redirects to 'authorizeUrl' automatically
            }

            get("/callback/facebook") {
                FacebookLoginAPI.callback(call)
            }
        }

        get("/login/status") {
            CommonLoginAPI.getLoginStatus(call)
        }
    }
}