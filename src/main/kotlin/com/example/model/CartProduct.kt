package com.example.model

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.uuid
import java.util.*

interface CartProduct: Entity<CartProduct> {
    companion object: Entity.Factory<PurchaseProduct>()

    var id: UUID
    var cart: Cart
    var amount: Int
    var product: Product
}

object CartProducts: Table<CartProduct>("cart_products") {
    val id = uuid("cart_product_id").primaryKey().bindTo { it.id }
    val product = uuid("product_id").references(Products) { it.product }
    val cart = uuid("cart_id").references(Carts) { it.cart }
    val amount = int("amount").bindTo { it.amount }
}
