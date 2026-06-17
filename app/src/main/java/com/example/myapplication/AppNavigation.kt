package com.example.myapplication

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

sealed class Screen(val route: String) {
    object Landing : Screen("landing")
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object Main : Screen("main") // This could be the BakingScreen or a placeholder
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Landing.route) {
        composable(Screen.Landing.route) {
            LandingScreen(
                onLoginClick = { navController.navigate(Screen.Login.route) },
                onSignUpClick = { navController.navigate(Screen.SignUp.route) }
            )
        }
        composable(Screen.Login.route) {
            LoginScreen(
                onBack = { navController.popBackStack() },
                onLoginSuccess = { navController.navigate(Screen.Main.route) }
            )
        }
        composable(Screen.SignUp.route) {
            SignUpScreen(
                onBack = { navController.navigate(Screen.Login.route) },
                onSignUpSuccess = { navController.navigate(Screen.Main.route) }
            )
        }
        composable(Screen.Main.route) {
            // Reusing the existing BakingScreen as the main page after login
            BakingScreen()
        }
    }
}
