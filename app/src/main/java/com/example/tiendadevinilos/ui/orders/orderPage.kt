package com.example.tiendadevinilos.ui.orders

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.tiendadevinilos.R
import com.example.tiendadevinilos.model.OrderResponse
import com.example.tiendadevinilos.ui.components.LoadingScreen

@Composable
fun OrderPage(
    orderViewModel: OrdersViewModel = viewModel(),
    user_id: String
) {
    val isLoading by orderViewModel.isLoading.observeAsState(true)
    val response by orderViewModel.responseStatus.observeAsState(false)
    val orders by orderViewModel.orders.observeAsState(emptyList())


    LaunchedEffect(user_id) {
        orderViewModel.getOrders(user_id)
    }


    Log.d("orders", "$isLoading")
    when {
        isLoading -> LoadingScreen(isLoading = isLoading)
        !isLoading && !orders.isEmpty() -> OrderContentLayout(orders = orders)
        !isLoading && orders.isEmpty() -> OrderEmptyLayout()
    }
}
@Composable
fun OrderEmptyLayout() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "No tienes historial de compras", color = Color.Black)
    }
}

@Composable
fun OrderContentLayout(orders: List<OrderResponse>) {

    if (orders.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .background(Color.White)

        ) {
            items(orders.size) { index ->
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


                    ) {
                    Column(
                    ) {

                        Row(
                        ) {
                            AsyncImage(
                                model = orders[index].img_url,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )
                            Column(
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Text(
                                    text = orders[index].name,
                                    color = colorResource(R.color.product_name),
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 18.sp,
                                    maxLines = 1
                                )
                                Text(text = orders[index].price.toString())
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                Color.Gray,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                    ) {
                                        Text(
                                            text =
                                            when (orders[index].status) {
                                                "pending" -> "   Pendiente   "
                                                "paid" -> "   Pagado   "
                                                "to_ship" -> "   Por enviar   "
                                                "shipped" -> "   Enviado   "
                                                "delivered" -> "   Entregado   "
                                                "cancelled" -> "   Cancelado   "
                                                else -> {
                                                    "Pendiente"
                                                }
                                            },
                                            color = Color.White,
                                            fontSize = 12.sp
                                        )
                                    }
                                    Text(
                                        text = "Orden: ${orders[index].id.toString()}",
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
