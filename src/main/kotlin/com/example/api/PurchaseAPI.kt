package com.example.api

import com.example.database.Database
import com.example.model.Purchase
import com.example.model.Purchases
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.ktorm.dsl.*
import org.ktorm.support.postgresql.insertReturning
import java.util.UUID

object PurchaseAPI {
    private val database = Database.instance
    suspend fun getPurchase(call: ApplicationCall) {
        val purchaseId = UUID.fromString(call.parameters["purchase_id"])

        val query = database
            .from(Purchases)
            .select()
            .where {
                Purchases.id eq purchaseId
            }

        val purchases = query.map {row ->
            Purchases.createEntity(row)
        }

        when (purchases.isEmpty()) {
            true -> call.respond(HttpStatusCode.NotFound)
            false -> call.respond(HttpStatusCode.OK, purchases.first())
        }
    }

    suspend fun getByUserId(call: ApplicationCall) {
        val userId = UUID.fromString(call.parameters["user_id"])

        val query = database
            .from(Purchases)
            .joinReferencesAndSelect()
            .where {
                Purchases.userId eq userId
            }

        val purchases = query.map { row ->
            Purchases.createEntity(row)
        }

        call.respond(HttpStatusCode.OK, purchases)
    }

    suspend fun addPurchase(call: ApplicationCall) {
        val purchase = call.receive<Purchase>()

        val id = database
            .insertReturning(Purchases, Purchases.id) {
                set(it.userId, purchase.user.id)
                set(it.cashSum, purchase.cashSum)
                set(it.payment, purchase.payment.id)
            }

        when(id) {
            null -> call.respond(HttpStatusCode.InternalServerError)
            else -> call.respond(HttpStatusCode.OK, mapOf(
                "purchaseId" to id
            ))
        }
    }
}