package com.example.database

import com.example.config.Config

object Database {
    val instance = org.ktorm.database.Database.connect(
        url = Config.url,
        driver = Config.driver,
        user = Config.username,
        password = Config.password
    )
}