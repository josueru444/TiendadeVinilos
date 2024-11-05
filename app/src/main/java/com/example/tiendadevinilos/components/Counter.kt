package com.example.tiendadevinilos.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiendadevinilos.R
import com.example.tiendadevinilos.viewmodel.CounterViewModel


@Composable
fun CounterComponent(
    maxCount: Int,
    defaultValue:Int = 1,
    modifier: Modifier,
    counterViewModel: CounterViewModel = viewModel()
) {
    val count = counterViewModel.counter.value


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
                    containerColor = colorResource(R.color.background_descrease),
                ),
                onClick = { counterViewModel.decrement() },
                shape = RoundedCornerShape(
                    topStart = 10.dp,
                    bottomStart = 10.dp,
                    topEnd = 0.dp,
                    bottomEnd = 0.dp
                ), modifier = Modifier
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
                text = counterViewModel.counter.value.toString(),
                fontSize = 15.sp,
                color = colorResource(R.color.product_price),
                modifier = Modifier
                    .width(60.dp),
                textAlign = TextAlign.Center
            )
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                ),
                onClick = { counterViewModel.increment(maxCount) },
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
                    modifier = Modifier
                        .size(15.dp)
                )
            }
        }
    }

}