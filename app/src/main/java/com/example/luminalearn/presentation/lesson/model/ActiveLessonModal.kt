package com.example.luminalearn.presentation.lesson.model

import com.example.luminalearn.presentation.lesson.component.ChineseLessonData
import com.example.luminalearn.presentation.lesson.component.ToneCardData

/**
 * Quản lý trạng thái Dialog/Modal duy nhất trên màn hình Bài học (Single Active Modal Pattern).
 * Đảm bảo tại một thời điểm chỉ có tối đa 1 modal được hiển thị, ngăn chặn triệt để xung đột state.
 */
sealed interface ActiveLessonModal {
    data class Preview(val lesson: ChineseLessonData) : ActiveLessonModal
    data class Handbook(val unitNumber: Int, val title: String, val lessons: List<ChineseLessonData>) : ActiveLessonModal
    data class QuickReview(val lesson: ChineseLessonData) : ActiveLessonModal
    data class ChestReward(val unitNumber: Int) : ActiveLessonModal
    data object BossExam : ActiveLessonModal
    data class InteractiveLearning(val lesson: ChineseLessonData, val currentCard: ToneCardData) : ActiveLessonModal
    data object Hearts : ActiveLessonModal
    data object Analytics : ActiveLessonModal
    data object GrammarVault : ActiveLessonModal
}
