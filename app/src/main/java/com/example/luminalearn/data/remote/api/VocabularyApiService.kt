package com.example.luminalearn.data.remote.api

import com.example.luminalearn.data.model.ApiResponse
import com.example.luminalearn.data.model.HskLevelDto
import com.example.luminalearn.data.model.TopicDto
import com.example.luminalearn.data.model.VocabularyDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface VocabularyApiService {

    @GET("vocabularies/levels")
    suspend fun getLevels(): Response<ApiResponse<List<HskLevelDto>>>

    @GET("vocabularies/topics")
    suspend fun getTopics(
        @Query("hskLevel") hskLevel: String? = null
    ): Response<ApiResponse<List<TopicDto>>>

    @GET("vocabularies")
    suspend fun getVocabularies(
        @Query("topic") topic: String? = null,
        @Query("hskLevel") hskLevel: String? = null
    ): Response<ApiResponse<List<VocabularyDto>>>
}
