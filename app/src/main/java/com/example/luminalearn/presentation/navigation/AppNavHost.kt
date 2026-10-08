package com.example.luminalearn.presentation.navigation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.luminalearn.presentation.container.MainContainerScreen
import com.example.luminalearn.presentation.login.LoginScreen
import com.example.luminalearn.presentation.main.AppDestination
import com.example.luminalearn.presentation.splash.SplashScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AppDestination.Splash.route,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> (fullWidth * 0.3f).toInt() },
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            ) + fadeIn(tween(280))
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> (-fullWidth * 0.25f).toInt() },
                animationSpec = tween(280)
            ) + fadeOut(tween(240))
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> (-fullWidth * 0.25f).toInt() },
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            ) + fadeIn(tween(280))
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> (fullWidth * 0.3f).toInt() },
                animationSpec = tween(280)
            ) + fadeOut(tween(240))
        }
    ) {
        composable(AppDestination.Splash.route) {
            SplashScreen(navController = navController)
        }
        composable(AppDestination.Onboarding.route) {
            com.example.luminalearn.presentation.onboarding.OnboardingScreen(navController = navController)
        }
        composable(AppDestination.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(AppDestination.Main.route) {
            MainContainerScreen(rootNavController = navController)
        }
    }
}

