package com.example.luminalearn.data.model

import com.google.gson.annotations.SerializedName

data class LoginRequestDto(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class RefreshTokenRequestDto(
    @SerializedName("refreshToken") val refreshToken: String
)

data class UserDto(
    @SerializedName("id") val id: Long,
    @SerializedName("email") val email: String,
    @SerializedName("fullName") val fullName: String
)

data class AuthResponseDto(
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String,
    @SerializedName("tokenType") val tokenType: String = "Bearer",
    @SerializedName("expiresIn") val expiresIn: Long,
    @SerializedName("user") val user: UserDto
)
