package com.example.luminalearn.data.repository

import com.example.luminalearn.data.model.LessonDto
import com.example.luminalearn.data.remote.RetrofitClient
import com.example.luminalearn.data.remote.api.LessonApiService

interface LessonRepository {
    suspend fun getRecommendedLessons(): Result<List<LessonDto>>
    suspend fun getLessonById(id: String): Result<LessonDto>
}

class LessonRepositoryImpl(
    private val lessonApiService: LessonApiService = RetrofitClient.lessonApiService
) : LessonRepository {

    override suspend fun getRecommendedLessons(): Result<List<LessonDto>> {
        return try {
            val response = lessonApiService.getRecommendedLessons()
            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!)
            } else {
                val errorMsg = response.body()?.message ?: "Không thể tải danh sách bài học đề xuất"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getLessonById(id: String): Result<LessonDto> {
        return try {
            val response = lessonApiService.getLessonById(id)
            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!)
            } else {
                val errorMsg = response.body()?.message ?: "Không tìm thấy bài học"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
