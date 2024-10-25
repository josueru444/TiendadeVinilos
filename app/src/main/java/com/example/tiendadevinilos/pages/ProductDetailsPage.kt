package com.example.tiendadevinilos.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.tiendadevinilos.R
import com.example.tiendadevinilos.components.CounterComponent
import com.example.tiendadevinilos.components.ExpandableText
import com.example.tiendadevinilos.model.ProductModel
import com.example.tiendadevinilos.viewmodel.ProductDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetails(navController: NavController, productId: String) {
    val viewModel: ProductDetailsViewModel = viewModel()

    val product = viewModel.product.observeAsState()

    LaunchedEffect(productId) {
        viewModel.getProductById(productId.toLong())
    }

    Scaffold(topBar = {
        TopBar(navController = navController)
    }, content = { paddingValues ->
        ContentProduct(
            modifier = Modifier.padding(paddingValues), id = productId, product = product.value
        )
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(navController: NavController) {
    CenterAlignedTopAppBar(colors = TopAppBarDefaults.topAppBarColors(
        containerColor = Color.White,
        titleContentColor = Color.Black,
    ), title = {
        Text(
            text = "Más detalles", fontWeight = FontWeight.Medium, fontSize = 25.sp
        )
    }, navigationIcon = {
        IconButton(onClick = {
            navController.popBackStack()
        }) {
            Icon(
                Icons.Filled.ArrowBackIosNew,
                contentDescription = "Back",
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
private fun ContentProduct(modifier: Modifier, id: String, product: ProductModel?) {

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        item {
            AsyncImage(
                model = product?.img_url,
                contentDescription = null,
                modifier = Modifier
                    .size(343.dp)
                    .clip(shape = RoundedCornerShape(15.dp))
            )
            Text(
                text = "${product?.name}",
                color = Color.Black,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 25.sp,
                modifier = Modifier
                    .padding(horizontal = 25.dp)
                    .padding(top = 10.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp, vertical = 10.dp)

            ) {
                Text(
                    text = "$${product?.price}",
                    color = colorResource(R.color.product_price),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth()

                )
            }

            ExpandableText(
                product?.description.toString() ?: "No hay Descripción",
                minimizedMaxLines = 6,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .heightIn(min = 154.dp)
            )

            CounterComponent(maxCount = product?.quantity ?: 1)

            Spacer(modifier = Modifier.size(10.dp))

            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black, contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(41.dp)
                    .padding(horizontal = 25.dp),
                shape = RoundedCornerShape(10.dp),
                onClick = {},

                ) {
                Text(text = "Comprar ahora", fontSize = 15.sp, fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.size(10.dp))

            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.background_descrease),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(41.dp)
                    .padding(horizontal = 25.dp),
                shape = RoundedCornerShape(10.dp),
                onClick = {},

                ) {
                Text(text = "Agregar al carrito ", fontSize = 15.sp, fontWeight = FontWeight.Medium)
            }
            Spacer(modifier = Modifier.size(5.dp))



        }
    }
}



