package com.example.luminalearn.data.remote.api

import com.example.luminalearn.data.model.ApiResponse
import com.example.luminalearn.data.model.AuthResponseDto
import com.example.luminalearn.data.model.LoginRequestDto
import com.example.luminalearn.data.model.RefreshTokenRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequestDto
    ): Response<ApiResponse<AuthResponseDto>>

    @POST("auth/refresh")
    suspend fun refreshToken(
        @Body request: RefreshTokenRequestDto
    ): Response<ApiResponse<AuthResponseDto>>
}
