package com.lunatcoms.firebasepractice.user.ui

import android.content.Context
import android.widget.Toast
import com.lunatcoms.firebasepractice.R

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lunatcoms.firebasepractice.login.ui.AuthState
import com.lunatcoms.firebasepractice.login.ui.AuthViewModel


@Composable
fun HomeScreen(viewModel: AuthViewModel, navigateToLogin: () -> Unit) {

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
            .padding(16.dp)
    ) {
        Login(Modifier.align(Alignment.Center), viewModel, navigateToLogin)
    }

}

@Composable
fun Login(modifier: Modifier, viewModel: AuthViewModel, navigateToLogin: () -> Unit) {

    val authState = viewModel.authState.observeAsState()

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> navigateToLogin()
            else -> Unit
        }
    }

    val userName by viewModel.userName.observeAsState()

    Column(modifier = modifier) {
        HeaderImage(Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.padding(16.dp))
        Text(
            text = "¡Bienvenido!",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = "¡${userName ?: "Rodrigo"}!",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.padding(80.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(iconRes = R.drawable.ic_camera, backgroundColor = colorResource(id = R.color.white_transparent))
            IconButton(iconRes = R.drawable.ic_mail_home, backgroundColor = Color.White)
            IconButton(iconRes = R.drawable.ic_language, backgroundColor = Color.White)
            IconButton(iconRes = R.drawable.ic_chat_home, backgroundColor = colorResource(id = R.color.blue_button))
        }
        Spacer(modifier = Modifier.padding(16.dp))
        LogoutButton { context, onComplete -> viewModel.signout(context, onComplete) }
        Spacer(modifier = Modifier.padding(12.dp))
    }


}

@Composable
fun LogoutButton(signOut: (Context, () -> Unit) -> Unit) {
    val context = LocalContext.current
    Button(
        onClick = {
            signOut(context) {
                Toast.makeText(context, "Sesión cerrada exitosamente", Toast.LENGTH_SHORT).show()
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.blue_button),
            disabledContainerColor = colorResource(id = R.color.blue),
            contentColor = Color.White,
            disabledContentColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = "Cerrar sesión",
            fontSize = 18.sp
        )
    }
}

@Composable
fun IconButton(iconRes: Int, backgroundColor: Color) {
    Box(
        modifier = Modifier
            .size(64.dp) // Tamaño cuadrado del botón
            .clip(RoundedCornerShape(16.dp)) // Esquinas redondeadas
            .background(backgroundColor)
            .clickable { /* Sin acción por ahora */ },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(48.dp), // Tamaño del icono más grande
        )
    }
}



@Composable
fun HeaderImage(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.ic_home),
        contentDescription = "Header",
        modifier = modifier.size(220.dp)

    )
}
