package com.example.luminalearn.data.remote.api

import com.example.luminalearn.data.model.ApiResponse
import com.example.luminalearn.data.model.DailyWisdomDto
import retrofit2.Response
import retrofit2.http.GET

interface WisdomApiService {

    @GET("wisdom/today")
    suspend fun getTodayWisdom(): Response<ApiResponse<DailyWisdomDto>>

    @GET("wisdom/random")
    suspend fun getRandomWisdom(): Response<ApiResponse<DailyWisdomDto>>
}
