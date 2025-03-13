package com.lunatcoms.firebasepractice.login.ui

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
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    navigateToHome: () -> Unit,
    navigateToSignup: () -> Unit
) {

    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Login(Modifier.align(Alignment.Center), viewModel, navigateToHome, navigateToSignup)
    }

}

@Composable
fun Login(
    modifier: Modifier,
    viewModel: AuthViewModel,
    navigateToHome: () -> Unit,
    navigateToSignup: () -> Unit
) {

    val email: String by viewModel.email.observeAsState(initial = "")
    val password: String by viewModel.password.observeAsState(initial = "")
    val loginEnable: Boolean by viewModel.loginEnable.observeAsState(initial = false)

    val navigateToSignupValue: Boolean by viewModel.navigateToSignup.observeAsState(initial = false)

    if (navigateToSignupValue) {
        LaunchedEffect(Unit) {
            navigateToSignup()
            viewModel.resetNavigation()
        }
    }

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
        ForgotPassWord(Modifier.align(Alignment.End))
        Spacer(modifier = Modifier.padding(16.dp))
        LoginButton(authState) { viewModel.login(email, password) }
        Spacer(modifier = Modifier.padding(12.dp))
        SignupButton(Modifier.align(Alignment.CenterHorizontally)) { viewModel.onSignupSelected() }

    }

}

@Composable
fun SignupButton(modifier: Modifier, onSignupSelected: () -> Unit) {

    Text(
        modifier = modifier.clickable { onSignupSelected() },
        text = buildAnnotatedString {
            append("¿No tienes cuenta? ")
            withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                append("Regístrate")
            }
        },
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black
    )
}

@Composable
fun LoginButton(authState: State<AuthState?>, onLoginSelected: () -> Unit) {
    Button(
        onClick = { onLoginSelected() },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF3F51B5),
            disabledContainerColor = Color(0xFF9FA8DA),
            contentColor = Color.White,
            disabledContentColor = Color.White
        ), enabled = authState.value != AuthState.Loading ///REVISAR VALIDACIONES
    ) {
        Text(text = "Iniciar sesión")
    }
}

@Composable
fun ForgotPassWord(modifier: Modifier) {
    Text(
        modifier = modifier.clickable { },
        text = "¿Olvidaste la contraseña?",
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black
    )
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
        placeholder = { Text(text = "Email") },
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
        painter = painterResource(id = R.drawable.ic_user),
        contentDescription = "Header",
        modifier = modifier.size(220.dp)
    )
}
