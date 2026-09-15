package com.example.luminalearn.data.model

import com.google.gson.annotations.SerializedName

data class DailyWisdomDto(
    @SerializedName("id") val id: Long = 0,
    @SerializedName("chinese") val chinese: String = "",
    @SerializedName("pinyin") val pinyin: String = "",
    @SerializedName("vietnamese") val vietnamese: String = "",
    @SerializedName("meaning") val meaning: String = "",
    @SerializedName("author") val author: String = ""
)
