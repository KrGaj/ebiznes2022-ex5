package com.example.model.session

data class OauthUserEmailsGithub(
    val email: String = "",
    val primary: Boolean = false,
    val verified: Boolean = false,
    val visibility: String = ""
)
