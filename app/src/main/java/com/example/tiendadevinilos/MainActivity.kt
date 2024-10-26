package com.example.tiendadevinilos
import LoginPage
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tiendadevinilos.pages.HomePage
import com.example.tiendadevinilos.pages.ProductDetails
import com.example.tiendadevinilos.repository.UserPreferencesRepository
import com.example.tiendadevinilos.ui.theme.TiendaDeVinilosTheme
import com.example.tiendadevinilos.viewmodel.UserViewModel

class MainActivity : ComponentActivity() {
    private lateinit var userViewModel: UserViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userPreferencesRepository = UserPreferencesRepository(applicationContext)
        userViewModel = UserViewModel(userPreferencesRepository)


        enableEdgeToEdge()
        setContent {
            TiendaDeVinilosTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Routes.homePage,
                    builder = {
                        composable(Routes.homePage) {
                            HomePage(navController, userViewModel = userViewModel)
                        }
                        composable(Routes.loginPage) {
                            LoginPage(navController)
                        }
                        composable(Routes.productPage) { backStackEntry ->
                            val idProduct = backStackEntry.arguments?.getString("idProduct")
                            ProductDetails(navController, idProduct ?: "0")
                        }
                    })
            }
        }
    }
}

