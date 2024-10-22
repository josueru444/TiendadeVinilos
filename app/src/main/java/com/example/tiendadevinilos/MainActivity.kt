package com.example.tiendadevinilos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tiendadevinilos.pages.HomePage
import com.example.tiendadevinilos.pages.LoginPage
import com.example.tiendadevinilos.ui.theme.TiendaDeVinilosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TiendaDeVinilosTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Routes.loginPage, builder = {
                    composable(Routes.homePage) {
                        HomePage(navController)
                    }
                    composable(Routes.loginPage) {
                        LoginPage(navController)
                    }
                })
            }
        }
    }
}

