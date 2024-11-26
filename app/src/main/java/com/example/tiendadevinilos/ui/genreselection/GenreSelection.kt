package com.example.tiendadevinilos.ui.genreselection

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.tiendadevinilos.R
import com.example.tiendadevinilos.Routes
import com.example.tiendadevinilos.model.GenreModel

@Composable
fun GenreSelectionPage(
    viewModel: GenreViewModel = viewModel(),
    user_id: String,
    navController: NavController
) {
    val genres = viewModel.genreList.observeAsState(emptyList()).value
    val context = LocalContext.current
    val selectedGenres = remember { mutableStateListOf<GenreModel>() }
    val isLoading = viewModel.isLoading.observeAsState(true).value
    LaunchedEffect(Unit) {
        viewModel.getGenreList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        if (genres.isNotEmpty() && !isLoading) {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                val rows = genres.chunked(3)

                item {
                    Text(
                        text = "Selecciona tus géneros favoritos",
                        modifier = Modifier.padding(15.dp),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.product_name)
                    )
                }

                items(rows.size) { rowIndex ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        rows[rowIndex].forEachIndexed { _, genre ->
                            val isSelected = selectedGenres.contains(genre)

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(RoundedCornerShape(100.dp))
                                        .background(Color(0xFF6200EE))
                                        .clickable {
                                            if (isSelected) {
                                                selectedGenres.remove(genre)
                                            } else {
                                                selectedGenres.add(genre)
                                            }
                                        }
                                ) {
                                    AsyncImage(
                                        model = genre.img,
                                        contentDescription = genre.genre_name,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .then(
                                                if (isSelected) {
                                                    Modifier.graphicsLayer {
                                                        alpha = 0.7f
                                                    }
                                                } else {
                                                    Modifier
                                                }
                                            )
                                    )
                                }
                                Text(
                                    text = genre.genre_name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = if (isSelected) Color(0xFF6200EE) else Color.Black,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        }
                    }
                }
            }

            Button(
                onClick = {
                    viewModel.addUserGenres(
                        userId = user_id,
                        genres = selectedGenres.toList(),
                        onComplete = {
                            selectedGenres.clear()
                            navController.navigate(Routes.homePage)
                        },
                        onError = { error ->
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = selectedGenres.isNotEmpty() && !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                } else {
                    Text("Continuar (${selectedGenres.size}) →")
                }
            }
        }
    }
}