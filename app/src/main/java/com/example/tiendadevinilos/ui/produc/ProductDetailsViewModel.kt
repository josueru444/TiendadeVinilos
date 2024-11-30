package com.example.tiendadevinilos.ui.produc

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendadevinilos.model.ProductModel
import com.example.tiendadevinilos.repository.ProductRepository
import kotlinx.coroutines.launch

class ProductDetailsViewModel : ViewModel() {
    private val repository = ProductRepository()
    private val _product = MutableLiveData<ProductModel?>()
    val product: LiveData<ProductModel?> = _product

    private val _isLoading = MutableLiveData<Boolean>(true)
    val isLoading: LiveData<Boolean> = _isLoading

    fun getProductById(id: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val productDetails = repository.getProductById(id)
                _product.value = productDetails
                //Log.d("ProductDetailsViewModel", "Producto obtenido: $productDetails")
            } catch (e: Exception) {
                Log.e("ProductDetailsViewModel", "Error al obtener el producto", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}
