package com.example.model

import org.ktorm.entity.Entity
import org.ktorm.schema.*
import java.util.UUID

interface Product: Entity<Product> {
    companion object: Entity.Factory<Product>()

    val id: UUID
    val name: String
    val description: String
    val price: Float
    val category: Category
    val available: Int
}

object Products: Table<Product>("products") {
    val id = uuid("product_id").primaryKey().bindTo { it.id }
    val name = varchar("product_name").bindTo { it.name }
    val description = varchar("description").bindTo { it.description }
    val price = float("price").bindTo { it.price }
    val category = uuid("category_id").references(Categories) { it.category }
    val available = int("available").bindTo { it.available }
}