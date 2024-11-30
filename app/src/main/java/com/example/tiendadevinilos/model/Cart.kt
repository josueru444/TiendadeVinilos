package com.example.tiendadevinilos.model

data class CartItemRequest(
    val user_id: String,
    val id_vinyl: String,
    val quantity: Int,
)

data class CartItemResponse(
    val id_vinyl: String,
    val name: String,
    val price: Float,
    val img_url: String,
    val stock: Int,
    val quantity: Int,
    val cart_id: String? = null,
    val addresses: List<AddressModel>? = null,
)

data class GetCartResponse(
    val status: String,
    val cart: List<CartItemResponse>,
    val addresses: List<AddressModel>,
)

data class GetItemsCartList(
    val user_id: String
)

data class ModifyCartRequest(
    val cart_id: String? = null,
    val id_vinyl: String,
    val quantity: Int,
)

data class DeteleCartItemRequest(
    val cart_id: String? = null,
    val id_vinyl: String,
)