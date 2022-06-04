package com.example.model

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.uuid
import java.util.*

interface CartProduct: Entity<CartProduct> {
    companion object: Entity.Factory<BoughtProduct>()

    val id: UUID
    val amount: Int
    val product: Product
}

object CartProducts: Table<CartProduct>("cart_products") {
    val id = uuid("id").primaryKey().bindTo { it.id }
    val amount = int("amount").bindTo { it.amount }
    val productId = uuid("productId").references(Products) { it.product }
}
