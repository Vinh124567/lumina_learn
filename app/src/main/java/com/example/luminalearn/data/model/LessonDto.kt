package com.example.luminalearn.data.model

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import com.example.luminalearn.presentation.lesson.component.ChineseLessonData
import com.example.luminalearn.presentation.lesson.component.SlideType
import com.example.luminalearn.presentation.lesson.component.ToneCardData
import com.example.luminalearn.presentation.lesson.component.ToneRuleItem
import com.example.luminalearn.presentation.main.component.LessonCardData
import com.google.gson.annotations.SerializedName

data class ToneRuleDto(
    @SerializedName("number") val number: Int = 1,
    @SerializedName("text") val text: String = ""
)

data class SlideDto(
    @SerializedName("slideIndex") val slideIndex: Int = 1,
    @SerializedName("category") val category: String = "",
    @SerializedName("cardType") val cardType: String = "",
    @SerializedName("slideType") val slideType: String = "CONCEPT",
    @SerializedName("subTitle") val subTitle: String = "",
    @SerializedName("title") val title: String = "",
    @SerializedName("pinyinVariants") val pinyinVariants: List<String> = emptyList(),
    @SerializedName("hanziVariants") val hanziVariants: List<String> = emptyList(),
    @SerializedName("hanViet") val hanViet: String = "",
    @SerializedName("meaning") val meaning: String = "",
    @SerializedName("explanationText") val explanationText: String = "",
    @SerializedName("explanationSubtitle") val explanationSubtitle: String = "",
    @SerializedName("explanationContent") val explanationContent: String = "",
    @SerializedName("boxColor") val boxColor: String = "",
    @SerializedName("toneRules") val toneRules: List<ToneRuleDto> = emptyList(),
    @SerializedName("audioUrl") val audioUrl: String = "",
    @SerializedName("question") val question: String = "",
    @SerializedName("options") val options: List<String> = emptyList(),
    @SerializedName("correctAnswerIndex") val correctAnswerIndex: Int = -1,
    @SerializedName("quizExplanation") val quizExplanation: String = "",
    @SerializedName("currentIndex") val currentIndex: Int = 1,
    @SerializedName("totalCount") val totalCount: Int = 4
)

data class LessonDto(
    @SerializedName("id") val id: String,
    @SerializedName("category") val category: String,
    @SerializedName("categoryBgColor") val categoryBgColor: String = "#FCE7F3",
    @SerializedName("categoryTextColor") val categoryTextColor: String = "#9333EA",
    @SerializedName("level") val level: String = "Cơ bản",
    @SerializedName("pinyinHanziTitle") val pinyinHanziTitle: String = "",
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String = "",
    @SerializedName("durationMins") val durationMins: Int = 0,
    @SerializedName("sparks") val sparks: Int = 0,
    @SerializedName("isCompleted") val isCompleted: Boolean = false,
    @SerializedName("totalSlides") val totalSlides: Int = 0,
    @SerializedName("slides") val slides: List<SlideDto> = emptyList()
)

fun LessonDto.toChineseLessonData(): ChineseLessonData {
    val bgColor = try {
        Color(categoryBgColor.toColorInt())
    } catch (_: Exception) {
        Color(0xFFF3E8FF)
    }

    val textColor = try {
        Color(categoryTextColor.toColorInt())
    } catch (_: Exception) {
        Color(0xFF7E22CE)
    }

    return ChineseLessonData(
        id = id,
        category = category,
        level = level,
        pinyinHanziTitle = pinyinHanziTitle,
        title = title,
        description = description,
        durationMins = durationMins,
        sparks = sparks,
        isCompleted = isCompleted,
        totalSlides = totalSlides,
        slides = slides.map { it.toToneCardData() },
        categoryBgColor = bgColor,
        categoryTextColor = textColor
    )
}

fun LessonDto.toLessonCardData(): LessonCardData {
    val bgColor = try {
        Color(categoryBgColor.toColorInt())
    } catch (_: Exception) {
        Color(0xFFFCE7F3)
    }

    val textColor = try {
        Color(categoryTextColor.toColorInt())
    } catch (_: Exception) {
        Color(0xFF9333EA)
    }

    return LessonCardData(
        category = category,
        categoryBgColor = bgColor,
        categoryTextColor = textColor,
        durationMins = durationMins,
        sparks = sparks,
        title = title,
        description = description,
        slideCount = totalSlides
    )
}

fun SlideDto.toToneCardData(): ToneCardData {
    return ToneCardData(
        slideIndex = slideIndex,
        category = category,
        cardType = cardType,
        slideType = SlideType.fromString(slideType),
        subTitle = subTitle,
        title = title,
        pinyinVariants = pinyinVariants,
        hanziVariants = hanziVariants,
        hanViet = hanViet,
        meaning = meaning,
        explanationText = explanationText,
        explanationSubtitle = explanationSubtitle,
        explanationContent = explanationContent,
        boxColor = boxColor,
        toneRules = toneRules.map { ToneRuleItem(number = it.number, text = it.text) },
        audioUrl = audioUrl,
        isPlaying = false,
        question = question,
        options = options,
        correctAnswerIndex = correctAnswerIndex,
        quizExplanation = quizExplanation,
        currentIndex = currentIndex,
        totalCount = totalCount
    )
}
