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
    val id = uuid("user_id").primaryKey().bindTo { it.id }
    val username = varchar("username").bindTo { it.username }
    val passwordAsHash = varchar("password").bindTo { it.passwordAsHash }
    val email = varchar("email").bindTo { it.email }
    val accessToken = varchar("access_token").bindTo { it.accessToken }
}

