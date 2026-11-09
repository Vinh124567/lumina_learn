package com.example.luminalearn.data.remote.api

import com.example.luminalearn.data.model.ApiResponse
import com.example.luminalearn.data.model.LessonDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface LessonApiService {

    @GET("lessons")
    suspend fun getAllLessons(
        @Query("level") level: String? = null,
        @Query("category") category: String? = null
    ): Response<ApiResponse<List<LessonDto>>>

    @GET("lessons/recommended")
    suspend fun getRecommendedLessons(): Response<ApiResponse<List<LessonDto>>>

    @GET("lessons/{id}")
    suspend fun getLessonById(
        @Path("id") id: String
    ): Response<ApiResponse<LessonDto>>

    @POST("lessons/{id}/complete")
    suspend fun completeLesson(
        @Path("id") id: String,
        @Query("stars") stars: Int = 3
    ): Response<ApiResponse<LessonDto>>

    @GET("lessons/{id}/review-questions")
    suspend fun getReviewQuestions(
        @Path("id") id: String
    ): Response<ApiResponse<List<com.example.luminalearn.data.model.QuickReviewQuestionDto>>>

    @POST("lessons/{id}/review")
    suspend fun submitReview(
        @Path("id") id: String
    ): Response<ApiResponse<com.example.luminalearn.data.model.ReviewResultDto>>

    @GET("lessons/exam")
    suspend fun getExamQuestions(
        @Query("level") level: String
    ): Response<ApiResponse<List<com.example.luminalearn.data.model.ExamQuestionDto>>>

    @POST("lessons/units/{unitNumber}/claim-chest")
    suspend fun claimChestReward(
        @Path("unitNumber") unitNumber: Int
    ): Response<ApiResponse<com.example.luminalearn.data.model.ChestRewardDto>>

    @GET("lessons/units")
    suspend fun getJourneyUnits(
        @Query("level") level: String
    ): Response<ApiResponse<List<com.example.luminalearn.data.model.JourneyUnitDto>>>

    @GET("user/hearts")
    suspend fun getHeartStatus(): Response<ApiResponse<com.example.luminalearn.data.model.HeartStatusDto>>

    @POST("user/hearts/consume")
    suspend fun consumeHeart(): Response<ApiResponse<com.example.luminalearn.data.model.HeartStatusDto>>

    @POST("user/hearts/refill")
    suspend fun refillHearts(): Response<ApiResponse<com.example.luminalearn.data.model.HeartStatusDto>>

    @GET("leaderboard/weekly")
    suspend fun getWeeklyLeaderboard(): Response<ApiResponse<com.example.luminalearn.data.model.LeaderboardDto>>

    @GET("user/analytics")
    suspend fun getUserAnalytics(): Response<ApiResponse<com.example.luminalearn.data.model.AnalyticsSummaryDto>>

    @GET("lessons/grammar")
    suspend fun getGrammarStructures(
        @Query("level") level: String? = null
    ): Response<ApiResponse<List<com.example.luminalearn.data.model.GrammarStructureDto>>>
}
