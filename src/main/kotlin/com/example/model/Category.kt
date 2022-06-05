package com.example.model

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.uuid
import org.ktorm.schema.varchar
import java.util.UUID

interface Category: Entity<Category> {
    companion object: Entity.Factory<Category>()

    var id: UUID
    var name: String
}

object Categories: Table<Category>("categories") {
    val id = uuid("category_id").primaryKey().bindTo { it.id }
    val name = varchar("category_name").bindTo { it.name }
}