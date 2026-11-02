package com.example.luminalearn.presentation.lesson

import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.luminalearn.presentation.lesson.component.LessonMainContent
import com.example.luminalearn.presentation.lesson.component.LessonModalsHost
import com.example.luminalearn.presentation.lesson.model.ActiveLessonModal
import java.util.Locale

private val ScreenBackground = Color(0xFFF6F8FB)

/**
 * Màn hình trung tâm Bài học (Lesson Screen).
 * Đã được refactor tinh gọn theo Clean Code & Single Active Modal Pattern:
 * - Điều phối trạng thái ở mức cao.
 * - Delegate hiển thị nội dung cho [LessonMainContent].
 * - Quản lý tập trung toàn bộ modal/dialog qua [LessonModalsHost].
 */
@Composable
fun LessonScreen(
    modifier: Modifier = Modifier,
    lessonViewModel: LessonViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by lessonViewModel.uiState.collectAsState()

    // Chế độ xem: true -> Bản đồ lộ trình phiêu lưu, false -> Danh sách thẻ bài học
    var isJourneyView by rememberSaveable { mutableStateOf(true) }

    // Quản lý Modal duy nhất đang hiển thị, tránh xung đột và rò rỉ state
    var activeModal by remember { mutableStateOf<ActiveLessonModal?>(null) }

    // Khởi tạo TextToSpeech hỗ trợ phát âm tiếng Trung
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    DisposableEffect(context) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.CHINESE
            }
        }
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    val speakText: (String) -> Unit = { text ->
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, text)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ScreenBackground)
    ) {
        // ── 1. GIAO DIỆN NỘI DUNG CHÍNH (MAP JOURNEY / ARCHIVE LIST) ──
        LessonMainContent(
            uiState = uiState,
            isJourneyView = isJourneyView,
            onToggleJourneyView = { isJourneyView = it },
            onSelectLessonForPreview = { lesson ->
                activeModal = ActiveLessonModal.Preview(lesson)
            },
            onOpenChest = { unitNumber, isUnlocked ->
                if (isUnlocked) {
                    activeModal = ActiveLessonModal.ChestReward(unitNumber)
                } else {
                    Toast.makeText(
                        context,
                        "Hoàn thành các bài học trong Phần $unitNumber để mở rương phần thưởng!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            onOpenHandbook = { unitNum, title, unitLessons ->
                activeModal = ActiveLessonModal.Handbook(unitNum, title, unitLessons)
            },
            onStartBossExam = { levelName, isUnlocked ->
                if (isUnlocked) {
                    activeModal = ActiveLessonModal.BossExam
                } else {
                    Toast.makeText(
                        context,
                        "Hãy hoàn thành tất cả bài học trong $levelName để mở Cổng Thi Vượt Cấp!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            onQuickReview = { lesson ->
                activeModal = ActiveLessonModal.QuickReview(lesson)
            },
            onOpenHearts = { activeModal = ActiveLessonModal.Hearts },
            onOpenAnalytics = { activeModal = ActiveLessonModal.Analytics },
            onOpenGrammarVault = { activeModal = ActiveLessonModal.GrammarVault },
            onSelectHskLevel = lessonViewModel::selectHskLevel,
            onSearchQueryChange = lessonViewModel::updateSearchQuery,
            onSelectCategory = lessonViewModel::selectCategory,
            onRetry = lessonViewModel::loadLessons
        )

        // ── 2. QUẢN LÝ VÀ RENDER CÁC DIALOG / MODAL TẬP TRUNG ──
        LessonModalsHost(
            activeModal = activeModal,
            uiState = uiState,
            lessonViewModel = lessonViewModel,
            onDismissModal = { activeModal = null },
            onStartLesson = { lesson ->
                val hearts = uiState.heartStatus?.currentHearts ?: 5
                if (hearts <= 0) {
                    activeModal = ActiveLessonModal.Hearts
                    Toast.makeText(
                        context,
                        "Bạn đã hết tim! Hãy nạp tim bằng Sparks hoặc ôn bài cũ để hồi phục nhé ❤️",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (lesson.slides.isNotEmpty()) {
                    activeModal = ActiveLessonModal.InteractiveLearning(
                        lesson = lesson,
                        currentCard = lesson.slides.first()
                    )
                } else {
                    Toast.makeText(
                        context,
                        "Bài học \"${lesson.title}\" đang hoàn thiện slide thực hành",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            onCardChange = { lesson, card ->
                if (card != null) {
                    activeModal = ActiveLessonModal.InteractiveLearning(
                        lesson = lesson,
                        currentCard = card
                    )
                } else {
                    activeModal = null
                }
            },
            onSpeak = speakText
        )
    }
}
