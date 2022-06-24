package com.example.database

object Config {
    val url: String = System.getenv("DATABASE_URL")
    val username: String = System.getenv("DATABASE_USERNAME")
    val password: String = System.getenv("DATABASE_PASSWORD")
    const val driver = "org.postgresql.Driver"
}