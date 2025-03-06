package com.lunatcoms.firebasepractice.ui.user.ui

import com.lunatcoms.firebasepractice.R

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(showBackground = true)

@Composable
fun UserScreen() {

    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Login(Modifier.align(Alignment.Center))
    }

}

@Composable
fun Login(modifier: Modifier) {
    Column(modifier = modifier) {
        HeaderImage(Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.padding(16.dp))
        EmailView("")
        Spacer(modifier = Modifier.padding(4.dp))
        ProviderView("")
        Spacer(modifier = Modifier.padding(8.dp))
        Spacer(modifier = Modifier.padding(16.dp))
        LoginButton()
        Spacer(modifier = Modifier.padding(12.dp))

    }

}

@Composable
fun LoginButton() {
    Button(
        onClick = { },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFF4303),
            disabledContainerColor = Color(0xFFF78058),
            contentColor = Color.White,
            disabledContentColor = Color.White
        )
    ) {
        Text(text = "Cerrar sesión")
    }
}

@Composable
fun ProviderView(providerApp:String) {
    Text(
        text = providerApp.ifEmpty {"Proveedor"},
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF636262)
    )
}

@Composable
fun EmailView(email: String) {
    Text(
        text = email.ifEmpty {"Email@pruebas.com"},
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF636262)
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
