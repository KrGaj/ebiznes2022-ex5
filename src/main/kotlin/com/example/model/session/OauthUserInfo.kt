package com.example.model.session

data class OauthUserInfo(
    val id: String = "",
    val name: String = "",
    val given_name: String = "",
    val email: String = "",
    val verified_email: Boolean = true,
    val picture: String = "",
    val locale: String = ""
)
