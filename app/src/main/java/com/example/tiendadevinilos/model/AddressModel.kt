package com.example.tiendadevinilos.model

data class AddressModel(
    val id: String?,
    val street: String,
    val city: String,
    val state: String?,
    val postal_code: String,
    val phone_number: String,
    val user_id: String

)