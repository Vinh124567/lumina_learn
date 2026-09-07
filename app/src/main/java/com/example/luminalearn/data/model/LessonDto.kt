package com.example.luminalearn.data.model

import androidx.compose.ui.graphics.Color
import com.example.luminalearn.presentation.lesson.component.ChineseLessonData
import com.example.luminalearn.presentation.lesson.component.ToneCardData
import com.google.gson.annotations.SerializedName

data class SlideDto(
    @SerializedName("slideIndex") val slideIndex: Int = 1,
    @SerializedName("category") val category: String = "",
    @SerializedName("cardType") val cardType: String = "",
    @SerializedName("subTitle") val subTitle: String = "",
    @SerializedName("title") val title: String = "",
    @SerializedName("pinyinVariants") val pinyinVariants: List<String> = emptyList(),
    @SerializedName("hanViet") val hanViet: String = "",
    @SerializedName("meaning") val meaning: String = "",
    @SerializedName("explanationText") val explanationText: String = "",
    @SerializedName("audioUrl") val audioUrl: String = "",
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

// Extension mapper chuyển đổi sang Model UI hiện có của App
fun LessonDto.toChineseLessonData(): ChineseLessonData {
    val bgColor = try {
        Color(android.graphics.Color.parseColor(categoryBgColor))
    } catch (_: Exception) {
        Color(0xFFF3E8FF)
    }

    val textColor = try {
        Color(android.graphics.Color.parseColor(categoryTextColor))
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

fun SlideDto.toToneCardData(): ToneCardData {
    return ToneCardData(
        slideIndex = slideIndex,
        category = category,
        cardType = cardType,
        subTitle = subTitle,
        title = title,
        pinyinVariants = pinyinVariants,
        hanViet = hanViet,
        meaning = meaning,
        explanationText = explanationText,
        audioUrl = audioUrl,
        isPlaying = false,
        currentIndex = currentIndex,
        totalCount = totalCount
    )
}
