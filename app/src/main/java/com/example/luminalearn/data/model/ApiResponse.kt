package com.example.luminalearn.data.model

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("errorCode") val errorCode: String? = null,
    @SerializedName("data") val data: T? = null
)
