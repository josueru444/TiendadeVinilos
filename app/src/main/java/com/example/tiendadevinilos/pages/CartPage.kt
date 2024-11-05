package com.example.tiendadevinilos.pages

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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
import com.example.tiendadevinilos.model.CartItemResponse
import com.example.tiendadevinilos.viewmodel.CartViewModel
import com.example.tiendadevinilos.viewmodel.UserViewModel
import java.math.BigDecimal
import java.math.RoundingMode


@Composable
fun CartPage(navController: NavController, userViewModel: UserViewModel) {
    val user = userViewModel.userData.value

    val cartViewModel: CartViewModel = viewModel()
    val isLoadingState = cartViewModel.isLoading.observeAsState(false)
    val errorState = cartViewModel.errorMessage.observeAsState()
    val cartItemsState = cartViewModel.cartItems.observeAsState(emptyList())


    Log.d("CartPage", "errorState: $errorState")

    val subtotal = cartItemsState.value.sumOf {
        BigDecimal(it.price.toString()).multiply(BigDecimal(it.quantity))
    }.setScale(2, RoundingMode.HALF_UP)

    LaunchedEffect(user) {
        user?.user_id?.let { userId ->
            cartViewModel.getCartItems(user_id = userId.toString())
        }
    }
    Scaffold(
        topBar = {
            CartTopBar(navController)

        },
        content = { paddingValues ->
            if (isLoadingState.value) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = Color.Black)
                    Text("Obteniendo productos...")
                }
            } else if (cartItemsState.value.isNotEmpty()) {
                CartContentLayout(
                    paddingValues,
                    navController = navController,
                    isLoadingState = isLoadingState,
                    cartItemsState = cartItemsState,
                    cartViewModel = cartViewModel
                )
            } else if (errorState.value != null) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("No se han podido obtener los productos")
                    Button(
                        onClick = {
                            navController.popBackStack()
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Black,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            "Volver al inicio",
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp
                        )
                    }
                }
            } else {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("No hay productos en el carrito")
                }
            }

        },
        bottomBar = {
            if (cartItemsState.value.isNotEmpty()) {
                BottomBar(
                    subtotal = subtotal.toString()
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CartTopBar(navController: NavController) {
    CenterAlignedTopAppBar(colors = TopAppBarDefaults.topAppBarColors(
        containerColor = Color.White,
        titleContentColor = Color.Black,
    ), title = {
        Text(
            text = "Mi carrito de compras", fontWeight = FontWeight.Medium, fontSize = 25.sp
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
private fun CartContentLayout(
    paddingValues: PaddingValues,
    navController: NavController,
    isLoadingState: androidx.compose.runtime.State<Boolean>,
    cartItemsState: androidx.compose.runtime.State<List<CartItemResponse>>,
    cartViewModel: CartViewModel

) {
    val cartItems = cartViewModel.cartItems.observeAsState(emptyList()).value


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        item {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                Icon(
                    Icons.Filled.LocalShipping,
                    tint = colorResource(R.color.product_name),
                    contentDescription = "Shipping",
                    modifier = Modifier
                        .size(30.dp)
                        .padding(end = 5.dp)
                )
                Text(
                    text = "Envío gratis a partir de $300",
                    color = colorResource(R.color.product_name),
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp
                )
            }
        }
        if (isLoadingState.value) {
            item {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(color = Color.Black)
                    Text(
                        text = "Cargando...",
                        color = Color.Black,
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp
                    )
                }
            }
        }
        items(cartItems.size) { index ->
            CartItem(
                cartItemsState.value[index],
                navController = navController,
                cartViewModel = cartViewModel
            )

        }

        item {
            Spacer(modifier = Modifier.height(86.dp))
        }
    }
}

@Composable
private fun CartItem(
    cartItemResponse: CartItemResponse,
    navController: NavController,
    cartViewModel: CartViewModel
) {
    val responseStatus = cartViewModel.responseStatus.observeAsState(false)

    LaunchedEffect(responseStatus.value) {
        if (responseStatus.value) {

            cartViewModel.resetResponseStatus()
        }
    }
    val cart_id = cartItemResponse.cart_id.toString()
    val id_vinyl = cartItemResponse.id_vinyl

    Card(
        onClick = {
            navController.navigate("productDetail/${cartItemResponse.id_vinyl}")
        },
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.cart_item_background),
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 5.dp)
            .height(80.dp),
    )
    {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = cartItemResponse.img_url,
                contentDescription = "Product Image",
                modifier = Modifier
                    .padding(vertical = 5.dp)
                    .padding(start = 10.dp, end = 10.dp)
                    .size(55.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = cartItemResponse.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = colorResource(R.color.product_name),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .padding(top = 15.dp)
                            .width(220.dp)
                    )
                    IconButton(
                        onClick = {
                            cartViewModel.deleteCartItem(cart_id = cart_id, id_vinyl = id_vinyl)
                        }
                    ) {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = "Delete",
                            tint = colorResource(R.color.product_name),
                            modifier = Modifier
                                .size(20.dp)
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "$${cartItemResponse.price}",
                        color = colorResource(R.color.product_price),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,

                        )
                    CustomCounter(
                        maxCount = cartItemResponse.stock,

                        currentValue = cartItemResponse.quantity,
                        id_vinyl = cartItemResponse.id_vinyl,
                        cart_id = cartItemResponse.cart_id.toString(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp)
                            .scale(scaleX = 0.7F, scaleY = 1.1F)
                            .height(31.dp),
                    )

                }
            }

        }
    }
}


