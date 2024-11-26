package com.example.tiendadevinilos.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.tiendadevinilos.model.UserModel
import com.example.tiendadevinilos.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class UserViewModel(private val userPreferencesRepository: UserPreferencesRepository) :
    ViewModel() {

    private val _isLoadingUser = MutableLiveData<Boolean>(true)
    val isLoadingUser: LiveData<Boolean> = _isLoadingUser

    val userData: LiveData<UserModel> = userPreferencesRepository.userData
        .onStart {
            // Indicar que se está cargando al iniciar el flujo
            _isLoadingUser.value = true
        }
        .catch { error ->
            // Manejo de errores del flujo
            Log.e("UserViewModel", "Error loading user data: ${error.message}")
            _isLoadingUser.value = false
        }
        .onEach {
            // Finalizar la carga cuando los datos se obtienen
            _isLoadingUser.value = false
        }
        .asLiveData()

    fun saveUserData(
        user_id: String,
        email: String,
        fullName: String,
        picture: String,
        token: String
    ) {
        viewModelScope.launch {
            _isLoadingUser.value = true
            try {
                userPreferencesRepository.saveUserData(
                    idUser = user_id,
                    email = email,
                    fullName = fullName,
                    picture = picture,
                    token = token
                )
                Log.d("UserViewModel", "User data saved successfully")
            } catch (e: Exception) {
                Log.e("UserViewModel", "Error saving user data: ${e.message}")
            } finally {
                _isLoadingUser.value = false
            }
        }
    }

    fun clearUserData() {
        viewModelScope.launch {
            _isLoadingUser.value = true
            try {
                userPreferencesRepository.clearUserData()
                Log.d("UserViewModel", "User data cleared successfully")
            } catch (e: Exception) {
                Log.e("UserViewModel", "Error clearing user data: ${e.message}")
            } finally {
                _isLoadingUser.value = false
            }
        }
    }
}
