package com.example.luminalearn.presentation.container

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.res.stringResource
import com.example.luminalearn.R
import com.example.luminalearn.presentation.common.AppBottomBar
import com.example.luminalearn.presentation.main.AppDestination
import com.example.luminalearn.presentation.main.MainScreen
import com.example.luminalearn.presentation.lesson.LessonScreen
import com.example.luminalearn.presentation.spark_ai_lab.SparkAILab

/**
 * Màn hình cha (Container) sau khi đăng nhập thành công.
 * Quản lý thanh BottomBar cố định và điều hướng giữa các tab chính:
 * - Tab 1: Home (MainScreen)
 * - Tab 2: Lesson / Explore
 * - Tab 3: Spark AI (SparkAILab)
 * - Tab 4: Reward
 */
@Composable
fun MainContainerScreen(
    rootNavController: NavHostController,
    modifier: Modifier = Modifier
) {
    val bottomNavController = rememberNavController()
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: AppDestination.Main.route

    fun navigateToTab(route: String) {
        if (route != currentRoute) {
            bottomNavController.navigate(route) {
                popUpTo(bottomNavController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        NavHost(
            navController = bottomNavController,
            startDestination = AppDestination.Main.route,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(AppDestination.Main.route) {
                MainScreen(
                    navController = rootNavController,
                    onNavigateToSparkAi = { navigateToTab(AppDestination.SparkAI.route) },
                    onNavigateToLesson = { navigateToTab(AppDestination.Lesson.route) }
                )
            }

            composable(AppDestination.Lesson.route) {
                LessonScreen()
            }

            composable(AppDestination.SparkAI.route) {
                SparkAILab()
            }

            composable(AppDestination.Reward.route) {
                PlaceholderScreen(title = stringResource(R.string.placeholder_rewards))
            }
        }

        AppBottomBar(
            currentRoute = currentRoute,
            onNavigate = { route -> navigateToTab(route) },
            onSparkAiClick = { navigateToTab(AppDestination.SparkAI.route) },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun PlaceholderScreen(
    title: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B),
                fontSize = 20.sp
            )
        )
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainContainerScreenPreview() {
    MainContainerScreen(
        rootNavController = androidx.navigation.compose.rememberNavController()
    )
}
