package com.example.model.session

import java.util.UUID

data class UserSession(val loggedIn: Boolean, val accessToken: String, val userId: UUID?)
