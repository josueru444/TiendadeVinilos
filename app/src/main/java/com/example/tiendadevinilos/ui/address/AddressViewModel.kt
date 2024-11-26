package com.example.tiendadevinilos.ui.address

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendadevinilos.model.AddressModel
import com.example.tiendadevinilos.network.RetrofitClient
import kotlinx.coroutines.launch

class AddressViewModel : ViewModel() {
    private val _addresses = MutableLiveData<List<AddressModel>>()
    val addresses: LiveData<List<AddressModel>> = _addresses

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _responseMessage = MutableLiveData<String>()
    val responseMessage: LiveData<String> = _responseMessage

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error


    fun addAddress(address: AddressModel) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = RetrofitClient.instance.addNewAddress(address)
                val message = response.firstOrNull()?.success
                Log.d("Response", response.toString())
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}