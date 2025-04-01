package com.lunatcoms.firebasepractice.login.ui

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.State
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
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.lunatcoms.firebasepractice.R

import com.google.android.gms.auth.api.signin.GoogleSignIn


@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    navigateToHome: () -> Unit,
    navigateToSignup: () -> Unit
) {

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF6EC6A9),
            Color(0xFF3A627E)
        )
    )




    Box(
        Modifier
            .background(gradient)
            .fillMaxSize()
            .padding(24.dp)
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


    val googleSignInResult by viewModel.googleSignInResult.observeAsState()

    LaunchedEffect(googleSignInResult) {
        if (googleSignInResult == true) {
            navigateToHome() // Navegar a la pantalla principal
            viewModel.resetNavigation() // Resetea el estado para evitar navegaciones repetidas
        }
    }


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account?.idToken
            if (idToken != null) {
                viewModel.signInWithGoogle(idToken)
            } else {
                Log.e("GoogleSignIn", "ID Token is null")
            }
        } catch (e: ApiException) {
            Log.e("GoogleSignIn", "Error: ${e.message}")
        }
    }

    fun signInWithGoogle() {
        val googleSignInClient = GoogleSignIn.getClient(
            context,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        )
        launcher.launch(googleSignInClient.signInIntent)
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
        GoogleSignInButton { signInWithGoogle() }
        Spacer(modifier = Modifier.padding(12.dp))
        SignupButton(Modifier.align(Alignment.CenterHorizontally)) { viewModel.onSignupSelected() }

    }
}

@Composable
fun GoogleSignInButton(onGoogleSignIn: () -> Unit) {
    Button(
        onClick = { onGoogleSignIn() },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.blue_button),
            contentColor = Color.White //
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_google),
            contentDescription = "Google Logo",
            tint = Color.Unspecified // Mantiene los colores originales del ícono
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Iniciar sesión con Google",
            color = Color.White, // Texto blanco
            fontSize = 18.sp
        )
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
        fontSize = 16.sp,
        color = Color.White
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
            containerColor = colorResource(id = R.color.blue_button),
            disabledContainerColor = colorResource(id = R.color.blue),
            contentColor = Color.White,
            disabledContentColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        enabled = authState.value != AuthState.Loading
    ) {
        Text(
            text = "Iniciar sesión",
            fontSize = 18.sp
        )
    }
}

@Composable
fun ForgotPassWord(modifier: Modifier) {
    Text(
        modifier = modifier.clickable { },
        text = "¿Olvidaste la contraseña?",
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White
    )
}

@Composable
fun PasswordField(password: String, onTextFieldChanged: (String) -> Unit) {
    // Cambia `var isPasswordVisible by remember` por el uso correcto de `remember` con `mutableStateOf`
    val isPasswordVisible = remember { mutableStateOf(false) }

    TextField(
        value = password,
        onValueChange = { onTextFieldChanged(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = "Contraseña",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_password),
                contentDescription = "Password Icon",
                tint = Color(0xFF636262) // Color del ícono
            )
        },
        trailingIcon = {
            IconButton(onClick = { isPasswordVisible.value = !isPasswordVisible.value }) {
                Icon(
                    painter = painterResource(
                        id = if (isPasswordVisible.value) R.drawable.ic_visible_on else R.drawable.ic_visible_off
                    ),
                    contentDescription = if (isPasswordVisible.value) "Ocultar contraseña" else "Mostrar contraseña",
                    tint = Color(0xFF636262) // Color del ícono del ojo
                )
            }
        },
        shape = RoundedCornerShape(16.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = if (isPasswordVisible.value) KeyboardType.Text else KeyboardType.Password
        ),
        visualTransformation = if (isPasswordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
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
fun EmailField(email: String, onTextFieldChanged: (String) -> Unit) {
    TextField(
        value = email,
        onValueChange = { onTextFieldChanged(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = "Correo Electrónico",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_mail), // Reemplaza con tu ícono
                contentDescription = "Email Icon",
                tint = Color(0xFF636262) // Color del ícono
            )
        },
        shape = RoundedCornerShape(16.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color(0xFF636262), // Color del texto cuando está enfocado
            unfocusedTextColor = Color(0xFF636262), // Color del texto cuando no está enfocado
            focusedContainerColor = Color(0x80FFFFFF), // Blanco con 75% de transparencia
            unfocusedContainerColor = Color(0x80FFFFFF), // Blanco con 75% de transparencia
            focusedIndicatorColor = Color.Transparent, // Sin subrayado
            unfocusedIndicatorColor = Color.Transparent // Sin subrayado
        )
    )
}

@Composable
fun HeaderImage(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.ic_login),
        contentDescription = "Header",
        modifier = modifier.size(150.dp)
    )
}


