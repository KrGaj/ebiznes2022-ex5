package com.example.model

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.float
import org.ktorm.schema.uuid
import org.ktorm.schema.varchar
import java.util.UUID

interface Product: Entity<Product> {
    companion object: Entity.Factory<Product>()

    val id: UUID
    val name: String
    val description: String
    val price: Float
    val categoryId: Category
}

object Products: Table<Product>("products") {
    val id = uuid("id").primaryKey().bindTo { it.id }
    val name = varchar("name").bindTo { it.name }
    val description = varchar("description").bindTo { it.description }
    val price = float("price").bindTo { it.price }
    val categoryId = uuid("category_id").references(Categories) { it.categoryId }
}