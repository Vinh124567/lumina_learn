package com.example.luminalearn.presentation.spark_ai_lab

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.luminalearn.presentation.spark_ai_lab.component.DEFAULT_TOPICS
import com.example.luminalearn.presentation.spark_ai_lab.component.MicroPromptStudioBanner
import com.example.luminalearn.presentation.spark_ai_lab.component.ReflexQuizStudioBanner
import com.example.luminalearn.presentation.spark_ai_lab.component.RoleplayPracticeDialog
import com.example.luminalearn.presentation.spark_ai_lab.component.RoleplayScenarioCard
import com.example.luminalearn.presentation.spark_ai_lab.component.RoleplayScenarioData
import com.example.luminalearn.presentation.spark_ai_lab.component.RoleplayTopicCard
import com.example.luminalearn.presentation.spark_ai_lab.component.RoleplayTopicData
import com.example.luminalearn.presentation.spark_ai_lab.component.SparkAiBentoGrid
import com.example.luminalearn.presentation.spark_ai_lab.component.SparkAiHeader
import com.example.luminalearn.presentation.spark_ai_lab.component.SparkPromptCard
import com.example.luminalearn.presentation.vocabulary.component.PixelBadge
import com.example.luminalearn.presentation.vocabulary.component.PixelColors
import com.example.luminalearn.presentation.vocabulary.component.PixelStepCornerShape

/**
 * Các phân hệ màn hình của Spark AI Lab (Bento Hub Architecture).
 */
enum class SparkAiScreen {
    HUB,          // Màn hình Bento Grid chính
    ROLEPLAY,     // Màn hình Phòng Luyện Hội Thoại
    MICRO_PROMPT, // Màn hình Trợ Lý Soạn Bài Học
    REFLEX_QUIZ   // Màn hình Đấu Trường Phản Xạ 5s
}

/**
 * Spark AI Lab thiết kế theo chuẩn Bento Grid:
 * - Màn hình chính là Bento Hub tối giản, trực quan.
 * - Ấn vào chức năng nào sẽ chuyển hẳn sang màn hình riêng của chức năng đó (không gộp rối mắt).
 * - Có nút quay lại Bento Hub rõ ràng.
 */
@Composable
fun SparkAILab(
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    var currentScreen by remember { mutableStateOf(SparkAiScreen.HUB) }
    var selectedTopic by remember { mutableStateOf<RoleplayTopicData?>(null) }
    var promptText by remember { mutableStateOf("") }
    var isGenerating by remember { mutableStateOf(false) }
    var activeScenario by remember { mutableStateOf<RoleplayScenarioData?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8FB))
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            when (currentScreen) {
                SparkAiScreen.HUB -> {
                    // ── 1. MÀN HÌNH BENTO GRID CHÍNH ──
                    SparkAiHeader()

                    Spacer(modifier = Modifier.height(16.dp))

                    SparkAiBentoGrid(
                        onNavigateToRoleplay = {
                            selectedTopic = null
                            currentScreen = SparkAiScreen.ROLEPLAY
                        },
                        onNavigateToPromptStudio = {
                            currentScreen = SparkAiScreen.MICRO_PROMPT
                        },
                        onNavigateToReflexQuiz = {
                            currentScreen = SparkAiScreen.REFLEX_QUIZ
                        }
                    )
                }

                SparkAiScreen.ROLEPLAY -> {
                    // ── 2. MÀN HÌNH PHÒNG HỘI THOẠI AI ──
                    RoleplayScreen(
                        selectedTopic = selectedTopic,
                        onBackToHub = { currentScreen = SparkAiScreen.HUB },
                        onSelectTopic = { selectedTopic = it },
                        onStartScenario = { activeScenario = it }
                    )
                }

                SparkAiScreen.MICRO_PROMPT -> {
                    // ── 3. MÀN HÌNH TRỢ LÝ SOẠN BÀI HỌC ──
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        BackToHubButton(
                            label = "SPARK AI HUB",
                            onClick = { currentScreen = SparkAiScreen.HUB }
                        )

                        MicroPromptStudioBanner()

                        SparkPromptCard(
                            prompt = promptText,
                            onPromptChange = { promptText = it },
                            isLoading = isGenerating,
                            onGenerateClick = {
                                coroutineScope.launch {
                                    isGenerating = true
                                    delay(2500)
                                    isGenerating = false
                                }
                            }
                        )
                    }
                }

                SparkAiScreen.REFLEX_QUIZ -> {
                    // ── 4. MÀN HÌNH ĐẤU TRƯỜNG PHẢN XẠ ──
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        BackToHubButton(
                            label = "SPARK AI HUB",
                            onClick = { currentScreen = SparkAiScreen.HUB }
                        )

                        ReflexQuizStudioBanner()
                    }
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }

    if (activeScenario != null) {
        RoleplayPracticeDialog(
            scenario = activeScenario!!,
            onDismiss = { activeScenario = null }
        )
    }
}

