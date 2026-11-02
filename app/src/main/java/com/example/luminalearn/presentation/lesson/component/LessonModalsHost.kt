package com.example.luminalearn.presentation.lesson.component

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.luminalearn.data.model.AnalyticsSummaryDto
import com.example.luminalearn.data.model.HeartStatusDto
import com.example.luminalearn.data.model.QuickReviewQuestionDto
import com.example.luminalearn.data.model.SkillRadarScoreDto
import com.example.luminalearn.presentation.lesson.LessonUiState
import com.example.luminalearn.presentation.lesson.LessonViewModel
import com.example.luminalearn.presentation.lesson.model.ActiveLessonModal

/**
 * Điều phối tập trung toàn bộ Dialog và Modal của Tab Bài học.
 * Đảm bảo state phân tách rõ ràng, không rò rỉ bộ nhớ và không dẫm chân nhau.
 */
@Composable
fun LessonModalsHost(
    activeModal: ActiveLessonModal?,
    uiState: LessonUiState,
    lessonViewModel: LessonViewModel,
    onDismissModal: () -> Unit,
    onStartLesson: (ChineseLessonData) -> Unit,
    onCardChange: (ChineseLessonData, ToneCardData?) -> Unit,
    onSpeak: (String) -> Unit
) {
    val context = LocalContext.current

    when (activeModal) {
        null -> Unit

        is ActiveLessonModal.Preview -> {
            LessonPreviewModal(
                lesson = activeModal.lesson,
                onStartLesson = {
                    onStartLesson(activeModal.lesson)
                },
                onDismiss = onDismissModal,
                onSpeak = onSpeak
            )
        }

        is ActiveLessonModal.Handbook -> {
            UnitHandbookModal(
                unitNumber = activeModal.unitNumber,
                unitTitle = activeModal.title,
                lessons = activeModal.lessons,
                onSpeak = onSpeak,
                onDismiss = onDismissModal
            )
        }

        is ActiveLessonModal.QuickReview -> {
            val lesson = activeModal.lesson
            var reviewQuestions by remember(lesson.id) { mutableStateOf<List<QuickReviewQuestionDto>>(emptyList()) }
            var isReviewLoading by remember(lesson.id) { mutableStateOf(true) }
            var reviewErrorMessage by remember(lesson.id) { mutableStateOf<String?>(null) }

            LaunchedEffect(lesson.id) {
                isReviewLoading = true
                reviewErrorMessage = null
                lessonViewModel.loadReviewQuestions(
                    lessonId = lesson.id,
                    onSuccess = { list ->
                        reviewQuestions = list
                        isReviewLoading = false
                    },
                    onError = { err ->
                        reviewErrorMessage = err
                        isReviewLoading = false
                    }
                )
            }

            QuickReviewModal(
                lesson = lesson,
                questions = reviewQuestions,
                isLoading = isReviewLoading,
                errorMessage = reviewErrorMessage,
                onCompleted = { lessonId ->
                    lessonViewModel.submitReview(
                        lessonId = lessonId,
                        onSuccess = { sparksBonus, _, message ->
                            onDismissModal()
                            Toast.makeText(
                                context,
                                message.ifBlank { "Hàn gắn trạm thành công! +$sparksBonus Sparks đã được cộng 🎉" },
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        onError = { err ->
                            Toast.makeText(context, err, Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                onDismiss = onDismissModal
            )
        }

        is ActiveLessonModal.ChestReward -> {
            val unitNum = activeModal.unitNumber
            ChestRewardDialog(
                unitNumber = unitNum,
                sparksEarned = 50,
                expEarned = 100,
                onClaim = {
                    lessonViewModel.claimChest(
                        unitNumber = unitNum,
                        onSuccess = { sparks, exp ->
                            Toast.makeText(
                                context,
                                "Chúc mừng! Bạn đã nhận được +$sparks Sparks và +$exp EXP từ máy chủ 🎉",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        onError = { error ->
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                onDismiss = onDismissModal
            )
        }

        is ActiveLessonModal.BossExam -> {
            var examQuestions by remember { mutableStateOf<List<BossQuestion>>(emptyList()) }
            var isExamLoading by remember { mutableStateOf(true) }
            var examErrorMessage by remember { mutableStateOf<String?>(null) }

            val loadQuestions: () -> Unit = {
                isExamLoading = true
                examErrorMessage = null
                lessonViewModel.loadExamQuestions(
                    level = uiState.selectedHskLevel,
                    onSuccess = { list ->
                        examQuestions = list
                        isExamLoading = false
                    },
                    onError = { err ->
                        examErrorMessage = err
                        isExamLoading = false
                    }
                )
            }

            LaunchedEffect(uiState.selectedHskLevel) {
                loadQuestions()
            }

            BossExamDialog(
                levelName = uiState.selectedHskLevel.uppercase(),
                questions = examQuestions,
                isLoading = isExamLoading,
                errorMessage = examErrorMessage,
                onRetry = loadQuestions,
                onDismiss = onDismissModal,
                onPassExam = {
                    onDismissModal()
                    Toast.makeText(context, "Chúc mừng bạn đã vượt cấp thành công! Mở khóa HSK 2! 🏆", Toast.LENGTH_LONG).show()
                    lessonViewModel.selectHskLevel("HSK 2")
                },
                onSpeak = onSpeak,
                onWrongAnswer = {
                    lessonViewModel.consumeHeart()
                }
            )
        }

        is ActiveLessonModal.InteractiveLearning -> {
            val lesson = activeModal.lesson
            val currentCard = activeModal.currentCard
            LessonDetailDialog(
                toneCardData = currentCard,
                lesson = lesson,
                action = LessonAction(
                    onDismiss = onDismissModal,
                    onNext = {
                        val nextIndex = currentCard.currentIndex
                        onCardChange(lesson, lesson.slides.getOrNull(nextIndex))
                    },
                    onPrev = {
                        val prevIndex = currentCard.currentIndex - 2
                        if (prevIndex >= 0) {
                            onCardChange(lesson, lesson.slides.getOrNull(prevIndex))
                        }
                    },
                    onComplete = {
                        onDismissModal()
                        lessonViewModel.completeLesson(lesson.id, 3)
                        Toast.makeText(context, "Chúc mừng bạn đã hoàn thành bài học đạt 3 sao! 🎉", Toast.LENGTH_SHORT).show()
                    },
                    onCompleteWithStars = { stars ->
                        onDismissModal()
                        lessonViewModel.completeLesson(lesson.id, stars)
                        Toast.makeText(context, "Chúc mừng bạn đã hoàn thành bài học đạt $stars sao! 🎉", Toast.LENGTH_SHORT).show()
                    }
                )
            )
        }

        is ActiveLessonModal.Hearts -> {
            val hearts = uiState.heartStatus ?: HeartStatusDto(
                currentHearts = 5,
                maxHearts = 5,
                nextRefillMinutes = 0,
                sparksRefillCost = 50,
                message = ""
            )
            HeartRefillModal(
                heartStatus = hearts,
                onRefillWithSparks = {
                    lessonViewModel.refillHearts(
                        onSuccess = { msg ->
                            onDismissModal()
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        },
                        onError = { err ->
                            Toast.makeText(context, err, Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                onPracticeToEarn = {
                    onDismissModal()
                    Toast.makeText(
                        context,
                        "Hãy chọn một bài học đã qua để ôn tập và hồi tim miễn phí!",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onDismiss = onDismissModal
            )
        }

        is ActiveLessonModal.Analytics -> {
            val analyticsData = uiState.analytics ?: AnalyticsSummaryDto(
                totalLessonsCompleted = uiState.allLessons.count { it.isCompleted },
                totalStars = uiState.allLessons.sumOf { it.stars },
                masteredVocabsCount = uiState.allLessons.sumOf { it.coreVocabularies.size },
                accuracyPercentage = 85,
                radarScores = SkillRadarScoreDto()
            )
            SkillRadarModal(
                analytics = analyticsData,
                onDismiss = onDismissModal
            )
        }

        is ActiveLessonModal.GrammarVault -> {
            HskGrammarVaultModal(
                grammarList = uiState.grammarStructures,
                isLoading = uiState.isGrammarLoading,
                onFilterLevel = { level ->
                    lessonViewModel.loadGrammarStructures(level)
                },
                onSpeak = onSpeak,
                onDismiss = onDismissModal
            )
        }
    }
}
