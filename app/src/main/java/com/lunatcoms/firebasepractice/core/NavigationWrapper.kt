package com.lunatcoms.firebasepractice.core

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lunatcoms.firebasepractice.login.ui.LoginScreen
import com.lunatcoms.firebasepractice.login.ui.LoginViewModel
import com.lunatcoms.firebasepractice.login.ui.signup.SignupScreen
import com.lunatcoms.firebasepractice.user.ui.HomeScreen

@Composable
fun NavigationWrapper(){

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Login){
       composable<Login> {
           LoginScreen(LoginViewModel(), {
               navController.navigate(Home){
               popUpTo(Login) { inclusive = true }
           } },
               { navController.navigate(Signup) })
       }

        composable<Signup> {
            SignupScreen(LoginViewModel()) {navController.navigateUp()}
        }

        composable<Home> {
            HomeScreen()
        }
    }
}