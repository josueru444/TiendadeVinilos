package com.example.tiendadevinilos

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.FragmentActivity
import com.example.tiendadevinilos.biometric.BiometricAuthenticator
import com.example.tiendadevinilos.repository.UserPreferencesRepository
import com.example.tiendadevinilos.ui.MainScreen
import com.example.tiendadevinilos.ui.theme.TiendaDeVinilosTheme
import com.example.tiendadevinilos.viewmodel.UserViewModel

class MainActivity : FragmentActivity() {
    private lateinit var userViewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val biometricAuthenticator = BiometricAuthenticator(this)
        val userPreferencesRepository = UserPreferencesRepository(applicationContext)
        userViewModel = UserViewModel(userPreferencesRepository)

        enableEdgeToEdge()
        setContent {
            TiendaDeVinilosTheme {
            MainScreen(userViewModel = userViewModel,biometricAuthenticator=biometricAuthenticator)
            }
        }
    }
}

