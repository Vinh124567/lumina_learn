package com.example.luminalearn.presentation.main

import android.widget.Space
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.luminalearn.R
import com.example.luminalearn.presentation.common.AppScaffold
import com.example.luminalearn.presentation.common.AppTopBar
import com.example.luminalearn.presentation.main.component.GreetingHeader
import com.example.luminalearn.presentation.main.component.TopBar
import com.example.luminalearn.ui.theme.TextSecondary
import com.example.luminalearn.ui.theme.Yellow
import kotlinx.coroutines.flow.collectLatest

import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.example.luminalearn.presentation.main.component.DailyGoalCard
import com.example.luminalearn.presentation.main.component.DailyWisdomCard
import com.example.luminalearn.presentation.main.component.LessonCard
import com.example.luminalearn.presentation.main.component.SparkChallengeCard
import com.example.luminalearn.presentation.main.component.StreakCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel(),
    navController: NavHostController,
    onNavigateToSparkAi: () -> Unit = { navController.navigate(AppDestination.SparkAI.route) },
    onNavigateToLesson: () -> Unit = { navController.navigate(AppDestination.Lesson.route) }
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                is MainUiEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    AppScaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(16.dp),
        ) {
            TopBar()
            Spacer(modifier = Modifier.height(24.dp))
            GreetingHeader(
                onAskAiClick = onNavigateToSparkAi
            )
            Spacer(modifier = Modifier.height(20.dp))
            DailyGoalCard(
                currentMinutes = 10,
                targetMinutes = 10,
                bonusSparks = 30,
                onStartLessonClick = onNavigateToLesson
            )
            Spacer(modifier = Modifier.height(20.dp))
            StreakCard(
                streakDays = 5,
                checkedDays = listOf(true, true, true, true, true, false, false),
                onClaimClick = {
                    Toast.makeText(context, context.getString(R.string.msg_streak_claimed), Toast.LENGTH_SHORT).show()
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
            SparkChallengeCard(
                bonusSparks = 20,
                onCompleteClick = {
                    Toast.makeText(context, context.getString(R.string.msg_challenge_completed), Toast.LENGTH_SHORT).show()
                }
            )
            Spacer(modifier = Modifier.height(28.dp))

            // ── Section: Recommended Lessons ────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.recommended_lessons_title),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp
                    ),
                    color = Color(0xFF0F172A),
                    modifier = Modifier.weight(1f),
                    maxLines = 1
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.view_all),
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    ),
                    color = Color(0xFF5C50F6),
                    softWrap = false,
                    maxLines = 1,
                    modifier = Modifier.clickable { onNavigateToLesson() }
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.recommended_lessons_subtitle),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 13.sp
                ),
                color = Color(0xFF64748B)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Lesson 1: First Principles Thinking
            LessonCard(
                category = stringResource(R.string.category_creative_thinking),
                categoryBgColor = Color(0xFFFCE7F3),
                categoryTextColor = Color(0xFF9333EA),
                durationMins = 4,
                sparks = 30,
                title = stringResource(R.string.lesson_1_title),
                description = stringResource(R.string.lesson_1_desc),
                slideCount = 4,
                onStartClick = onNavigateToLesson
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Lesson 2: The 2-Minute Micro-Habit Rule
            LessonCard(
                category = stringResource(R.string.category_focus_productivity),
                categoryBgColor = Color(0xFFDBEAFE),
                categoryTextColor = Color(0xFF2563EB),
                durationMins = 3,
                sparks = 25,
                title = stringResource(R.string.lesson_2_title),
                description = stringResource(R.string.lesson_2_desc),
                slideCount = 4,
                onStartClick = onNavigateToLesson
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ── Section: Daily Wisdom Card ──────────────────────────────
            DailyWisdomCard(
                onRefreshClick = {
                    Toast.makeText(context, "Refreshed daily wisdom!", Toast.LENGTH_SHORT).show()
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
