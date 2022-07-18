package com.example.api

import com.example.database.Database
import com.example.model.Categories
import com.example.model.Category
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.ktorm.dsl.*
import org.ktorm.support.postgresql.insertReturning
import java.util.UUID

object CategoryAPI {
    private val database = Database.instance
    suspend fun get(call: ApplicationCall) {
        val categoryId = UUID.fromString(call.parameters["category_id"])

        val query = database
            .from(Categories)
            .joinReferencesAndSelect()
            .where {
                Categories.id eq categoryId
            }

        val categories = query.map { row ->
            Categories.createEntity(row)
        }

        when (categories.isEmpty()) {
            true -> call.respond(HttpStatusCode.OK, categories.first())
            false -> call.respond(HttpStatusCode.NotFound)
        }
    }

    suspend fun getAll(call: ApplicationCall) {
        val query = database
            .from(Categories)
            .select()

        val categories = query.map {row ->
            Categories.createEntity(row)
        }

        call.respond(HttpStatusCode.OK, categories)
    }

    suspend fun addCategory(call: ApplicationCall) {
        val category = call.receive<Category>()

        val createdCategory = database
            .insertReturning(Categories, Categories.id) {
            set(it.name, category.name)
        }

        when(createdCategory) {
            null -> call.respond(HttpStatusCode.InternalServerError)
            else -> call.respond(HttpStatusCode.OK, mapOf(
                "categoryId" to createdCategory
            ))
        }
    }

    suspend fun removeCategory(call: ApplicationCall) {
        val category = call.receive<Category>()

        val removedCategory = database
            .delete(Categories) {
                it.id eq category.id
            }

        call.respond(HttpStatusCode.OK, mapOf(
            "removed" to removedCategory
        ))
    }
}