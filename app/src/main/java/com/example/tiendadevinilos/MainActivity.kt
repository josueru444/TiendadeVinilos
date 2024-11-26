package com.example.tiendadevinilos

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
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
                setStatusBarColor(color = Color.White)
                MainScreen(
                    userViewModel = userViewModel,
                    biometricAuthenticator = biometricAuthenticator
                )
            }
        }
    }
}
@SuppressLint("ComposableNaming")
@Composable
fun setStatusBarColor(color: Color){
    val view = LocalView.current

    if (!view.isInEditMode){
        LaunchedEffect(key1 = true) {
            val window =(view.context as Activity).window
            window.statusBarColor = color.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars=true

        }
    }
}
