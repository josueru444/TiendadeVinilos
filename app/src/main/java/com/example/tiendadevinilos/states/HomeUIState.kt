package com.example.tiendadevinilos.states

import com.example.tiendadevinilos.model.ProductModel

sealed class HomeUiState {
    data object Loading : HomeUiState()
    data class Success(val albums: List<ProductModel>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}