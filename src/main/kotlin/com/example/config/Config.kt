package com.example.config

object Config {
    val url: String = System.getenv("DATABASE_URL")
    val username: String = System.getenv("DATABASE_USERNAME")
    val password: String = System.getenv("DATABASE_PASSWORD")
    val githubClientId: String = System.getenv("GITHUB_CLIENT_ID")
    val githubClientSecret: String = System.getenv("GITHUB_CLIENT_SECRET")
    val gitlabClientId: String = System.getenv("GITLAB_CLIENT_ID")
    val gitlabClientSecret: String = System.getenv("GITLAB_CLIENT_SECRET")
    val googleClientId: String = System.getenv("GOOGLE_CLIENT_ID")
    val googleClientSecret: String = System.getenv("GOOGLE_CLIENT_SECRET")
    val facebookClientId: String = System.getenv("FACEBOOK_CLIENT_ID")
    val facebookClientSecret: String = System.getenv("FACEBOOK_CLIENT_SECRET")
    val jwt_secret: String = System.getenv("JWT_SECRET")
    val jwt_issuer: String = System.getenv("JWT_ISSUER")
    val jwt_realm: String = System.getenv("JWT_REALM")
    const val driver = "org.postgresql.Driver"
}