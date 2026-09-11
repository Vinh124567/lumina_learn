package com.example.luminalearn.presentation.lesson.component

enum class SlideType {
    CONCEPT,
    INTERACTIVE,
    QUIZ,
    TAKEAWAY;

    companion object {
        fun fromString(value: String?): SlideType {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: CONCEPT
        }
    }
}

data class LessonAction(
    val onNext: () -> Unit = {},
    val onPrev: () -> Unit = {},
    val onPlayAudio: () -> Unit = {},
    val onDismiss: () -> Unit = {},
    val onAskAi: () -> Unit = {},
    val onReport: () -> Unit = {},
    val onFavorite: () -> Unit = {},
    val onShare: () -> Unit = {},
    val onComplete: () -> Unit = onDismiss
)

data class ToneRuleItem(
    val number: Int,
    val text: String
)

data class ToneCardData(
    val slideIndex: Int = 1,
    val category: String = "",
    val cardType: String = "",
    val slideType: SlideType = SlideType.CONCEPT,
    val subTitle: String = "",
    val title: String = "",
    val pinyinVariants: List<String> = emptyList(),
    val hanziVariants: List<String> = emptyList(),
    val hanViet: String = "",
    val meaning: String = "",
    val explanationText: String = "",
    val explanationSubtitle: String = "",
    val explanationContent: String = "",
    val boxColor: String = "",
    val toneRules: List<ToneRuleItem> = emptyList(),
    val audioUrl: String = "",
    val isPlaying: Boolean = false,
    val question: String = "",
    val options: List<String> = emptyList(),
    val correctAnswerIndex: Int = -1,
    val quizExplanation: String = "",
    val currentIndex: Int = slideIndex,
    val totalCount: Int = 1
)
