package com.lunatcoms.firebasepractice.login.ui.signup

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lunatcoms.firebasepractice.R
import com.lunatcoms.firebasepractice.login.ui.AuthState
import com.lunatcoms.firebasepractice.login.ui.AuthViewModel

@Composable
fun SignupScreen(viewModel: AuthViewModel, navigateBack: () -> Unit, navigateToHome: () -> Unit) {

    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Signup(Modifier.align(Alignment.Center), viewModel, navigateBack, navigateToHome)
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
        }, fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black
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
            containerColor = Color(0xFF3F51B5),
            disabledContainerColor = Color(0xFF9FA8DA),
            contentColor = Color.White,
            disabledContentColor = Color.White
        ), enabled = loginEnable
    ) {
        Text(text = "Crear cuenta")
    }
}

@Composable
fun PasswordField(password: String, onTextFieldChanged: (String) -> Unit) {
    TextField(
        value = password,
        onValueChange = { onTextFieldChanged(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Contraseña") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color(0xFF636262), // Color del texto cuando está enfocado
            unfocusedTextColor = Color(0xFF636262), // Color del texto cuando no está enfocado
            focusedContainerColor = Color(0xFFDEDDDD),
            unfocusedContainerColor = Color(0xFFDEDDDD),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable


fun EmailField(email: String, onTextFieldChanged: (String) -> Unit) {

    TextField(
        value = email,
        onValueChange = { onTextFieldChanged(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Correo electrónico") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color(0xFF636262), // Color del texto cuando está enfocado
            unfocusedTextColor = Color(0xFF636262), // Color del texto cuando no está enfocado
            focusedContainerColor = Color(0xFFDEDDDD),
            unfocusedContainerColor = Color(0xFFDEDDDD),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun HeaderImage(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.ic_signup),
        contentDescription = "Header",
        modifier = modifier.size(220.dp),
        colorFilter = ColorFilter.tint(Color(0xFF3F51B5))

    )
}