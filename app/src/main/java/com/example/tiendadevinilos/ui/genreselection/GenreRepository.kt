package com.example.tiendadevinilos.ui.genreselection

import com.example.tiendadevinilos.model.AddUserGenreModel
import com.example.tiendadevinilos.model.GenreModel
import com.example.tiendadevinilos.network.Responses
import com.example.tiendadevinilos.network.RetrofitClient
import retrofit2.Response

class GenreRepository {
    private val apiService = RetrofitClient.instance

    suspend fun getGenreList(): List<GenreModel> {
        return apiService.getAllGenre()
    }

    suspend fun addUserGenre(genre: AddUserGenreModel): Response<Responses> {

        return apiService.addUserGenre(
           genre
        )
    }

}
