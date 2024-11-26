package com.example.tiendadevinilos.ui.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.tiendadevinilos.R
import com.example.tiendadevinilos.Routes
import com.example.tiendadevinilos.ui.genreselection.GenreViewModel
import com.example.tiendadevinilos.viewmodel.UserViewModel
import kotlinx.coroutines.launch

data class DrawerItem(val name: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

@Composable
fun ModalNavigationDrawerSample(
    drawerState: DrawerState,
    navController: NavController,
    userName: String?,
    imgProfile: String?,
    userViewModel: UserViewModel,
    genreViewModel: GenreViewModel = viewModel(),
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()


    val drawerItems = listOf(
        DrawerItem("Inicio", Icons.Filled.Home),
        DrawerItem("Carrito", Icons.Filled.ShoppingCart),
        DrawerItem("Compras", Icons.Filled.ShoppingBag),
        DrawerItem("Contáctanos", Icons.Filled.Email),
        DrawerItem("Cerrar Sesión", Icons.Filled.Logout),

    )

    val selectedItem = remember { mutableStateOf(drawerItems[0]) }

    ModalNavigationDrawer(
        scrimColor = Color.Black.copy(alpha = 0.6f),
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                Modifier.width(250.dp),
                drawerContainerColor = Color.White,
                drawerContentColor = Color.Black,
                drawerShape = MaterialTheme.shapes.large
            ) {

                ProfileSection(
                    navController,
                    userName = userName,
                    imgProfile = imgProfile,
                    drawerState = drawerState
                )

                Spacer(Modifier.height(12.dp))
                drawerItems.forEach { item ->
                    if (userName == "" && (item.name == "Carrito" || item.name == "Compras" || item.name == "Cerrar Sesión" || item.name == "Contáctanos")) {
                        return@forEach
                    }

                    NavigationDrawerItem(
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedContainerColor = Color.White,
                            selectedContainerColor = colorResource(R.color.input_background),
                            unselectedTextColor = colorResource(R.color.product_name),
                            unselectedIconColor = colorResource(R.color.product_name),
                            selectedTextColor = Color.Black,
                            selectedIconColor = Color.Black
                        ),
                        icon = { Icon(item.icon, contentDescription = null) },
                        label = { Text(item.name) },
                        selected = item == selectedItem.value,
                        onClick = {
                            selectedItem.value = item
                            scope.launch {
                                when (item.name) {
                                    "Cerrar Sesión" -> {
                                        userViewModel.clearUserData()
                                        genreViewModel.clearUserGenreList()
                                        navController.navigate(Routes.homePage)

                                    }

                                    "Inicio" -> navController.navigate(Routes.homePage)
                                    "Carrito" -> navController.navigate(Routes.cartPage)
                                    "Compras" -> navController.navigate(Routes.orderPage)
                                    "Contáctanos"->navController.navigate(Routes.chatPage)
                                }


                                drawerState.close()
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        },
        content = {
            content()
        }
    )
}

@Composable
private fun ProfileSection(
    navController: NavController,
    drawerState: DrawerState,
    imgProfile: String?,
    userName: String?
) {
    val scope = rememberCoroutineScope()
    Row(
        Modifier
            .padding(16.dp)
            .clip(CircleShape)
            .clickable(
                onClick = {
                    scope.launch {
                        drawerState.close()
                    }
                    navController.navigate("loginPage")
                }
            ),
        verticalAlignment = Alignment.CenterVertically
    )
    {
        AsyncImage(
            model = if (imgProfile == "") {
                "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png"
            } else {
                imgProfile ?: ""
            },
            contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .size(60.dp)
        )
        Column(
            Modifier
                .padding(start = 8.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (userName == "") {
                    "Inicia Sesión"
                } else {
                    userName ?: ""
                },
                color = Color.Black,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
            )
            Text(
                text = "Ver perfil",
                color = colorResource(R.color.product_name),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,

                )
        }
    }
    Log.d("imgProfile", imgProfile.toString())
}
