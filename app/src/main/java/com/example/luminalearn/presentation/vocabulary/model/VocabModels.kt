package com.example.luminalearn.presentation.vocabulary.model

import androidx.compose.runtime.Immutable

@Immutable
data class VocabWordItem(
    val id: String,
    val hanzi: String,
    val pinyin: String,
    val hanViet: String,
    val meaning: String,
    val partOfSpeech: String,
    val topic: String,
    val radical: String,
    val strokes: String,
    val exampleHanzi: String,
    val examplePinyin: String,
    val exampleMeaning: String,
    val hskLevel: String,
    val targetScore: String = "HSK 1 (Mục tiêu 180–200/200 điểm)",
    val isMastered: Boolean = true
)

@Immutable
data class TopicItem(
    val id: String,
    val name: String,
    val icon: String,
    val count: Int
)

@Immutable
data class HskLevelFilter(
    val title: String,
    val scoreRange: String? = null
)

enum class VocabStudyMode(val title: String) {
    LIST("Danh sách từ"),
    FLASHCARD("Flashcard"),
    REFLEX("Luyện phản xạ")
}

object VocabConstants {
    const val ALL_TOPICS = "Tất cả chủ đề"
    const val ALL_LEVELS = "Tất cả cấp độ"
    const val TOPIC_ALL_ID = "all"
}
