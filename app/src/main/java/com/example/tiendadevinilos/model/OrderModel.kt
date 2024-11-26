package com.example.tiendadevinilos.model

data class OrderModel(
    val user_id: String,
    val total: Double,
    val items: List<OrderItemModel>
)

data class OrderItemModel(
    val id_vinyl: String,
    val quantity: Int,
    val price: Double,
)

data class OrderResponse(
    val id: Int,
    val price: Float,
    val status: String,
    val name: String,
    val img_url: String
)

data class OrderApiResponse(
    val success: Boolean,
    val message: String,
    val data: List<OrderResponse>
)