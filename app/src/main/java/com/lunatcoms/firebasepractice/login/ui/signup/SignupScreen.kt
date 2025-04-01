package com.lunatcoms.firebasepractice.login.ui.signup

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lunatcoms.firebasepractice.R
import com.lunatcoms.firebasepractice.login.ui.AuthState
import com.lunatcoms.firebasepractice.login.ui.AuthViewModel

@Composable
fun SignupScreen(viewModel: AuthViewModel, navigateBack: () -> Unit, navigateToHome: () -> Unit) {
    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF6EC6A9),
            Color(0xFF3A627E)
        ) // Gradiente del fondo
    )

    Box(
        Modifier
            .background(gradient) // Aplicar el gradiente
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Signup(
            Modifier.align(Alignment.Center),
            viewModel,
            navigateBack,
            navigateToHome
        )
    }
}

@Composable
fun Signup(
    modifier: Modifier,
    viewModel: AuthViewModel,
    navigateBack: () -> Unit,
    navigateToHome: () -> Unit
) {
    val email: String by viewModel.email.observeAsState(initial = "")
    val password: String by viewModel.password.observeAsState(initial = "")
    val loginEnable: Boolean by viewModel.loginEnable.observeAsState(initial = false)

    val authState = viewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated -> navigateToHome()
            is AuthState.Error -> Toast.makeText(
                context,
                (authState.value as AuthState.Error).message, Toast.LENGTH_LONG
            ).show()

            else -> Unit
        }
    }

    Column(modifier = modifier) {
        HeaderImage(Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.padding(16.dp))
        EmailField(email) { viewModel.onLoginChanged(it, password) }
        Spacer(modifier = Modifier.padding(4.dp))
        PasswordField(password) { viewModel.onLoginChanged(email, it) }
        Spacer(modifier = Modifier.padding(8.dp))
        Spacer(modifier = Modifier.padding(16.dp))
        CreatedButton(loginEnable) { viewModel.signup(email, password) }
        Spacer(modifier = Modifier.padding(12.dp))
        BackButton(Modifier.align(Alignment.CenterHorizontally), navigateBack)
    }
}

@Composable
fun BackButton(modifier: Modifier, navigateBack: () -> Unit) {
    Text(
        modifier = modifier.clickable { navigateBack() },
        text = buildAnnotatedString {
            append("¿Ya te registraste? ")
            withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                append("Inicia sesión")
            }
        },
        fontSize = 16.sp,
        color = Color.White // Ajuste de color a blanco para contrastar con el fondo oscuro
    )
}

@Composable
fun CreatedButton(loginEnable: Boolean, onSignupSelected: () -> Unit) {
    Button(
        onClick = { onSignupSelected() },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.blue_button),
            disabledContainerColor = colorResource(id = R.color.blue_button_light),
            contentColor = Color.White,
            disabledContentColor = Color.White
        ), enabled = loginEnable
    ) {
        Text(text = "Crear cuenta")
    }
}

@Composable
fun PasswordField(password: String, onTextFieldChanged: (String) -> Unit) {
    // Estado para alternar la visibilidad de la contraseña
    val isPasswordVisible = remember { mutableStateOf(false) }

    TextField(
        value = password,
        onValueChange = { onTextFieldChanged(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = "Contraseña",
                color = Color.White, // Cambiado a blanco para contraste
                fontWeight = FontWeight.Bold // Estilo bold
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_password), // Icono de contraseña
                contentDescription = "Password Icon",
                tint = Color(0xFF636262) // Color del ícono
            )
        },
        trailingIcon = {
            IconButton(
                onClick = {
                    isPasswordVisible.value = !isPasswordVisible.value
                } // Alternar visibilidad
            ) {
                Icon(
                    painter = painterResource(
                        id = if (isPasswordVisible.value) R.drawable.ic_visible_on else R.drawable.ic_visible_off
                    ), // Cambia entre íconos abiertos/cerrados
                    contentDescription = if (isPasswordVisible.value) "Ocultar contraseña" else "Mostrar contraseña",
                    tint = Color(0xFF636262) // Color del ícono del ojo
                )
            }
        },
        shape = RoundedCornerShape(16.dp), // Esquinas redondeadas
        keyboardOptions = KeyboardOptions(
            keyboardType = if (isPasswordVisible.value) KeyboardType.Text else KeyboardType.Password
        ), // Cambiar el teclado entre texto y contraseña
        visualTransformation = if (isPasswordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color(0xFF636262),
            unfocusedTextColor = Color(0xFF636262),
            focusedContainerColor = Color(0x80FFFFFF), // Fondo blanco con transparencia
            unfocusedContainerColor = Color(0x80FFFFFF), // Fondo blanco con transparencia
            focusedIndicatorColor = Color.Transparent, // Sin línea inferior
            unfocusedIndicatorColor = Color.Transparent // Sin línea inferior
        )
    )
}


@Composable
fun EmailField(email: String, onTextFieldChanged: (String) -> Unit) {
    TextField(
        value = email,
        onValueChange = { onTextFieldChanged(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = "Correo electrónico",
                color = Color.White, // Cambiado a blanco
                fontWeight = FontWeight.Bold // Estilo bold
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_mail),
                contentDescription = "Email Icon",
                tint = Color(0xFF636262)
            )
        },
        shape = RoundedCornerShape(16.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color(0xFF636262),
            unfocusedTextColor = Color(0xFF636262),
            focusedContainerColor = Color(0x80FFFFFF),
            unfocusedContainerColor = Color(0x80FFFFFF),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun HeaderImage(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.ic_login),
        contentDescription = "Header",
        modifier = modifier.size(150.dp),
    )
}
