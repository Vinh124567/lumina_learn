package com.example.luminalearn.data.remote.api

import com.example.luminalearn.data.model.ApiResponse
import com.example.luminalearn.data.model.LessonDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface LessonApiService {

    @GET("lessons/recommended")
    suspend fun getRecommendedLessons(): Response<ApiResponse<List<LessonDto>>>

    @GET("lessons/{id}")
    suspend fun getLessonById(
        @Path("id") id: String
    ): Response<ApiResponse<LessonDto>>
}
