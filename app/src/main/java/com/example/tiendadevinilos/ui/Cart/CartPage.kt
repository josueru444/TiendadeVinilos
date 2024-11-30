package com.example.tiendadevinilos.ui.Cart

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.tiendadevinilos.R
import com.example.tiendadevinilos.Routes
import com.example.tiendadevinilos.model.AddressModel
import com.example.tiendadevinilos.model.CartItemResponse
import com.example.tiendadevinilos.model.OrderItemModel
import com.example.tiendadevinilos.model.OrderModel
import com.example.tiendadevinilos.ui.components.LoadingScreen
import com.example.tiendadevinilos.ui.orders.OrdersViewModel
import com.example.tiendadevinilos.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartPage(navController: NavController, userViewModel: UserViewModel) {
    val user = userViewModel.userData.value
    val cartViewModel: CartViewModel = viewModel()

    // Estados observados
    val isLoading by cartViewModel.isLoading.observeAsState(false)
    val errorMessage by cartViewModel.errorMessage.observeAsState()
    val cartItems by cartViewModel.cartItems.observeAsState(emptyList())
    val addresses by cartViewModel.addresses.observeAsState()


    // Subtotal calculado
    val subtotal = remember(cartItems) {
        cartItems.sumOf {
            BigDecimal(it.price.toString()) * BigDecimal(it.quantity)
        }.setScale(2, RoundingMode.HALF_UP)
    }

    // Estado para el modal
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(user) {
        user?.user_id?.let { userId ->
            cartViewModel.getCartItems(userId.toString())
        }
    }

    Scaffold(
        bottomBar = {
            BottomBar(
                subtotal = subtotal.toString(),
                onProceedToPayment = { showBottomSheet = true }
            )
        }
    ) {
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState
            ) {
                ModalContent(
                    cartItems = cartItems,
                    addresses = addresses,
                    onClose = {
                        scope.launch {
                            sheetState.hide()
                            showBottomSheet = false
                        }
                    },
                    navController = navController,
                    subtotal = subtotal.toString(),
                    user_id = user?.user_id ?: ""
                )
            }
        }
        val cartItemsState = cartViewModel.cartItems.observeAsState(emptyList())

        when {
            isLoading -> LoadingScreen(isLoading = isLoading)
            errorMessage?.isNotBlank() == true -> EmptyCartScreen()


            cartItems.isEmpty() -> EmptyCartScreen()
            else -> CartContentLayout(
                user_id = user?.user_id ?: "",
                cartItems = cartItems,
                navController = navController,
                cartItemsState = cartItemsState,
            )
        }
    }
}

@Composable
fun ModalContent(
    user_id: String,
    cartItems: List<CartItemResponse>,
    addresses: List<AddressModel>?,
    onClose: () -> Unit,
    navController: NavController,
    subtotal: String,
    orderViewModel: OrdersViewModel = viewModel()

) {
    val context = LocalContext.current
    val response by orderViewModel.responseStatus.observeAsState(false)
    val isLoading by orderViewModel.isLoading.observeAsState(true)

    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Finalizar pedido",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            items(cartItems.size) { cartItem ->
                AsyncImage(
                    modifier = Modifier
                        .size(65.dp)
                        .padding(5.dp)
                        .clip(RoundedCornerShape(5.dp)),
                    model = cartItems[cartItem].img_url,
                    contentDescription = "Product Image"
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Dirección de envío:",
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp,
            color = colorResource(R.color.product_name),
            textAlign = TextAlign.Start
        )
        if (!addresses.isNullOrEmpty()) {
            addresses?.let {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.LocationOn, "", tint = colorResource(R.color.product_name))

                    AddressDropdown(addresses = it)

                    IconButton(
                        onClick = {
                            onClose()
                            navController.navigate(Routes.newAddressPage)

                        }
                    ) {
                        Icon(Icons.Filled.Add, "", tint = colorResource(R.color.product_name))
                    }
                }

            }
        } else {
            Text(text = "Agregar dirección",
                color = Color.Black,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    onClose()
                    navController.navigate(Routes.newAddressPage)
                })
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Total: ",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            Text(
                "$${subtotal}",
                fontSize = 21.sp,
                fontWeight = FontWeight.Medium,
                color = colorResource(R.color.product_price)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Black,
                contentColor = Color.White
            ),
            enabled = !addresses.isNullOrEmpty(),
            onClick = {
                onClose()
                orderViewModel.addNewOrder(
                    OrderModel(
                        user_id = user_id,
                        total = subtotal.toDouble(),
                        items = cartItems.map {
                            OrderItemModel(
                                id_vinyl = it.id_vinyl,
                                quantity = it.quantity,
                                price = it.price.toDouble(),
                            )
                        }
                    )
                )
                navController.popBackStack(navController.graph.startDestinationId, false)
                navController.navigate(Routes.orderPage)
            }
        ) {
            Text(
                "Confirmar pedido",
                color = Color.White
            )
        }

    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddressDropdown(addresses: List<AddressModel>) {
    var isExpanded by remember { mutableStateOf(false) }
    var selectedText by remember {
        mutableStateOf(
            "${addresses.firstOrNull()?.city} " +
                    "\n${addresses.firstOrNull()?.street ?: ""}"
        )
    }
    var selectedId by remember { mutableStateOf(addresses.firstOrNull()?.id) }
    Log.d("idaaa", "$selectedId")
    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = !isExpanded }
    ) {
        TextField(
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = colorResource(R.color.text_color_input),
                unfocusedTextColor = colorResource(R.color.text_color_input_label),
            ),
            value = selectedText,
            onValueChange = { selectedText = it },
            readOnly = true,
            textStyle = TextStyle(
                fontSize = 15.sp
            ),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) }
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            addresses.forEach { address ->
                DropdownMenuItem(
                    text = {
                        Text(text = "${address.street}, " + "${address.city ?: ""}")
                    },
                    onClick = {
                        selectedText = address.street
                        isExpanded = false
                        selectedId = address.id.toString()
                    }
                )
            }
        }
    }
}


@Composable
fun ErrorScreen(errorMessage: String?, onRetry: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(errorMessage ?: "Error desconocido")
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Text("Volver al inicio", color = Color.White)
        }
    }
}

@Composable
fun EmptyCartScreen() {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("No hay productos en el carrito")
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun CartContentLayout(
    user_id: String,
    cartItems: List<CartItemResponse>?,
    navController: NavController,
    cartItemsState: State<List<CartItemResponse>>,
    cartViewModel: CartViewModel = viewModel(),

    ) {


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
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

        cartItems?.let {
            items(it.size) { index ->
                CartItem(
                    cartItemsState.value[index],
                    navController = navController,
                    cartViewModel = cartViewModel
                )

            }
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
    onProceedToPayment: () -> Unit,
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
                    enabled = if (subtotal.toFloat() > 0) true else false,
                    elevation = ButtonDefaults.elevation(0.dp),
                    onClick = {
                        onProceedToPayment()

                    },
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