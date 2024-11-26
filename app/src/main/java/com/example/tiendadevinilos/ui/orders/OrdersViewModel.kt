package com.example.tiendadevinilos.ui.orders

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendadevinilos.model.OrderModel
import com.example.tiendadevinilos.model.OrderResponse
import com.example.tiendadevinilos.network.RetrofitClient
import com.example.tiendadevinilos.network.userIdModelRetro
import kotlinx.coroutines.launch

class OrdersViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _responseStatus = MutableLiveData<Boolean>()
    val responseStatus: LiveData<Boolean> get() = _responseStatus

    private val _orders = MutableLiveData<List<OrderResponse>>()
    val orders: LiveData<List<OrderResponse>> get() = _orders

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun getOrders(user_id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitClient.instance.getAllOrders(userIdModelRetro(user_id))
                if (response.isSuccessful) {
                    response.body()?.let { orderResponse ->
                        val orders = orderResponse.data
                        if (orders != null) {
                            _orders.value = orders
                            _responseStatus.value = true
                        } else {
                            _errorMessage.value = "No se encontraron órdenes"
                            _responseStatus.value = false
                        }
                    } ?: run {
                        _errorMessage.value = "La respuesta del servidor está vacía"
                        _responseStatus.value = false
                    }
                } else {
                    _errorMessage.value = "Error en la respuesta: ${response.message()}"
                    _responseStatus.value = false
                }
            } catch (e: Exception) {
                Log.e("OrderViewModel", "Error al obtener las órdenes", e)
                _errorMessage.value = "Error al obtener las órdenes: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun addNewOrder(orderModel: OrderModel) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = RetrofitClient.instance.addNewOrder(orderModel)
                if (response.isSuccessful) {
                    _responseStatus.value = true
                    Log.d("OrderViewModel", "Orden agregada correctamente")
                } else {
                    _responseStatus.value = false
                    _errorMessage.value = "Error al agregar la orden: ${response.message()}"
                }
            } catch (e: Exception) {
                Log.e("OrderViewModel", "Error al agregar la orden", e)
                _errorMessage.value = "Error al agregar la orden: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

}