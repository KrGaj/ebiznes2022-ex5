package com.example.plugins.routing

import com.example.api.PurchaseAPI
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.purchaseRouting() {
    routing {
        route("/purchases") {
            route("user") {
                get {
                    PurchaseAPI.getByUserId(call)
                }
            }

            get {
                PurchaseAPI.getPurchase(call)
            }

            post {
                PurchaseAPI.addPurchase(call)
            }
        }
    }
}