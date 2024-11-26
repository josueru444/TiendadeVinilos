package com.example.tiendadevinilos.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.razzaghi.compose_loading_dots.LoadingWavy
import com.razzaghi.compose_loading_dots.core.rememberDotsLoadingController

@Composable
fun LoadingScreen(isLoading: Boolean) {
    if (isLoading) {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val loadingController = rememberDotsLoadingController()
            LoadingWavy(
                controller = loadingController,
                dotsColor = Color.Black,
                modifier = Modifier
            )
            Text("Obteniendo productos...")
        }
    }
}