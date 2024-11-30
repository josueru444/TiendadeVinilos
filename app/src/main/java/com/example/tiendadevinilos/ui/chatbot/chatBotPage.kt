package com.example.tiendadevinilos.ui.chatbot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiendadevinilos.R
import com.example.tiendadevinilos.model.MessageModel
import com.razzaghi.compose_loading_dots.core.rememberDotsLoadingController

@Composable
fun ChatPage(generativeViewModel: GenerativeViewModel = viewModel()) {
    val response = generativeViewModel.response.observeAsState()
    val error = generativeViewModel.error.observeAsState()
    val isLoading = generativeViewModel.isLoading.observeAsState(false)
    val context = LocalContext.current
    val rememberDotsLoadingWavyController = rememberDotsLoadingController()
    val messages = remember { mutableListOf<MessageModel>() }
    var currentMessage by remember { mutableStateOf("") }

    if (response.value.toString().isNotBlank()) {
        messages.add(MessageModel(text = response.value!!, isSentByUser = false))
        generativeViewModel.resetResponse()
    }
    if (error.value.toString().isNotBlank()) {
        messages.add(MessageModel(text = error.value!!, isSentByUser = false))
        generativeViewModel.resetError()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            reverseLayout = true
        ) {
            items(messages.size) { index ->
                val message = messages[messages.size - 1 - index]
                MessageBubble(
                    MessageModel(
                        text = message.text,
                        isSentByUser = message.isSentByUser
                    )
                )
            }

        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = 8.dp)
                .padding(10.dp)
        ) {
            OutlinedTextField(
                value = currentMessage,
                onValueChange = { currentMessage = it },
                label = { Text("Escribe un mensaje...", color = Color.Black) },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black,
                ),
                textStyle = TextStyle(
                    color = Color.Black
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                enabled = !isLoading.value,
                onClick = {
                    if (currentMessage.isNotBlank()) {
                        generativeViewModel.makePrompt(currentMessage)
                        messages.add(MessageModel(text = currentMessage, isSentByUser = true))
                        currentMessage = ""
                    }
                }
            ) {
                Icon(Icons.Filled.Send, contentDescription = "Enviar mensaje", tint = Color.Black)
            }
        }
    }
}

@Composable
fun MessageBubble(messageModel: MessageModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (messageModel.isSentByUser) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = if (messageModel.isSentByUser) Color.Black else colorResource(R.color.cart_item_background),
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Text(
                text = messageModel.text,

                modifier = Modifier.padding(8.dp),
                color = if (messageModel.isSentByUser) Color.White else Color.Black
            )
        }
    }
}