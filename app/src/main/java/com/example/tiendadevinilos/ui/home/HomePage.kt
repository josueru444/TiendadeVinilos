package com.example.tiendadevinilos.ui.home

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.tiendadevinilos.R
import com.example.tiendadevinilos.Routes
import com.example.tiendadevinilos.model.ProductModel
import com.example.tiendadevinilos.ui.components.CustomCarousel
import com.example.tiendadevinilos.ui.components.SkeletonContent
import com.example.tiendadevinilos.ui.genreselection.GenreViewModel
import com.example.tiendadevinilos.viewmodel.ProductsViewModel


@Composable
fun HomePage(
    navController: NavController,
    productViewModel: ProductsViewModel = viewModel(),

    genreViewModel: GenreViewModel = viewModel(),
) {

    val products = productViewModel.products.observeAsState(emptyList()).value
    val genreProducts = genreViewModel.userGenreList.observeAsState(emptyList()).value

    val carouselProducts = productViewModel.carouselProducts.observeAsState(emptyList()).value
    val isLoading = productViewModel.isLoading.observeAsState(true).value

    val context = LocalContext.current



    if (isLoading) {
        SkeletonContent()
    } else if (products.isNotEmpty()) {
        Content(
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
}

@Composable
fun Content(
    products: List<ProductModel>,
    carouselProducts: List<ProductModel>,
    navController: NavController
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
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
        item(span = { GridItemSpan(maxLineSpan) }) {
        }
    }

}

