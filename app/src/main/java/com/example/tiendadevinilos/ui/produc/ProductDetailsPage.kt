package com.example.tiendadevinilos.ui.produc

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.tiendadevinilos.R
import com.example.tiendadevinilos.Routes
import com.example.tiendadevinilos.biometric.BiometricAuthenticator
import com.example.tiendadevinilos.model.ProductModel
import com.example.tiendadevinilos.model.UserModel
import com.example.tiendadevinilos.ui.Cart.CartViewModel
import com.example.tiendadevinilos.ui.components.CounterComponent
import com.example.tiendadevinilos.ui.components.ExpandableText
import com.example.tiendadevinilos.viewmodel.CounterViewModel
import com.example.tiendadevinilos.viewmodel.UserViewModel
import com.razzaghi.compose_loading_dots.LoadingWavy
import com.razzaghi.compose_loading_dots.core.rememberDotsLoadingController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetails(
    navController: NavController,
    productId: String,
    userViewModel: UserViewModel,
    biometricAuthenticator: BiometricAuthenticator
) {

    val viewModel: ProductDetailsViewModel = viewModel()
    val isLoading = viewModel.isLoading.observeAsState(true).value
    val product = viewModel.product.observeAsState()
    val userData = userViewModel.userData.observeAsState(
        initial = UserModel(
            user_id = null,
            email = null,
            fullName = null,
            picture = null,
            token = null
        )
    )

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(productId) {
        viewModel.getProductById(productId)
    }

    val rememberDotsLoadingWavyController = rememberDotsLoadingController()

    if (isLoading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoadingWavy(
                controller = rememberDotsLoadingWavyController,
                dotsColor = Color.Black,
                modifier = Modifier
            )
            Text(
                text = "Cargando...",
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }
    } else if (product.value != null && !isLoading) {
        ContentProduct(
            modifier = Modifier,
            id = productId,
            product = product.value,
            user_id = userData.value.user_id ?: null,
            navController = navController,
            snackbarHostState = snackbarHostState,
            scope = scope,
            biometricAuthenticator = biometricAuthenticator
        )
    } else {
        Text(
            text = "Error al cargar los datos",
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@SuppressLint("CoroutineCreationDuringComposition", "UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContentProduct(
    modifier: Modifier,
    id: String,
    product: ProductModel?,
    user_id: String?,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    navController: NavController,
    biometricAuthenticator: BiometricAuthenticator
) {
    //fingerprint
    val activity = LocalContext.current as FragmentActivity
    var message: String = remember { mutableStateOf("").toString() }
    val scaffoldState = rememberBottomSheetScaffoldState()

    val cartViewModel: CartViewModel = viewModel()
    val counterViewModel: CounterViewModel = viewModel()
    var counter = counterViewModel.counter

    val context = LocalContext.current

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(0.dp)
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

                CounterComponent(
                    maxCount = product?.quantity ?: 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp)
                        .height(41.dp)
                )

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
                    onClick = {
                        biometricAuthenticator.promptBiometricAuth(
                            title = "Verify your identity",
                            subTitle = "Use your fingerprint",
                            negativeButtonText = "Cancel",
                            fragmentActivity = activity,
                            onSuccess = {
                                message = "Success"
                                scope.launch {
                                    scaffoldState.bottomSheetState.expand()
                                }
                                Toast.makeText(context, "Huella Escaneada", Toast.LENGTH_SHORT)
                                    .show()
                            },
                            onError = { _, errorString ->
                                message = errorString.toString()
                            },
                            onFailed = {
                                message = "Verification error"
                            }
                        )

                    },

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

                    onClick = {
                        if (user_id != "") {
                            cartViewModel.addToCart(
                                user_id = user_id.toString(),
                                id_vinyl = id,
                                quantity = counter.value
                            )
                            scope.launch {
                                val result = snackbarHostState.showSnackbar(
                                    message = "Agregado al carrito",
                                    actionLabel = "Ver carrito",
                                    duration = SnackbarDuration.Short,
                                )
                                when (result) {
                                    SnackbarResult.ActionPerformed -> {
                                        navController.navigate(Routes.cartPage)
                                    }

                                    else -> {

                                    }
                                }
                            }

                            counter.value = 1

                        } else {
                            navController.navigate(Routes.loginPage)
                        }
                    },

                    ) {
                    Text(
                        text = "Agregar al carrito ",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(modifier = Modifier.size(5.dp))


            }
        }
    }
}
