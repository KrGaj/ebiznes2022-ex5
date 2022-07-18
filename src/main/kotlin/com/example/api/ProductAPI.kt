package com.example.api

import com.example.database.Database
import com.example.model.Product
import com.example.model.Products
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.ktorm.dsl.*
import org.ktorm.support.postgresql.insertReturning
import java.util.*

object ProductAPI {
    private val database = Database.instance

    suspend fun get(call: ApplicationCall) {
        val productId = UUID.fromString(call.parameters["product_id"])

        val query = database
            .from(Products)
            .joinReferencesAndSelect()
            .where {
                Products.id eq productId
            }

        val products = query.map { row ->
            Products.createEntity(row)
        }

        when(products.isEmpty()) {
            true -> call.respond(HttpStatusCode.NotFound)
            false -> call.respond(HttpStatusCode.OK, products.first())
        }
    }

    suspend fun getAll(call: ApplicationCall) {
        val query = database
            .from(Products)
            .joinReferencesAndSelect()

        val products = query.map { row ->
            Products.createEntity(row)
        }

        call.respond(HttpStatusCode.OK, products)
    }

    suspend fun addProduct(call: ApplicationCall) {
        val product = call.receive<Product>()

        val addedProduct = database
            .insertReturning(Products, Products.id) {
                set(it.name, product.name)
                set(it.description, product.description)
                set(it.price, product.price)
                set(it.category, product.category.id)
                set(it.available, product.available)
            }

        when(addedProduct) {
            null -> call.respond(HttpStatusCode.InternalServerError)
            else -> call.respond(HttpStatusCode.OK, mapOf(
                "productId" to addedProduct
            ))
        }
    }

    suspend fun removeProduct(call: ApplicationCall) {
        val product = call.receive<Product>()

        val removed = database
            .delete(Products) {
                Products.id eq product.id
            }

        call.respond(HttpStatusCode.OK, mapOf(
            "removedProducts" to removed
        ))
    }

    suspend fun updateProduct(call: ApplicationCall) {
        val product = call.receive<Product>()

        val updated = database
            .update(Products) {
                set(it.name, product.name)
                set(it.description, product.description)
                set(it.price, product.price)
                set(it.category, product.category.id)
                set(it.available, product.available)

                where {
                    it.id eq product.id
                }
            }

        call.respond(HttpStatusCode.OK, mapOf(
            "updatedProducts" to updated
        ))
    }
}
