package com.example.model

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.uuid
import java.util.UUID

interface Cart: Entity<Cart> {
    companion object: Entity.Factory<Cart>()

    var id: UUID
    var user: User
}

object Carts: Table<Cart>("carts") {
    val id = uuid("cart_id").primaryKey().bindTo { it.id }
    val userId = uuid("user_id").references(Users) { it.user }
}
