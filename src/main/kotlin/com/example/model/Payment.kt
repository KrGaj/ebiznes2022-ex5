package com.example.model

import org.ktorm.entity.Entity
import org.ktorm.schema.*
import java.util.UUID

interface Payment: Entity<Payment> {
    companion object: Entity.Factory<Payment>()

    val id: UUID
    val method: String
    val date: Long
    val paid: Boolean
}

object Payments: Table<Payment>("payments") {
    val id = uuid("id").primaryKey().bindTo { it.id }
    val method = varchar("method").bindTo { it.method }
    val date = long("paymentDate").bindTo { it.date }
    val paid = boolean("paid").bindTo { it.paid }
}
