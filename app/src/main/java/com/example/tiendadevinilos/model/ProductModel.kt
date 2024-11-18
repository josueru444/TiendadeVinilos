package com.example.tiendadevinilos.model

import java.math.BigInteger

data class ProductModel(
    val id_vinyl: BigInteger,
    val name: String,
    val price: Double,
    val quantity: Int,
    val img_url: String,
    val description: String,
    val igGenre: Int,
    val createdAt: String,
    val modifiedAt: String
)

data class ProductModelByUser(
    val id_vinyl: BigInteger?,
    val name: String?,
    val price: Double?,
    val img_url: String?,
    val genre_name: String?,

    )
