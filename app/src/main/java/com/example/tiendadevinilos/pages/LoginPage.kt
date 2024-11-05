import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tiendadevinilos.R
import com.example.tiendadevinilos.Routes
import com.example.tiendadevinilos.repository.UserPreferencesRepository
import com.example.tiendadevinilos.viewmodel.AddUserViewModel
import com.example.tiendadevinilos.viewmodel.UserViewModel
import com.stevdzasan.onetap.GoogleUser
import com.stevdzasan.onetap.OneTapSignInWithGoogle
import com.stevdzasan.onetap.getUserFromTokenId
import com.stevdzasan.onetap.rememberOneTapSignInState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var visible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Iniciar sesión",
            color = Color.Black,
            fontSize = 40.sp,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(modifier = Modifier.height(80.dp))

        TextField(
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = colorResource(R.color.input_background),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = colorResource(R.color.text_color_input),
                unfocusedTextColor = colorResource(R.color.text_color_input_label),
            ),
            value = email,
            onValueChange = { email = it },
            placeholder = {
                Text(
                    text = "Usuario",
                    color = colorResource(R.color.text_color_input_label),
                )
            },
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = androidx.compose.ui.text.input.KeyboardType.Email),
            textStyle = TextStyle(fontSize = 18.sp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = colorResource(R.color.input_background),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = colorResource(R.color.text_color_input),
                unfocusedTextColor = colorResource(R.color.text_color_input_label),
            ),
            value = password,
            onValueChange = { password = it },
            placeholder = {
                Text(
                    text = "Contraseña",
                    color = colorResource(R.color.text_color_input_label),
                )
            },
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = androidx.compose.ui.text.input.KeyboardType.Password),
            visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
            textStyle = TextStyle(fontSize = 18.sp),
            trailingIcon = {
                val image = if (visible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { visible = !visible }) {
                    Icon(
                        imageVector = image,
                        contentDescription = if (visible) "Ocultar contraseña" else "Mostrar contraseña",
                        modifier = Modifier.size(24.dp),
                        tint = colorResource(R.color.text_color_input_label)
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            modifier = Modifier.clickable { navController.navigate(Routes.homePage) },
            text = "Iniciar sesión | Registrarse",
            textDecoration = TextDecoration.Underline,
            color = colorResource(R.color.text_login_btn),
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GoogleLogin(navController)


            IconButton(onClick = { /*TODO: Implementar Facebook Login*/ }) {
                Image(
                    painter = painterResource(id = R.drawable.facebook_logo),
                    contentDescription = "Facebook Logo",
                    modifier = Modifier.size(41.dp)
                )
            }
        }
    }
}

@Composable
private fun GoogleLogin(
    navController: NavController,
) {
    val context = LocalContext.current
    val userPreferencesRepository = UserPreferencesRepository(context)
    val viewModelSaveDataStore: UserViewModel =
        remember { UserViewModel(userPreferencesRepository) }

    val state = rememberOneTapSignInState()
    var user: GoogleUser? by remember { mutableStateOf(null) }

    val viewModel: AddUserViewModel = viewModel()
    val responseMessage by viewModel.responseMessage.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)

    OneTapSignInWithGoogle(
        state = state,
        clientId = "253523235046-akq9kgv3k1o9hklbh3bgicu9f7649bt0.apps.googleusercontent.com",
        rememberAccount = true,
        onTokenIdReceived = {
            user = getUserFromTokenId(tokenId = it)

            user?.let {
                val userId = (it.sub) ?: ""
                val email = it.email ?: ""
                val fullName = it.givenName ?: ""
                val picture = it.picture ?: ""

                viewModel.addGoogleUser(
                    userId,
                    email,
                    fullName,
                    picture
                )
                viewModelSaveDataStore.saveUserData(
                    userId,
                    email,
                    fullName,
                    picture
                )
            }
            navController.popBackStack()
            if (isLoading) {
                Toast.makeText(context, "Cargando...", Toast.LENGTH_SHORT).show()
            }
        },
        onDialogDismissed = {
            Log.d("ErrorLogin", it)
            Toast.makeText(context, "No se ha podido iniciar sesión", Toast.LENGTH_SHORT).show()
        }
    )
    if (state.opened) {
        CircularProgressIndicator(color = Color.Black)
    } else {
        IconButton(onClick = { state.open() }) {
            Image(
                painter = painterResource(id = R.drawable.google_logo),
                contentDescription = "Google Logo",
                modifier = Modifier.size(41.dp)
            )
        }
    }


}
