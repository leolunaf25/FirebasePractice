package com.lunatcoms.firebasepractice.core

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lunatcoms.firebasepractice.login.ui.LoginScreen
import com.lunatcoms.firebasepractice.login.ui.AuthViewModel
import com.lunatcoms.firebasepractice.login.ui.signup.SignupScreen
import com.lunatcoms.firebasepractice.user.ui.HomeScreen

@Composable
fun NavigationWrapper() {

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Login) {
        composable<Login> {
            LoginScreen(AuthViewModel(),
                {
                    navController.navigate(Home) {
                        popUpTo<Login> { inclusive = true }
                        launchSingleTop = true
                    }
                },
                { navController.navigate(Signup) {
                    launchSingleTop = true
                } })
        }

        composable<Signup> {
            SignupScreen(AuthViewModel(),
                { navController.popBackStack() },
                {
                    navController.navigate(Home) {
                        popUpTo<Login> { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<Home> {
            HomeScreen(AuthViewModel()) {
                navController.navigate(Login) {
                    popUpTo<Home> { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }
}