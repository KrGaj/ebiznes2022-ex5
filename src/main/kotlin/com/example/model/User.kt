package com.example.model

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.uuid
import org.ktorm.schema.varchar
import java.util.UUID

interface User: Entity<User> {
    companion object : Entity.Factory<User>()

    val id: UUID
    val username: String
    val passwordAsHash: String
    val email: String
    val accessToken: String
}


object Users: Table<User>("users") {
    val id = uuid("id").primaryKey()
    val username = varchar("username")
    val passwordAsHash = varchar("password_hash")
    val email = varchar("email")
    val accessToken = varchar("access_token")
}

