package com.example.tiendadevinilos.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.tiendadevinilos.model.UserModel
import com.example.tiendadevinilos.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class UserViewModel(private val userPreferencesRepository: UserPreferencesRepository) :
    ViewModel() {

    val userData: LiveData<UserModel> = userPreferencesRepository.userData
        .onEach {
            _isLoadingUser.value = false
        }
        .asLiveData()
    private val _isLoadingUser = MutableLiveData<Boolean>(true)
    val isLoadingUser: LiveData<Boolean> = _isLoadingUser

    fun saveUserData(user_id: String, email: String, fullName: String, picture: String) {
        viewModelScope.launch {
            userPreferencesRepository.saveUserData(
                idUser = user_id, email = email, fullName = fullName, picture = picture

            )
            Log.d("UserViewModel", "User data saved: $user_id, $email, $fullName, $picture")
        }
    }

    fun clearUserData() {
        viewModelScope.launch {
            userPreferencesRepository.clearUserData()
        }
    }
}
