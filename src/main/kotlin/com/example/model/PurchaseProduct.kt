package com.example.model

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.uuid
import java.util.UUID

interface PurchaseProduct: Entity<PurchaseProduct> {
    companion object: Entity.Factory<PurchaseProduct>()

    var id: UUID
    var purchase: Purchase
    var amount: Int
    var product: Product
}

object PurchaseProducts: Table<PurchaseProduct>("purchase_products") {
    val id = uuid("purchase_product_id").primaryKey().bindTo { it.id }
    val purchase = uuid("purchase_id").references(Purchases) { it.purchase }
    val productId = uuid("product_id").references(Products) { it.product }
    val amount = int("amount").bindTo { it.amount }
}
