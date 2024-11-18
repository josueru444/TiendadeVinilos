package com.example.tiendadevinilos.model

data class GenreModel(
    val id: Int,
    val genre_name: String,
    val img: String

)

data class AddUserGenreModel(
    val user_id: String,
    val genre_id: Int
)