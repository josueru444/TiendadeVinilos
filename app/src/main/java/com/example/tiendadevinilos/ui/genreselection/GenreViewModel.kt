package com.example.tiendadevinilos.ui.genreselection

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendadevinilos.model.AddUserGenreModel
import com.example.tiendadevinilos.model.GenreModel
import com.example.tiendadevinilos.model.ProductModelByUser
import com.example.tiendadevinilos.network.RetrofitClient
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class GenreViewModel : ViewModel() {
    private val repository = GenreRepository()
    private val _genreList = MutableLiveData<List<GenreModel>>()
    val genreList: LiveData<List<GenreModel>> = _genreList

    val _responseMessage = MutableLiveData<String>()
    val responseMessage: LiveData<String> = _responseMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _userGenreList = MutableLiveData<List<ProductModelByUser>>()
    val userGenreList: LiveData<List<ProductModelByUser>> = _userGenreList

    suspend fun getGenreList() {
        try {
            Log.d("GenreViewModel", "Obteniendo géneros")
            _isLoading.value = true
            val genreList = repository.getGenreList()
            if (genreList.isNotEmpty()) {
                _genreList.value = genreList
                Log.d("GenreViewModel", "Géneros obtenidos en el if: $genreList")
            } else {
                Log.d("GenreViewModel", "No se encontraron géneros")
            }
        } catch (e: Exception) {
            Log.e("Error GenreViewModel", "Error al obtener géneros", e)
        } finally {
            Log.d("GenreViewModel", "Finally en getGenreList")
            _isLoading.value = false
        }
    }


    fun getUserGenre(user_id: String) {
        viewModelScope.launch {
            try {
                Log.d("GenreViewModel", "Obteniendo géneros del usuario")
                _isLoading.value = true
                val request = mapOf("user_id" to user_id)
                val response = RetrofitClient.instance.getUserGenre(request)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _responseMessage.value = responseBody?.message?.lowercase() ?: ""
                    _userGenreList.value = responseBody?.data as? List<ProductModelByUser>
                } else {
                    Log.e(
                        "GenreViewModel",
                        "Error en la respuesta: ${response.errorBody()?.string()}"
                    )
                    _responseMessage.value = ""
                }
            } catch (e: Exception) {
                Log.e("GenreViewModel", "Error al obtener géneros", e)
                _responseMessage.value = ""
            } finally {
                Log.d("GenreViewModel", "Cambiando _isLoading a false")
                _isLoading.value = false
                Log.d("GenreViewModel", "isLoading: ${_isLoading.value}")
            }
        }
    }

    fun addUserGenres(
        userId: String,
        genres: List<GenreModel>,
        onComplete: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {

            try {
                Log.d("GenreViewModel", "Agregando géneros al usuario")
                _isLoading.value = true
                val results = genres.map { genre ->
                    async {
                        try {
                            val addGenreModel =
                                AddUserGenreModel(user_id = userId, genre_id = genre.id)
                            Log.d("GenreViewModel", "Agregando género: $addGenreModel")
                            val response = repository.addUserGenre(addGenreModel)

                            if (!response.isSuccessful) {
                                Log.e("GenreViewModel", response.message())
                                throw Exception("Error al agregar género ${genre.genre_name}: ${response.message()}")
                            }
                            true
                        } catch (e: Exception) {
                            Log.e(
                                "GenreViewModel",
                                "Error al agregar género ${genre.genre_name}",
                                e
                            )
                            false
                        }
                    }
                }.awaitAll()

                if (results.all { it }) {
                    onComplete()
                } else {
                    onError("Error al agregar algunos géneros")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Error desconocido")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearUserGenreList() {
        _userGenreList.value = emptyList()
    }

    fun clearIsLoading() {
        _isLoading.value = false

    }

}