package com.example.luminalearn.presentation.container

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.luminalearn.R
import com.example.luminalearn.core.util.NetworkMonitor
import com.example.luminalearn.presentation.common.AppBottomBar
import com.example.luminalearn.presentation.lesson.LessonScreen
import com.example.luminalearn.presentation.main.AppDestination
import com.example.luminalearn.presentation.main.MainScreen
import com.example.luminalearn.presentation.spark_ai_lab.SparkAILab
import com.example.luminalearn.presentation.vocabulary.VocabularyScreen
import com.example.luminalearn.ui.theme.PlusJakartaSans

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

    val isOnline by com.example.luminalearn.core.util.NetworkMonitor.isOnline.collectAsState()

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        NavHost(
            navController = bottomNavController,
            startDestination = AppDestination.Main.route,
            modifier = Modifier.fillMaxSize(),
            enterTransition = {
                fadeIn(animationSpec = tween(260)) + scaleIn(
                    initialScale = 0.96f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    )
                )
            },
            exitTransition = {
                fadeOut(animationSpec = tween(180)) + scaleOut(
                    targetScale = 1.02f,
                    animationSpec = tween(180)
                )
            }
        ) {
            composable(AppDestination.Main.route) {
                MainScreen(
                    navController = rootNavController,
                    onNavigateToSparkAi = { navigateToTab(AppDestination.SparkAI.route) },
                    onNavigateToLesson = { navigateToTab(AppDestination.Lesson.route) }
                )
            }

            composable(AppDestination.Roadmap.route) {
                PlaceholderScreen(title = stringResource(R.string.nav_roadmap))
            }

            composable(AppDestination.Vocabulary.route) {
                VocabularyScreen()
            }

            composable(AppDestination.Lesson.route) {
                LessonScreen()
            }

            composable(AppDestination.SparkAI.route) {
                SparkAILab()
            }

            composable(AppDestination.Reward.route) {
                com.example.luminalearn.presentation.reward.WeeklyLeaderboardScreen(
                    onNavigateToLesson = { navigateToTab(AppDestination.Lesson.route) }
                )
            }
        }

        // ── CHỈ BÁO NGOẠI TUYẾN TOÀN CỤC (GLOBAL OFFLINE FLOATING PILL) ──
        AnimatedVisibility(
            visible = !isOnline,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 10.dp)
                .zIndex(99f)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF0F172A).copy(alpha = 0.92f))
                    .border(
                        BorderStroke(0.5.dp, Color(0xFF38BDF8).copy(alpha = 0.6f)),
                        RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF38BDF8))
                    )
                    Text(
                        text = stringResource(R.string.global_offline_indicator_msg),
                        fontFamily = PlusJakartaSans,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
            }
        }

        AppBottomBar(
            currentRoute = currentRoute,
            onNavigate = { route -> navigateToTab(route) },
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
