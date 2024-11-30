package com.example.tiendadevinilos.model

data class StatisticsModel(
    var genre_name: String,
    var total_quantity: Int,
    var total_price: Double

)

data class StatisticsResponse(
    val success: Boolean,
    val message: String,
    val data: List<StatisticsModel>
)