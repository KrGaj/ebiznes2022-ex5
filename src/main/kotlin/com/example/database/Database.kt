package com.example.database

object Database {
    val instance = org.ktorm.database.Database.connect(
        url = Config.url,
        driver = Config.driver,
        user = Config.username,
        password = Config.password
    )
}