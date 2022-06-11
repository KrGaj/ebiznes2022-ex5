package com.example.plugins.routing

import com.example.api.ProductAPI
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.productRouting() {
    routing {
        route("/products") {
            route("all") {
                get() {
                    ProductAPI.getAll(call)
                }
            }

            get {
                ProductAPI.get(call)
            }

            post {
                ProductAPI.addProduct(call)
            }

            delete {
                ProductAPI.removeProduct(call)
            }

            put {
                ProductAPI.updateProduct(call)
            }
        }
    }
}