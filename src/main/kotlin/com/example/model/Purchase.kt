package com.example.model

import com.example.utils.json
import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.uuid
import java.util.UUID

interface Purchase: Entity<Purchase> {
    companion object: Entity.Factory<Purchase>()

    val id: UUID
    val user: User
    val payment: Payment
}

object Purchases: Table<Purchase>("purchases") {
    val id = uuid("id").primaryKey().bindTo { it.id }
    val userId = uuid("userId").references(Users) { it.user }
    val payment = uuid("payment").references(Payments) { it.payment }
}
