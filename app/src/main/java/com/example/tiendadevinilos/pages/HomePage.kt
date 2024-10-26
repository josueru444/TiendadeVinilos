package com.example.tiendadevinilos.pages

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.packInts
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.tiendadevinilos.R
import com.example.tiendadevinilos.components.CustomCarousel
import com.example.tiendadevinilos.components.ModalNavigationDrawerSample
import com.example.tiendadevinilos.components.shimmerEffect
import com.example.tiendadevinilos.model.ProductModel
import com.example.tiendadevinilos.model.UserModel
import com.example.tiendadevinilos.viewmodel.ProductsViewModel
import com.example.tiendadevinilos.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    navController: NavController,
    productViewModel: ProductsViewModel = viewModel(),
    userViewModel: UserViewModel
) {

    val context = LocalContext.current
    val userData = userViewModel.userData.observeAsState(
        initial = UserModel(
            user_id = null,
            email = null,
            fullName = null,
            picture = null
        )
    )
    if (userData.value.picture == null) {
        Toast.makeText(context, "picture: ${userData.value.picture}", Toast.LENGTH_SHORT)
            .show()
    }

    val products = productViewModel.products.observeAsState(emptyList()).value
    val carouselProducts = productViewModel.carouselProducts.observeAsState(emptyList()).value
    val isLoading = productViewModel.isLoading.observeAsState(true).value

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
                TopBar(drawerState = drawerState, scope = scope)
            }, content = { paddingValues ->
                if (isLoading) {
                    SkeletonContent(modifier = Modifier.padding(paddingValues))
                } else if (products.isNotEmpty()) {

                    Content(
                        modifier = Modifier.padding(paddingValues),
                        products = products,
                        carouselProducts = carouselProducts,
                        navController = navController
                    )
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error al cargar los datos",
                            color = Color.Black,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Button(
                            onClick = {
                                productViewModel.getProducts()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black,
                                contentColor = Color.White
                            ),
                            modifier = Modifier.padding(top = 16.dp),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(text = "Reintentar", color = Color.White)
                        }
                    }

                }
            })
    }
}


@Composable
fun SkeletonContent(modifier: Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(10) {
            Box(
                modifier = Modifier
                    .height(210.dp)
                    .width(155.dp)

            ) {

                OutlinedCard(
                    modifier = Modifier.fillMaxSize(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color.Black),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .background(Color.White)
                            .shimmerEffect()
                    )

                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(drawerState: DrawerState, scope: CoroutineScope) {
    CenterAlignedTopAppBar(colors = TopAppBarDefaults.topAppBarColors(
        containerColor = Color.White,
        titleContentColor = Color.Black,
    ), title = {
        Text(
            text = "Tienda de Vinilos", fontWeight = FontWeight.Medium, fontSize = 25.sp
        )
    }, navigationIcon = {
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
        IconButton(onClick = {}) {
            Icon(
                Icons.Filled.Search,
                contentDescription = "Search",
                tint = Color.Black,
                modifier = Modifier.size(30.dp)
            )
        }
    })
}

@Composable
fun Content(
    modifier: Modifier,
    products: List<ProductModel>,
    carouselProducts: List<ProductModel>,
    navController: NavController
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item(
            span = { GridItemSpan(2) }
        ) {


            CustomCarousel(
                carouselProducts as ArrayList<ProductModel>,
                navController = navController
            )
        }
        item(span = { GridItemSpan(maxLineSpan) }) {

            Text(
                modifier = Modifier.padding(horizontal = 5.dp),
                text = "Recomendaciones",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = colorResource(R.color.product_name)
            )
        }
        items(products.size) { index ->
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    Modifier.align(Alignment.Center)
                ) {
                    OutlinedCard(
                        onClick = {
                            try {
                                navController.navigate("productDetail/${products[index].id_vinyl}")
                            } catch (e: Exception) {
                                Log.e("Error", e.message.toString())
                            }
                        },
                        modifier = Modifier
                            .height(210.dp)
                            .width(155.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White,
                        ),
                        border = BorderStroke(1.dp, Color.Black),
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AsyncImage(
                                modifier = Modifier.size(155.dp),
                                model = products[index].img_url,
                                contentDescription = "Descripción de la imagen",
                                contentScale = ContentScale.Crop,
                            )
                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .fillMaxWidth(),
                                text = products[index].name,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = colorResource(R.color.product_name)
                            )
                            Text(
                                text = "$${products[index].price}",
                                color = colorResource(R.color.product_price),
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

