package com.example.tiendadevinilos.ui.home

import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
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
import com.example.tiendadevinilos.model.ProductModelByUser
import com.example.tiendadevinilos.ui.components.CustomCarousel
import com.example.tiendadevinilos.ui.components.SkeletonContent
import com.example.tiendadevinilos.ui.genreselection.GenreViewModel
import com.example.tiendadevinilos.viewmodel.ProductsViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


@Composable
fun HomePage(
    navController: NavController,
    productViewModel: ProductsViewModel = viewModel(),
    genreViewModel: GenreViewModel = viewModel(),
    user_id: String,
    loadingUser: Boolean

) {

    //Productos
    val products = productViewModel.products.observeAsState(emptyList()).value
    val carouselProducts = productViewModel.carouselProducts.observeAsState(emptyList()).value
    val isLoadingProducts = productViewModel.isLoading.observeAsState(true).value

    val userGenreList =
        genreViewModel.userGenreList.observeAsState(initial = emptyList()).value ?: emptyList()
    val isLoadingGenre = genreViewModel.isLoading.observeAsState(initial = true).value
    val context = LocalContext.current

    if (!user_id.equals("") && !loadingUser) {
        LaunchedEffect(user_id) {
            genreViewModel.getUserGenre(user_id)
        }
        Log.d("HomePage", "USERRRR: $user_id")
        Log.d("HomePage", "userGenreList: $userGenreList")
    } else if(user_id.isBlank() && !loadingUser) {
        genreViewModel.clearIsLoading()
    }
    Log.d("HomePage", "///////////////////////////")
    Log.d("HomePage", "isLoading: $isLoadingProducts")
    Log.d("HomePage", "isLoadingGenre: $isLoadingGenre")
    Log.d("HomePage", "userGenreList: $userGenreList")
    Log.d("HomePage", "user_id: $user_id")
    Log.d("HomePage", "LoadingUser: $loadingUser")
    Log.d("HomePage", "///////////////////////////")


    if (isLoadingProducts || isLoadingGenre || loadingUser) {
        SkeletonContent()
    }
       else if (products.isNotEmpty() && userGenreList.isNullOrEmpty() && (user_id == "" || user_id == "null" || user_id.isEmpty())) {
            Content(
                products = products,
                carouselProducts = carouselProducts,
                navController = navController
            )

        }
        else if (user_id != "" && !userGenreList.isNullOrEmpty() && !isLoadingProducts && !isLoadingGenre) {
            ContentByGenre(
                genreData = userGenreList,
                carouselProducts = carouselProducts,
                navController = navController,
                products = products
            )

        }
        else if (!user_id.isBlank() && userGenreList.isNullOrEmpty() && !isLoadingProducts && !isLoadingGenre) {
            Toast.makeText(context, "useGenreList: ${userGenreList.toString()}", Toast.LENGTH_SHORT).show()
            LaunchedEffect(Unit) {
                navController.navigate(Routes.genreSelectionPage)
            }

    } else if(!isLoadingProducts && !isLoadingGenre && products.isEmpty() ){
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
                }, colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black, contentColor = Color.White
                ), modifier = Modifier.padding(top = 16.dp), shape = RoundedCornerShape(10.dp)
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
        item(span = { GridItemSpan(2) }) {


            CustomCarousel(
                carouselProducts.toMutableList() as ArrayList<ProductModel>,
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
        item(span = { GridItemSpan(maxLineSpan) }) {}
    }

}


@Composable
fun ContentByGenre(
    genreData: List<ProductModelByUser>?,
    carouselProducts: List<ProductModel>,
    navController: NavController,
    products: List<ProductModel>
) {
    val gson = Gson()
    val json = gson.toJson(genreData)

    val listType = object : TypeToken<List<Map<String, List<ProductModelByUser>>>>() {}.type
    val genreList: List<Map<String, List<ProductModelByUser>>> = gson.fromJson(json, listType)

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item(span = { GridItemSpan(2) }) {
            CustomCarousel(
                productLists = carouselProducts.toMutableList() as ArrayList<ProductModel>,
                navController = navController
            )
        }
        genreList.forEach { genreMap ->
            genreMap.forEach { (genreName, products) ->
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Text(
                        text = genreName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = colorResource(R.color.product_name)
                    )
                }

                item(span = { GridItemSpan(maxLineSpan) }) {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        items(products) { product ->
                            Box(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Box(
                                    Modifier.align(Alignment.Center)
                                ) {
                                    OutlinedCard(
                                        onClick = {
                                            try {
                                                navController.navigate("productDetail/${product.id_vinyl}")
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
                                                model = product.img_url,
                                                contentDescription = "Descripción de la imagen",
                                                contentScale = ContentScale.Crop,
                                            )
                                            Text(
                                                modifier = Modifier
                                                    .padding(horizontal = 8.dp)
                                                    .fillMaxWidth(),
                                                text = product?.name ?: "Sin Nombre",
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.Medium,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                color = colorResource(R.color.product_name)
                                            )
                                            Text(
                                                text = "$${product.price}",
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
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = "Todos nuestros productos",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = colorResource(R.color.product_name)
                )
            }
            if (products.isNotEmpty()) {
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

    }
}
