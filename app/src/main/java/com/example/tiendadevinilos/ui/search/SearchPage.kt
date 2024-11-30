package com.example.tiendadevinilos.ui.search

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.tiendadevinilos.R
import com.example.tiendadevinilos.model.ProductModel
import com.example.tiendadevinilos.ui.components.LoadingScreen
import com.example.tiendadevinilos.viewmodel.ProductsViewModel

@Composable
fun SearchPage(
    search: String,
    clicked: Boolean,
    navController: NavController,
    productsViewModel: ProductsViewModel = viewModel()
) {
    val isLoading by productsViewModel.isLoading.observeAsState(true)
    val products by productsViewModel.products.observeAsState(emptyList())
    val errorMessage by productsViewModel.error.observeAsState()

    Scaffold(
        topBar = {
        }
    )
    { innerPadding ->
        when {
            isLoading -> LoadingScreen(isLoading = isLoading)
            errorMessage?.isNotBlank() == true -> Text(errorMessage ?: "")
            products.isNotEmpty() -> SearchContent(
                modifier = Modifier.padding(innerPadding),
                clicked = clicked,
                products = products,
                search = search,
                navController = navController
            )

        }
    }
}

@Composable
fun SearchContent(
    modifier: Modifier,
    clicked: Boolean,
    products: List<ProductModel>,
    search: String,
    navController: NavController
) {
    Log.d("product", "$clicked")
    val searchQuery = search

    val results = products.filter { it.name.contains(searchQuery, ignoreCase = true) }.take(20)
    Log.d("results",results.toString())
    LazyColumn(modifier = modifier) {
        if (clicked) {
            items(results.size) { product ->
                OutlinedCard(
                    colors = CardDefaults.cardColors(
                        containerColor = colorResource(R.color.cart_item_background),
                        contentColor = colorResource(R.color.product_name)
                    ),
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color.Black),
                    onClick = {
                        navController.navigate("productDetail/${results[product].id_vinyl}")
                    }
                ) {
                    Column(
                    ) {
                        Row(
                        ) {
                            AsyncImage(
                                model = results[product].img_url,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(50.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )
                            Column(
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Text(
                                    text = results[product].name,
                                    color = colorResource(R.color.product_name),
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 18.sp,
                                    maxLines = 1
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                )
                                {
                                    Text(text = results[product].price.toString())
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                Color.Gray,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                    ) {
                                        Text(
                                            text = "  ${results[product].genre_name.toString()}  ",
                                            color = Color.White,
                                            fontSize = 12.sp
                                        )
                                    }
                                }

                            }
                        }

                    }
                }
            }
        }
        if (clicked && results.isNullOrEmpty()) {
            item {
               Column(
                   modifier = Modifier.fillMaxSize(),
                   horizontalAlignment = Alignment.CenterHorizontally
               ){
                   Text(
                       "No encontró: $search",
                       color = Color.Black,
                       fontSize = 18.sp,
                       modifier = Modifier.fillMaxWidth(),
                       textAlign = TextAlign.Center
                   )
               }
            }
        }

    }
}
