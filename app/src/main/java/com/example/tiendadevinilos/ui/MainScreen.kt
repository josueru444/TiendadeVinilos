package com.example.tiendadevinilos.ui

import LoginPage
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.BarChart
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
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.tiendadevinilos.R
import com.example.tiendadevinilos.Routes
import com.example.tiendadevinilos.biometric.BiometricAuthenticator
import com.example.tiendadevinilos.model.UserModel
import com.example.tiendadevinilos.ui.Cart.CartPage
import com.example.tiendadevinilos.ui.address.addAddress
import com.example.tiendadevinilos.ui.chatbot.ChatPage
import com.example.tiendadevinilos.ui.components.ModalNavigationDrawerSample
import com.example.tiendadevinilos.ui.genreselection.GenreSelectionPage
import com.example.tiendadevinilos.ui.home.HomePage
import com.example.tiendadevinilos.ui.orders.OrderPage
import com.example.tiendadevinilos.ui.produc.ProductDetails
import com.example.tiendadevinilos.ui.search.SearchPage
import com.example.tiendadevinilos.viewmodel.UserViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import ir.ehsannarmani.compose_charts.ui.ChartPage
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    userViewModel: UserViewModel,
    biometricAuthenticator: BiometricAuthenticator,
) {
            FirebaseInstallations.getInstance().id.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val installationId = task.result
                    // Imprime o muestra el ID para probar
                    Log.d("FirebaseInstallationID", "ID de instalación: $installationId")
                } else {
                    Log.e("FirebaseInstallationID", "Error al obtener el ID de instalación", task.exception)
                }
            }

    FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
        if (!task.isSuccessful) {
            Log.w(TAG, "Fetching FCM registration token failed", task.exception)
            return@OnCompleteListener
        }

        // Get new FCM registration token
        val token = task.result

        // Log and toast

        Log.d("token", token)
    })

    //User info
    LocalContext.current
    val userData = userViewModel.userData.observeAsState(
        initial = UserModel(
            user_id = null,
            email = null,
            fullName = null,
            picture = null,
            token = null
        )
    )
    var user_id = userData.value.user_id.toString().takeIf { it != "null" } ?: ""
    var token = userData.value.token.toString().takeIf { it != "null" } ?: ""
    Log.d("Token", token)

    var isLoadingUser = userViewModel.isLoadingUser.observeAsState(true).value


    //Navigation
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route


    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var search by remember { mutableStateOf("") }

    var clicked by remember { mutableStateOf(false) }

    fun toggleClick() {
        clicked = !clicked // Cambia su valor
        println("Clicked ahora es: $clicked")
    }



    ModalNavigationDrawerSample(
        drawerState = drawerState,
        navController = navController,
        userName = userData.value.fullName ?: "",
        imgProfile = userData.value.picture ?: "",
        userViewModel = userViewModel
    ) {
        Scaffold(
            topBar = {
                if (currentRoute == Routes.searchPage) {
                    DisposableEffect(Unit) {
                        onDispose {
                            clicked = false
                            search = ""
                        }
                    }
                    CenterAlignedTopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            actionIconContentColor = Color.Black,
                            navigationIconContentColor = Color.Black,
                            containerColor = Color.White,
                            titleContentColor = Color.Black,
                        ),
                        title = {
                            TextField(
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    containerColor = colorResource(R.color.input_background),
                                    focusedBorderColor = Color.Transparent,
                                    unfocusedBorderColor = Color.Transparent,
                                    focusedTextColor = colorResource(R.color.text_color_input),
                                    unfocusedTextColor = colorResource(R.color.text_color_input_label),
                                ),
                                modifier = Modifier.scale(scaleY = 0.9f, scaleX = 1f),
                                shape = RoundedCornerShape(10.dp),
                                textStyle = TextStyle(
                                    fontSize = 15.sp,
                                    color = Color.Black
                                ),
                                value = search,
                                singleLine = true,
                                onValueChange = { search = it },
                                placeholder = { Text("Buscar") }
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                navController.popBackStack()
                            }) {
                                Icon(Icons.Filled.ArrowBackIosNew, "")
                            }
                        },
                        actions = {
                            IconButton(onClick = {
                                clicked = true
                            }) {
                                Icon(Icons.Filled.Search, "")
                            }
                        })
                } else if (currentRoute != Routes.loginPage) {
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
                        if (currentRoute == Routes.orderPage) {
                            IconButton(onClick = {
                                navController.navigate(Routes.chartPage)
                            }) {
                                Icon(
                                    Icons.Filled.BarChart,
                                    contentDescription = "Chart",
                                    tint = Color.Black,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        } else if (currentRoute != Routes.chartPage) {
                            IconButton(onClick = {
                                navController.navigate(Routes.searchPage)
                            }) {
                                Icon(
                                    Icons.Filled.Search,
                                    contentDescription = "Search",
                                    tint = Color.Black,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
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
                        LoginPage(navController, user_id, isLoadingUser)
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
                    composable(Routes.orderPage) {
                        OrderPage(
                            user_id = user_id
                        )
                    }
                    composable(Routes.chatPage) {
                        ChatPage()
                    }
                    composable(Routes.newAddressPage) {
                        addAddress(user_id = user_id, navController = navController)
                    }
                    composable(Routes.chartPage) {
                        ChartPage(user_id = user_id)
                    }
                    composable(Routes.searchPage) {
                        SearchPage(
                            search = search,
                            clicked = clicked,
                            navController = navController
                        )
                    }
                })
        }
    }
}
