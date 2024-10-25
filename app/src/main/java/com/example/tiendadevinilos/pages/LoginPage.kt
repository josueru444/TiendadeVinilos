import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tiendadevinilos.R
import com.example.tiendadevinilos.Routes
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            textStyle = TextStyle(
                textAlign = TextAlign.Left,
                fontSize = 18.sp,
            )
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
            textStyle = TextStyle(
                textAlign = TextAlign.Left,
                fontSize = 18.sp
            ),
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
            modifier = Modifier
                .clickable {
                    navController.navigate(Routes.homePage)
                },
            text = "Iniciar sesión | Registrarse",
            textDecoration = TextDecoration.Underline,
            color = colorResource(R.color.text_login_btn),
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically

        ) {
            GoogleLogin()

            IconButton(
                onClick = { /*TODO*/ },
                content = {
                    Image(
                        painter = painterResource(id = R.drawable.facebook_logo),
                        contentDescription = "Google Logo",
                        modifier = Modifier
                            .size(41.dp)
                    )
                }

            )
        }
    }
}


@Composable
fun GoogleLogin() {
    val state = rememberOneTapSignInState()
    var user: GoogleUser? by remember { mutableStateOf(null) }
    val context = LocalContext.current
    OneTapSignInWithGoogle(
        state = state,
        clientId = "253523235046-akq9kgv3k1o9hklbh3bgicu9f7649bt0.apps.googleusercontent.com\n",
        rememberAccount = true,
        onTokenIdReceived = {
            user = getUserFromTokenId(tokenId = it)
            Toast.makeText(context, user?.givenName, Toast.LENGTH_SHORT).show()
        },
        onDialogDismissed = {
            Log.d("MainActivity", it)
        }
    )

    if (state.opened == false) {
        IconButton(
            onClick = { state.open() },
            content = {
                Image(
                    painter = painterResource(id = R.drawable.google_logo),
                    contentDescription = "Google Logo",
                    modifier = Modifier
                        .size(41.dp)
                )
            }

        )

    } else {
        CircularProgressIndicator(
            color = Color.Black
        )

    }
}


