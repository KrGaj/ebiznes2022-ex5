package com.example.api

import com.example.database.Database
import com.example.model.*
import com.example.model.session.JwtUserInfo
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.ktorm.dsl.*
import java.util.UUID

object CartAPI {
    private val database = Database.instance

    suspend fun getByUserId(call: ApplicationCall) {
        println("user_id: ${call.parameters["user_id"]}")
        val userId = UUID.fromString(call.parameters["user_id"])

        val principal = call.principal<JwtUserInfo>()
        println("Received: ${principal?.email}")

        val productsQuery = database
            .from(CartProducts)
            .joinReferencesAndSelect()
            .where {
                CartProducts.user eq userId
            }

        val cartProducts = productsQuery.map {row ->
            CartProducts.createEntity(row)
        }

        call.respond(HttpStatusCode.OK, cartProducts)
    }

    suspend fun addProduct(call: ApplicationCall) {
        val cartProduct = call.receive<CartProduct>()

        val id = database.insertAndGenerateKey(CartProducts) {
            set(it.user, cartProduct.user.id)
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