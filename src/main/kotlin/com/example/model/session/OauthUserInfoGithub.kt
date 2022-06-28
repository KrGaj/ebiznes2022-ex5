package com.example.model.session

data class OauthUserInfoGithub(
    val id: String = "",
    val login: String = "",
    val node_id: String = "",
    val url: String = "",
    val avatar_url: String = "",
    val email: String? = null
)
