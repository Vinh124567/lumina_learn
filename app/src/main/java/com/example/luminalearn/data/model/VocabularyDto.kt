package com.example.luminalearn.data.model

import com.google.gson.annotations.SerializedName

data class TopicDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("count")
    val count: Int
)

data class VocabularyDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("hanzi")
    val hanzi: String,
    @SerializedName("pinyin")
    val pinyin: String,
    @SerializedName("hanViet")
    val hanViet: String,
    @SerializedName("meaning")
    val meaning: String,
    @SerializedName("partOfSpeech")
    val partOfSpeech: String,
    @SerializedName("topic")
    val topic: String,
    @SerializedName("radical")
    val radical: String,
    @SerializedName("strokes")
    val strokes: String,
    @SerializedName("exampleHanzi")
    val exampleHanzi: String,
    @SerializedName("examplePinyin")
    val examplePinyin: String,
    @SerializedName("exampleMeaning")
    val exampleMeaning: String,
    @SerializedName("hskLevel")
    val hskLevel: String = "HSK 1",
    @SerializedName("targetScore")
    val targetScore: String = "HSK 1 (Mục tiêu 180–200/200 điểm)",
    @SerializedName("isMastered")
    val isMastered: Boolean = false,
    @SerializedName("userId")
    val userId: Long? = null
)

data class HskLevelDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("scoreRange")
    val scoreRange: String? = null,
    @SerializedName("count")
    val count: Int = 0
)

data class CreateVocabularyRequest(
    @SerializedName("hanzi") val hanzi: String,
    @SerializedName("pinyin") val pinyin: String,
    @SerializedName("hanViet") val hanViet: String = "",
    @SerializedName("meaning") val meaning: String,
    @SerializedName("hskLevel") val hskLevel: String = "HSK 1",
    @SerializedName("topic") val topic: String = "Đời sống & Xã hội",
    @SerializedName("partOfSpeech") val partOfSpeech: String = "Danh từ",
    @SerializedName("radical") val radical: String = "",
    @SerializedName("strokes") val strokes: String = "",
    @SerializedName("exampleHanzi") val exampleHanzi: String = "",
    @SerializedName("examplePinyin") val examplePinyin: String = "",
    @SerializedName("exampleMeaning") val exampleMeaning: String = "",
    @SerializedName("note") val note: String = ""
)

