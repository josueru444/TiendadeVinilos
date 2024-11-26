package com.example.tiendadevinilos.ui.address

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tiendadevinilos.model.AddressModel

@Composable
fun addAddress(
    user_id: String,
    navController: NavController,
    addressViewModel: AddressViewModel = viewModel()
) {
    var street by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var zipCode by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Ingrese la dirección de envío",
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )
        OutlinedTextField(
            value = street,
            onValueChange = {
                street = it
            },
            label = { Text("Dirección") }
        )
        OutlinedTextField(
            value = city,
            onValueChange = {
                city = it
            },
            label = { Text("Ciudad") }
        )
        OutlinedTextField(
            value = zipCode,
            onValueChange = {
                zipCode = it
            },
            label = { Text("Código Postal") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = phone,
            onValueChange = {
                phone = it
            },
            label = { Text("Teléfono") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )
        val isFormValid =
            street.isNotBlank() && city.isNotBlank() && zipCode.isNotBlank() && phone.isNotBlank()

        Button(
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (isFormValid) Color.Black else Color.Gray,
                contentColor = Color.White
            ),
            onClick = {
                if (isFormValid) {
                    val address = AddressModel(
                        id = null,
                        user_id = user_id,
                        state = "",
                        street = street,
                        city = city,
                        postal_code = zipCode,
                        phone_number = phone
                    )
                    addressViewModel.addAddress(address)
                    navController.popBackStack()
                }
            },
            enabled = isFormValid
        ) {
            Text("Guardar dirección")
        }

    }
}