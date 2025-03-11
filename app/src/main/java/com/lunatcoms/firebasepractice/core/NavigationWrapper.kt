package com.lunatcoms.firebasepractice.core

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lunatcoms.firebasepractice.login.ui.LoginScreen
import com.lunatcoms.firebasepractice.user.ui.HomeScreen

@Composable
fun NavigationWrapper(){

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Login){
       composable<Login> {
           LoginScreen { navController.navigate(Home) }
       }

        composable<Home> {
            HomeScreen()
        }
    }
}