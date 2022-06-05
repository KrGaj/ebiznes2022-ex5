package com.example.api

import com.example.api.response.CartResponseGet
import com.example.database.Database
import com.example.model.CartProduct
import com.example.model.CartProducts
import com.example.model.Carts
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.ktorm.dsl.*
import java.util.UUID

class CartAPI {
    private val database = Database.instance

    suspend fun get(call: ApplicationCall) {
        val userId = UUID.fromString(call.parameters["userId"])

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
            .select()

        val cartProducts = productsQuery.map {row ->
            CartProducts.createEntity(row)
        }
            .filter {cartProduct ->
                cartProduct.id in carts.map { it.id }
            }

        val response = CartResponseGet(carts, cartProducts)

        call.respond(response)
    }

    suspend fun addProduct(call: ApplicationCall) {
        val cartProduct = call.receive<CartProduct>()

        val id = database.insertAndGenerateKey(CartProducts) {
            set(it.cart, cartProduct.cart.id)
            set(it.productId, cartProduct.product.id)
            set(it.amount, cartProduct.amount)
        } as UUID

        val test = database.from(CartProducts).select().where {
            CartProducts.id eq id
        }

        println("CartAPI.addProduct: $test")

        call.respond(HttpStatusCode.OK)
    }

    suspend fun removeProduct(call: ApplicationCall) {
        val cartProduct = call.receive<CartProduct>()

        database.delete(CartProducts) {
            it.id eq cartProduct.id
        }

        call.respond(HttpStatusCode.OK)
    }

    suspend fun updateProduct(call: ApplicationCall) {
        val cartProduct = call.receive<CartProduct>()

        database.update(CartProducts) {
            set(it.amount, cartProduct.amount)
        }

        call.respond(HttpStatusCode.OK)
    }
}