@Composable
fun CustomCounter(
    maxCount: Int,
    currentValue: Int,
    cart_id: String,
    id_vinyl: String,
    modifier: Modifier = Modifier,
) {

    val cartViewModel: CartViewModel = viewModel()
    val isLoadingState = cartViewModel.isLoading.observeAsState(false)
    val errorState = cartViewModel.errorMessage.observeAsState()
    val responseStatus = cartViewModel.responseStatus.observeAsState(false)

    val count = remember { mutableStateOf(currentValue) }

    LaunchedEffect(responseStatus.value) {
        if (responseStatus.value) {

            cartViewModel.resetResponseStatus()
        }
    }
    LaunchedEffect(errorState.value) {
        errorState.value?.let { error ->
            count.value = currentValue
        }
    }
    LaunchedEffect(currentValue) {
        count.value = currentValue
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Row(
            modifier = Modifier
                .height(41.dp)
                .border(1.dp, Color.Black, RoundedCornerShape(10.dp)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorResource(R.color.background_descrease)
                ),
                onClick = {
                    if (count.value > 1 && !isLoadingState.value) {
                        val newValue = count.value - 1
                        cartViewModel.modifyQuantity(
                            cart_id = cart_id,
                            id_vinyl = id_vinyl,
                            quantity = newValue
                        )
                    }
                },
                enabled = !isLoadingState.value,
                shape = RoundedCornerShape(
                    topStart = 10.dp,
                    bottomStart = 10.dp,
                    topEnd = 0.dp,
                    bottomEnd = 0.dp
                ),
                modifier = Modifier
                    .height(44.dp)
                    .width(41.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(
                    Icons.Filled.Remove,
                    contentDescription = "Decrease",
                    modifier = Modifier.size(15.dp),
                    tint = Color.White
                )
            }

            Text(
                text = count.value.toString(),
                fontSize = 15.sp,
                color = colorResource(R.color.product_price),
                modifier = Modifier.width(60.dp),
                textAlign = TextAlign.Center
            )

            Button(
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Black,
                ),
                onClick = {
                    if (count.value < maxCount && !isLoadingState.value) {
                        val newValue = count.value + 1
                        cartViewModel.modifyQuantity(
                            cart_id = cart_id,
                            id_vinyl = id_vinyl,
                            quantity = newValue
                        )
                    }
                },
                enabled = !isLoadingState.value,
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    bottomStart = 0.dp,
                    topEnd = 10.dp,
                    bottomEnd = 10.dp
                ),
                modifier = Modifier
                    .height(44.dp)
                    .width(41.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Increase",
                    tint = Color.White,
                    modifier = Modifier.size(15.dp)
                )
            }
        }
    }
}


@Composable
private fun BottomBar(
    subtotal: String
) {
    BottomAppBar(
        backgroundColor = Color.Transparent,
        elevation = 0.dp,
        modifier = Modifier.height(80.dp),

        ) {
        ElevatedCard(
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .padding(bottom = 15.dp),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(R.color.elevated_card)
            ),
            elevation = CardDefaults.elevatedCardElevation(10.dp, 0.dp, 0.dp, 10.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)

            ) {
                Row {
                    Text(
                        text = "Subtotal: ",
                        color = Color.Black,
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp
                    )
                    Text(
                        text = "$${subtotal.toString()}",
                        color = colorResource(R.color.product_price),
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp
                    )
                }
                Button(
                    elevation = ButtonDefaults.elevation(0.dp),
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Black,

                        )
                ) {
                    Text(
                        "Proceder con el pago", color = Color.White,
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}