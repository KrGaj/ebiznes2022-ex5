package com.example.model

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.uuid
import org.ktorm.schema.varchar
import java.util.UUID

interface Category: Entity<Category> {
    companion object: Entity.Factory<Category>()

    val id: UUID
    val name: String
}

object Categories: Table<Category>("categories") {
    val id = uuid("id").primaryKey().bindTo { it.id }
    val name = varchar("name").bindTo { it.name }
}