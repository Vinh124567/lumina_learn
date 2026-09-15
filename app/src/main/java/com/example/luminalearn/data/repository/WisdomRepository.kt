package com.example.luminalearn.data.repository

import com.example.luminalearn.data.model.DailyWisdomDto
import com.example.luminalearn.data.remote.RetrofitClient
import com.example.luminalearn.data.remote.api.WisdomApiService

interface WisdomRepository {
    suspend fun getTodayWisdom(): Result<DailyWisdomDto>
    suspend fun getRandomWisdom(): Result<DailyWisdomDto>
}

class WisdomRepositoryImpl(
    private val wisdomApiService: WisdomApiService = RetrofitClient.wisdomApiService
) : WisdomRepository {

    override suspend fun getTodayWisdom(): Result<DailyWisdomDto> {
        return try {
            val response = wisdomApiService.getTodayWisdom()
            val body = response.body()
            if (response.isSuccessful && body != null && body.success && body.data != null) {
                Result.success(body.data)
            } else {
                Result.failure(Exception(body?.message ?: "Không thể tải thành ngữ hôm nay"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRandomWisdom(): Result<DailyWisdomDto> {
        return try {
            val response = wisdomApiService.getRandomWisdom()
            val body = response.body()
            if (response.isSuccessful && body != null && body.success && body.data != null) {
                Result.success(body.data)
            } else {
                Result.failure(Exception(body?.message ?: "Không thể đổi thành ngữ"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
