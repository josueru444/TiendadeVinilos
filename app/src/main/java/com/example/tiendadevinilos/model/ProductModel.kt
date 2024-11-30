package com.example.tiendadevinilos.model

data class ProductModel(
    val id_vinyl: String?,
    val name: String,
    val price: Double,
    val quantity: Int,
    val img_url: String,
    val description: String,
    val igGenre: Int,
    val genre_name: String?,
    val createdAt: String,
    val modifiedAt: String
)

data class ProductModelByUser(
    val id_vinyl: String?,
    val name: String?,
    val price: Double?,
    val img_url: String?,
    val genre_name: String?,

    )
