package com.example.api.response

import com.example.model.Cart
import com.example.model.CartProduct

data class CartResponseGet(
    val carts: List<Cart>,
    val products: List<CartProduct>
)
