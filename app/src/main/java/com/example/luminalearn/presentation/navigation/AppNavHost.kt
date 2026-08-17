package com.example.luminalearn.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.luminalearn.presentation.login.LoginScreen
import com.example.luminalearn.presentation.main.AppDestination
import com.example.luminalearn.presentation.main.MainScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AppDestination.Login.route
    ) {
        composable(AppDestination.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(AppDestination.Main.route) {
            MainScreen(navController = navController)
        }
        // Sau này thêm màn hình khác vào đây
    }
}

