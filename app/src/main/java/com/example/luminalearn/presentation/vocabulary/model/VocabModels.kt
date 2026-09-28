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
    val isMastered: Boolean = true,
    val userId: Long? = null
) {
    val isCustom: Boolean
        get() = userId != null

    fun matchesCategory(category: String): Boolean {
        if (category.isBlank() ||
            category == "Tất cả" ||
            category == "Tất cả chủ đề" ||
            category.equals("all", ignoreCase = true)
        ) {
            return true
        }

        val pos = partOfSpeech.lowercase()
        val top = topic.lowercase()
        val cat = category.lowercase()

        return when {
            cat.contains("tự thêm") || cat.contains("tôi tự thêm") || cat.contains("từ của tôi") || cat == "custom" ->
                isCustom
            cat.contains("động từ") -> pos.contains("động từ")
            cat.contains("danh từ") -> pos.contains("danh từ")
            cat.contains("tính từ") -> pos.contains("tính từ")
            cat.contains("hư từ") || cat.contains("ngữ pháp") || cat.contains("cấu trúc") ->
                pos.contains("phó từ") || pos.contains("liên từ") || pos.contains("giới từ") || pos.contains("trợ từ") || pos.contains("cảm thán")
            cat.contains("thành ngữ") ->
                pos.contains("thành ngữ") || top.contains("thành ngữ")
            cat.contains("lượng từ") || cat.contains("số từ") ->
                pos.contains("lượng từ") || pos.contains("số từ")
            cat.contains("kinh tế") || cat.contains("công nghệ") ->
                top.contains("kinh tế") || top.contains("tài chính") || top.contains("thương mại") || top.contains("công nghệ") || top.contains("ngân hàng")
            cat.contains("đời sống") || cat.contains("xã hội") ->
                top.contains("đời sống") || top.contains("xã hội") || top.contains("gia đình") || top.contains("giao tiếp") || top.contains("chào hỏi") || top.contains("ăn uống") || top.contains("mua sắm") || top.contains("khách sạn") || top.contains("thời trang") || top.contains("nhà cửa")
            cat.contains("tự nhiên") || cat.contains("môi trường") ->
                top.contains("tự nhiên") || top.contains("động vật") || top.contains("khí hậu") || top.contains("môi trường") || top.contains("thời tiết") || top.contains("phương hướng") || top.contains("màu sắc")
            else -> top.contains(category, ignoreCase = true) || pos.contains(category, ignoreCase = true)
        }
    }
}

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

enum class VocabSourceFilter(val title: String) {
    ALL("Tất cả kho từ"),
    CUSTOM("Từ tôi đã thêm"),
    MASTERED("Đã thuộc")
}

enum class VocabStudyMode(val title: String) {
    LIST("Danh sách từ"),
    FLASHCARD("Flashcard"),
    REFLEX("Luyện phản xạ")
}

object VocabConstants {
    const val ALL_TOPICS = "Tất cả"
    const val ALL_LEVELS = "Tất cả cấp độ"
    const val TOPIC_ALL_ID = "all"
}
