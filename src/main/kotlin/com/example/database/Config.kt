package com.example.database

object Config {
    val url: String = System.getenv("database_url")
    val username: String = System.getenv("database_username")
    val password: String = System.getenv("database_password")
    const val driver = "org.postgresql.Driver"
}