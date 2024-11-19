package com.example.tiendadevinilos.ui

import LoginPage
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.tiendadevinilos.Routes
import com.example.tiendadevinilos.biometric.BiometricAuthenticator
import com.example.tiendadevinilos.model.UserModel
import com.example.tiendadevinilos.ui.Cart.CartPage
import com.example.tiendadevinilos.ui.components.ModalNavigationDrawerSample
import com.example.tiendadevinilos.ui.genreselection.GenreSelectionPage
import com.example.tiendadevinilos.ui.genreselection.GenreViewModel
import com.example.tiendadevinilos.ui.home.HomePage
import com.example.tiendadevinilos.ui.produc.ProductDetails
import com.example.tiendadevinilos.viewmodel.ProductsViewModel
import com.example.tiendadevinilos.viewmodel.UserViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    userViewModel: UserViewModel,
    biometricAuthenticator: BiometricAuthenticator,
    genreViewModel: GenreViewModel = viewModel(),
) {

    //User info
    LocalContext.current
    val userData = userViewModel.userData.observeAsState(
        initial = UserModel(
            user_id = null,
            email = null,
            fullName = null,
            picture = null
        )
    )
    var user_id = userData.value.user_id.toString().takeIf { it != "null" } ?: ""
    var isLoadingUser = userViewModel.isLoadingUser.observeAsState(true).value





    //Navigation
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route


    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawerSample(
        drawerState = drawerState,
        navController = navController,
        userName = userData.value.fullName ?: "",
        imgProfile = userData.value.picture ?: "",
        userViewModel = userViewModel
    ) {
        Scaffold(
            topBar = {
                if (currentRoute != Routes.loginPage) {
                    CenterAlignedTopAppBar(colors = TopAppBarDefaults.topAppBarColors(
                        actionIconContentColor = Color.Black,
                        navigationIconContentColor = Color.Black,
                        containerColor = Color.White,
                        titleContentColor = Color.Black,
                    ), title = {
                        Text(
                            text = "Tienda de Vinilos",
                            fontWeight = FontWeight.Medium,
                            fontSize = 25.sp
                        )
                    }, navigationIcon = {
                        if (currentRoute != Routes.homePage) {
                            IconButton(
                                onClick = {
                                    navController.popBackStack()
                                }
                            ) {
                                Icon(
                                    Icons.Filled.ArrowBackIosNew,
                                    contentDescription = "",
                                    tint = Color.Black
                                )
                            }
                        } else
                            IconButton(onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }) {
                                Icon(
                                    Icons.Filled.Menu,
                                    contentDescription = "Menu",
                                    tint = Color.Black,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                    }, actions = {
                        IconButton(onClick = {
                            navController.navigate(Routes.genreSelectionPage)
                        }) {
                            Icon(
                                Icons.Filled.Search,
                                contentDescription = "Search",
                                tint = Color.Black,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    })
                }
                if (currentRoute == Routes.loginPage || currentRoute == Routes.genreSelectionPage) {
                    CenterAlignedTopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            actionIconContentColor = Color.Black,
                            navigationIconContentColor = Color.Black,
                            containerColor = Color.White,
                            titleContentColor = Color.Black,
                        ),
                        title = {
                            Text(
                                text = "Tienda de Vinilos",
                                fontWeight = FontWeight.Medium,
                                fontSize = 25.sp
                            )
                        },
                        navigationIcon = {},
                        actions = {})
                }
            },
            floatingActionButton = {
                if (currentRoute == Routes.homePage) {
                    FloatingActionButton(
                        onClick = {
                            if (userData.value.user_id.toString().isNotEmpty()) {
                                navController.navigate("cartPage")
                            } else {
                                navController.navigate("loginPage")
                            }
                        },
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ) {
                        Icon(Icons.Filled.ShoppingCart, contentDescription = "Cart")
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Routes.homePage,
                modifier = Modifier.padding(innerPadding),
                builder = {
                    composable(Routes.homePage) {
                        HomePage(
                            navController = navController,
                            user_id = user_id,
                            loadingUser = isLoadingUser
                        )


                    }
                    composable(Routes.loginPage) {
                        LoginPage(navController)
                    }
                    composable(Routes.productPage) { backStackEntry ->
                        val idProduct = backStackEntry.arguments?.getString("idProduct")
                        ProductDetails(
                            navController,
                            idProduct ?: "0",
                            userViewModel = userViewModel,
                            biometricAuthenticator = biometricAuthenticator
                        )
                    }
                    composable(Routes.cartPage) {
                        CartPage(navController, userViewModel = userViewModel)
                    }
                    composable(Routes.genreSelectionPage) {
                        GenreSelectionPage(
                            user_id = user_id,
                            navController = navController
                        )
                    }
                })
        }
    }
}
