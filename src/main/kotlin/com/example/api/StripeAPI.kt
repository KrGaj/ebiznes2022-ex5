package com.example.api

import com.example.config.Config
import com.stripe.Stripe
import com.stripe.model.PaymentIntent
import com.stripe.param.PaymentIntentCreateParams
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

object StripeAPI {
    suspend fun getSecret(call: ApplicationCall) {
        val amount = call.parameters["amount"]?.toLong() ?: 1L
        Stripe.apiKey = Config.stripe_secret

        val params = PaymentIntentCreateParams
            .builder()
            .setAmount(amount)
            .setCurrency("pln")
            .setAutomaticPaymentMethods(
                PaymentIntentCreateParams.AutomaticPaymentMethods
                    .builder()
                    .setEnabled(true)
                    .build()
            )
            .build()

        val intent = PaymentIntent.create(params)

        call.respond(HttpStatusCode.OK, mapOf("clientSecret" to intent.clientSecret))
    }
}