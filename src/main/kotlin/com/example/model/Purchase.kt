package com.example.model

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.uuid
import java.util.UUID

interface Purchase: Entity<Purchase> {
    companion object: Entity.Factory<Purchase>()

    val id: UUID
    val user: User
    val cashSum: Int
    val payment: Payment
}

object Purchases: Table<Purchase>("purchases") {
    val id = uuid("purchase_id").primaryKey().bindTo { it.id }
    val userId = uuid("user_id").references(Users) { it.user }
    val cashSum = int("cash_sum").bindTo { it.cashSum }
    val payment = uuid("payment_id").references(Payments) { it.payment }
}
