package com.example.api

import com.example.database.Database
import com.example.model.Payment
import com.example.model.Payments
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.ktorm.dsl.*
import org.ktorm.support.postgresql.insertReturning
import java.util.UUID

object PaymentAPI {
    private val database = Database.instance

    suspend fun getById(call: ApplicationCall) {
        val paymentId = UUID.fromString(call.parameters["payment_id"])

        val query = database
            .from(Payments)
            .joinReferencesAndSelect()
            .where {
                Payments.id eq paymentId
            }

        val payments = query.map { row ->
            Payments.createEntity(row)
        }

        when(payments.isEmpty()) {
            true -> call.respond(HttpStatusCode.NotFound)
            false -> call.respond(HttpStatusCode.OK, payments.first())
        }
    }

    suspend fun addPayment(call: ApplicationCall) {
        val payment = call.receive<Payment>()

        val id = database
            .insertReturning(Payments, Payments.id) {
                set(it.date, payment.date)
                set(it.method, payment.method)
                set(it.paid, payment.paid)
            }

        when(id) {
            null -> call.respond(HttpStatusCode.InternalServerError)
            else -> call.respond(HttpStatusCode.OK)
        }
    }
}