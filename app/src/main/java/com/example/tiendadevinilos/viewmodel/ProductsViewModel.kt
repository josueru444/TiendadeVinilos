package com.example.tiendadevinilos.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendadevinilos.model.ProductModel
import com.example.tiendadevinilos.repository.ProductRepository
import kotlinx.coroutines.launch

class ProductsViewModel : ViewModel() {
    private val repository = ProductRepository()

    private val _products = MutableLiveData<List<ProductModel>>()
    val products: LiveData<List<ProductModel>> = _products

    private val _carouselProducts = MutableLiveData<List<ProductModel>>()
    val carouselProducts: LiveData<List<ProductModel>> = _carouselProducts

    private val _isLoading = MutableLiveData<Boolean>(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        if (_products.value == null) {
            getProducts()
        }
    }

    fun getProducts() {
        viewModelScope.launch {
            try {
                val productList = repository.getProducts()
                if (productList.isNotEmpty()) {
                    _products.value = productList
                    _carouselProducts.value = productList.shuffled().take(6)
                    Log.d("ProductsViewModel", "Productos obtenidos: $productList")
                } else {
                    _error.value = "No se encontraron productos"
                }
            } catch (e: Exception) {
                _error.value = "Error al obtener productos: ${e.message}"
                Log.e("Error ProductsViewModel", "Error al obtener productos", e)
            } finally {
                _isLoading.value = false
                Log.d("ProductsViewModel", "isLoading actualizado: ${_isLoading.value}")
            }
        }
    }



}