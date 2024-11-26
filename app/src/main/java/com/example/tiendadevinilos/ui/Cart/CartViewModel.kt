package com.example.tiendadevinilos.ui.Cart

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendadevinilos.model.AddressModel
import com.example.tiendadevinilos.model.CartItemRequest
import com.example.tiendadevinilos.model.CartItemResponse
import com.example.tiendadevinilos.model.DeteleCartItemRequest
import com.example.tiendadevinilos.model.GetItemsCartList
import com.example.tiendadevinilos.model.ModifyCartRequest
import com.example.tiendadevinilos.network.RetrofitClient
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {
    private val _cartItems = MutableLiveData<List<CartItemResponse>>()
    val cartItems: MutableLiveData<List<CartItemResponse>> get() = _cartItems

    private val _addresses = MutableLiveData<List<AddressModel>?>()
    val addresses: MutableLiveData<List<AddressModel>?> get() = _addresses

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _responseStatus = MutableLiveData<Boolean>()
    val responseStatus: LiveData<Boolean> = _responseStatus


    fun resetResponseStatus() {
        _responseStatus.value = false
    }

    private fun clearError() {
        _errorMessage.value = ""
    }

    fun addToCart(user_id: String, id_vinyl: String, quantity: Int) {
        viewModelScope.launch {
            try {
                clearError()
                _isLoading.value = true
                val request = CartItemRequest(user_id, id_vinyl, quantity)
                val response = RetrofitClient.instance.AddItemCart(request)
                Log.d("Response", response.toString())
                _responseStatus.value = true

            } catch (e: Exception) {
                Log.e("Error", "Error al agregar al carrito")
                _errorMessage.value = "Error al agregar al carrito: ${e.message}"
                _responseStatus.value = false

            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getCartItems(user_id: String) {
        viewModelScope.launch {
            try {
                clearError()
                _isLoading.value = true
                val request = GetItemsCartList(user_id = user_id)
                val response = RetrofitClient.instance.getCartItems(request)

                if (response.isSuccessful) {
                    response.body()?.let { cartResponse ->
                        val cartItems = cartResponse.cart
                        _cartItems.value = cartItems
                        val addresses = cartResponse.addresses
                        Log.d("Addresses", addresses.toString())
                        _addresses.value = addresses
                        _responseStatus.value = true
                    } ?: run {
                        _errorMessage.value = "La respuesta está vacía"
                        _responseStatus.value = false
                    }
                } else {
                    _errorMessage.value = "Error en la respuesta: ${response.errorBody()?.string()}"
                    _responseStatus.value = false
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al Cargar el carrito: ${e.message}"
                _responseStatus.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun modifyQuantity(cart_id: String, id_vinyl: String, quantity: Int) {
        viewModelScope.launch {
            try {
                clearError()
                _isLoading.value = true
                val request = ModifyCartRequest(cart_id, id_vinyl, quantity)
                val response = RetrofitClient.instance.modifyQuantityCartItems(request)

                if (response.isSuccessful) {
                    _responseStatus.value = true
                    _cartItems.value?.let { currentItems ->
                        val updatedItems = currentItems.map { item ->
                            if (item.id_vinyl == id_vinyl) {
                                item.copy(quantity = quantity)
                            } else {
                                item
                            }
                        }
                        _cartItems.value = updatedItems
                    }
                } else {
                    _errorMessage.value =
                        "Error al modificar la cantidad: ${response.errorBody()?.string()}"
                    _responseStatus.value = false
                }

            } catch (e: Exception) {
                Log.e("Error", "Error al modificar la cantidad")
                _errorMessage.value = "Error al modificar la cantidad: ${e.message}"
                _responseStatus.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteCartItem(cart_id: String, id_vinyl: String) {
        viewModelScope.launch {
            try {
                clearError()
                _isLoading.value = true
                val request = DeteleCartItemRequest(cart_id = cart_id, id_vinyl = id_vinyl)
                val response = RetrofitClient.instance.deleteCartItem(request)
                if (response.isSuccessful) {
                    _responseStatus.value = true
                    // Filtra el elemento eliminado y actualiza _cartItems
                    _cartItems.value = _cartItems.value?.filter { it.id_vinyl != id_vinyl }
                } else {
                    _errorMessage.value =
                        "Error al eliminar el producto: ${response.errorBody()?.string()}"
                    _responseStatus.value = false
                }
            } catch (e: Exception) {
                Log.e("Error", "Error al eliminar el producto")
                _errorMessage.value = "Error al eliminar el producto: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }


}