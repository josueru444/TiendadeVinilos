package com.example.tiendadevinilos.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendadevinilos.model.UserModel
import com.example.tiendadevinilos.network.RetrofitClient
import kotlinx.coroutines.launch

class AddUserViewModel : ViewModel() {
    private val _responseMessage = MutableLiveData<String>()
    val responseMessage: LiveData<String> = _responseMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun addGoogleUser(
        sub: String,
        email: String,
        fullname: String,
        picture: String,
        token: String
    ) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val userRequest = UserModel(sub, email, fullname, picture, token)
                val response = RetrofitClient.instance.AddUserGoogle(userRequest)
                Log.d("Response", response.toString())
            } catch (e: Exception) {
                Log.d("Error", e.toString())
                _error.value = "Error: ${e.message}"

            } finally {
                _isLoading.value = false
            }
        }
    }


}