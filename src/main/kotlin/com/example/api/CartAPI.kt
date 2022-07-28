package com.example.api

import com.example.database.Database
import com.example.model.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.ktorm.dsl.*
import java.util.UUID

object CartAPI {
    private val database = Database.instance

    suspend fun getByUserId(call: ApplicationCall) {
        val userId = UUID.fromString(call.parameters["user_id"])

        val cartQuery = database
            .from(Carts)
            .select()
            .where {
                Carts.userId eq userId
            }

        val carts = cartQuery.map { row ->
            println("${row[Carts.id]}   ${row[Carts.userId]}")
            Carts.createEntity(row)
        }

        val productsQuery = database
            .from(CartProducts)
            .joinReferencesAndSelect()

        val cartProducts = productsQuery.map {row ->
            CartProducts.createEntity(row)
        }.filter {cartProduct ->
                cartProduct.id in carts.map { it.id }
            }

        call.respond(HttpStatusCode.OK, cartProducts)
    }

    suspend fun addProduct(call: ApplicationCall) {
        val cartProduct = call.receive<CartProduct>()

        val id = database.insertAndGenerateKey(CartProducts) {
            set(it.cart, cartProduct.cart.id)
            set(it.product, cartProduct.product.id)
            set(it.amount, cartProduct.amount)
        } as UUID

        val test = database.from(CartProducts).select().where {
            CartProducts.id eq id
        }

        println("CartAPI.addProduct: $test")

        call.respond(HttpStatusCode.OK, mapOf("cartProductId" to id))
    }

    suspend fun addMultipleProducts(call: ApplicationCall) {
        val products = call.receive<List<Product>>()

        for (product in products) {
            database
                .insert(Products) {
                    set(it.name, product.name)
                    set(it.description, product.description)
                    set(it.price, product.price)
                    set(it.available, product.available)
                }
        }
    }

    suspend fun removeProduct(call: ApplicationCall) {
        val cartProduct = call.receive<CartProduct>()

        val removed = database.delete(CartProducts) {
            it.id eq cartProduct.id
        }

        call.respond(HttpStatusCode.OK, mapOf("removed" to removed))
    }

    suspend fun updateProduct(call: ApplicationCall) {
        val cartProduct = call.receive<CartProduct>()

        if (cartProduct.amount == 0) {
            removeProduct(call)
            return
        }

        val updated = database.update(CartProducts) {
            set(it.amount, cartProduct.amount)
        }

        call.respond(HttpStatusCode.OK, mapOf("updated" to updated))
    }
}