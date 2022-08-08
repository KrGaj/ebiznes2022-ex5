package com.example.model.session

import io.ktor.server.auth.*

data class JwtUserInfo(
    val userId: String,
    val username: String,
    val email: String
    ) : Principal