/**
 * Nút quay lại màn hình Bento Hub chuẩn Pixel Arcade.
 */
@Composable
private fun BackToHubButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonShape = PixelStepCornerShape(1.5.dp)
    Box(
        modifier = modifier
            .clip(buttonShape)
            .background(Color.White)
            .border(BorderStroke(1.2.dp, PixelColors.BorderDark), buttonShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "◀",
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                color = PixelColors.PixelBlue
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Black,
                color = PixelColors.BorderDark,
                letterSpacing = 0.3.sp
            )
        }
    }
}

/**
 * Màn hình Phòng Hội Thoại AI (hiển thị danh sách chủ đề hoặc kịch bản chi tiết).
 */
@Composable
private fun RoleplayScreen(
    selectedTopic: RoleplayTopicData?,
    onBackToHub: () -> Unit,
    onSelectTopic: (RoleplayTopicData?) -> Unit,
    onStartScenario: (RoleplayScenarioData) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Hàng điều hướng quay lại
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackToHubButton(
                label = if (selectedTopic == null) "SPARK AI HUB" else "TẤT CẢ CHỦ ĐỀ",
                onClick = {
                    if (selectedTopic == null) {
                        onBackToHub()
                    } else {
                        onSelectTopic(null)
                    }
                }
            )

            PixelBadge(
                text = if (selectedTopic == null) "PHÒNG HỘI THOẠI" else selectedTopic.levelBadge,
                backgroundColor = if (selectedTopic == null) PixelColors.CardBlue else selectedTopic.headerColor.copy(alpha = 0.15f),
                textColor = if (selectedTopic == null) PixelColors.PixelBlue else selectedTopic.headerColor
            )
        }

        if (selectedTopic == null) {
            // Danh sách các Card Ngang theo chủ đề
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "CHỦ ĐỀ GIAO TIẾP THỰC TẾ",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = PixelColors.BorderDark,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "${DEFAULT_TOPICS.size} CHỦ ĐỀ",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF64748B)
                )
            }

            DEFAULT_TOPICS.forEach { topic ->
                RoleplayTopicCard(
                    topic = topic,
                    onClick = { onSelectTopic(topic) }
                )
            }
        } else {
            // Chi tiết danh sách kịch bản của chủ đề
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "${selectedTopic.iconEmoji} ${selectedTopic.title}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = PixelColors.BorderDark,
                    letterSpacing = 0.3.sp
                )
                Text(
                    text = "${selectedTopic.chineseTitle} (${selectedTopic.pinyinTitle}) • ${selectedTopic.description}",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B),
                    fontWeight = FontWeight.Medium,
                    lineHeight = 16.sp
                )
            }

            selectedTopic.scenarios.forEach { scenario ->
                RoleplayScenarioCard(
                    scenario = scenario,
                    onStartClick = { onStartScenario(scenario) }
                )
            }
        }
    }
}