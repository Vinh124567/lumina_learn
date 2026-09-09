package com.example.luminalearn.presentation.main

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.luminalearn.R
import com.example.luminalearn.presentation.common.AppScaffold
import com.example.luminalearn.presentation.common.CelebrationEffect
import com.example.luminalearn.presentation.lesson.component.LessonAction
import com.example.luminalearn.presentation.lesson.component.LessonDetailDialog
import com.example.luminalearn.presentation.lesson.component.ToneCardData
import com.example.luminalearn.presentation.main.component.DailyGoalCard
import com.example.luminalearn.presentation.main.component.DailyWisdomCard
import com.example.luminalearn.presentation.main.component.GreetingHeader
import com.example.luminalearn.presentation.main.component.LessonCard
import com.example.luminalearn.presentation.main.component.LessonCardData
import com.example.luminalearn.presentation.main.component.SparkChallengeCard
import com.example.luminalearn.presentation.main.component.StreakCard
import com.example.luminalearn.presentation.main.component.TopBar
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel(),
    navController: NavHostController,
    onNavigateToSparkAi: () -> Unit = { navController.navigate(AppDestination.SparkAI.route) },
    onNavigateToLesson: () -> Unit = { navController.navigate(AppDestination.Lesson.route) }
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var confettiTrigger by remember { mutableIntStateOf(0) }
    var lessonContent by remember { mutableStateOf<ToneCardData?>(null) }
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                TopBar(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(horizontal = 16.dp),
                ) {
                    Spacer(modifier = Modifier.height(12.dp))
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
                            confettiTrigger++
                        }
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    SparkChallengeCard(
                        bonusSparks = 20,
                        onCompleteClick = {
                            confettiTrigger++
                            Toast.makeText(
                                context,
                                context.getString(R.string.msg_challenge_completed),
                                Toast.LENGTH_SHORT
                            ).show()
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
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onNavigateToLesson() }
                                .padding(horizontal = 8.dp, vertical = 6.dp)
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

                    // Lesson 1: Pinyin
                    LessonCard(
                        data = LessonCardData(
                            category = stringResource(R.string.category_pinyin),
                            categoryBgColor = Color(0xFFFCE7F3),
                            categoryTextColor = Color(0xFF9333EA),
                            durationMins = 4,
                            sparks = 30,
                            title = stringResource(R.string.lesson_1_title),
                            description = stringResource(R.string.lesson_1_desc),
                            slideCount = 4
                        ),
                        onStartClick = {
                            lessonContent = ToneCardData(
                                category = "PHÁT ÂM PINYIN",
                                subTitle = "四声与变调 (Sì shēng yǔ biàntiào)",
                                title = "4 Thanh điệu Pinyin & Quy tắc biến âm",
                                pinyinVariants = listOf("mā", "má", "mǎ", "mà"),
                                hanViet = "Ma (Mẹ) · Ma (Gái) · Mã (Ngựa) · Mắng",
                                meaning = "Bốn ý nghĩa hoàn toàn khác nhau chỉ nhờ thay đổi thanh điệu!",
                                explanationText = "Bản đồ 4 Thanh điệu qua chữ \"ma\"",
                                currentIndex = 1,
                                totalCount = 4
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Lesson 2: Radicals
                    LessonCard(
                        data = LessonCardData(
                            category = stringResource(R.string.category_radicals),
                            categoryBgColor = Color(0xFFDBEAFE),
                            categoryTextColor = Color(0xFF2563EB),
                            durationMins = 3,
                            sparks = 25,
                            title = stringResource(R.string.lesson_2_title),
                            description = stringResource(R.string.lesson_2_desc),
                            slideCount = 4
                        ),
                        onStartClick = onNavigateToLesson
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // ── Section: Daily Wisdom Card ──────────────────────────────
                    DailyWisdomCard(
                        onRefreshClick = {
                            Toast.makeText(context, "Refreshed daily wisdom!", Toast.LENGTH_SHORT)
                                .show()
                        }
                    )

                    Spacer(modifier = Modifier.height(96.dp))
                }
            }

            // Hiệu ứng pháo hoa nổ đè lên giữa màn hình khi nhận thưởng
            CelebrationEffect(
                triggerKey = confettiTrigger,
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    lessonContent?.let {
        LessonDetailDialog(
            toneCardData  = it,
            action = LessonAction(
                onDismiss = { lessonContent = null }
            )
        )
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
    MainScreen(
        navController = androidx.navigation.compose.rememberNavController()
    )
}
