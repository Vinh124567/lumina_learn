package com.example.luminalearn.data.repository

import com.example.luminalearn.data.local.LessonCacheManager
import com.example.luminalearn.data.model.LessonDto
import com.example.luminalearn.data.remote.RetrofitClient
import com.example.luminalearn.data.remote.api.LessonApiService

interface LessonRepository {
    suspend fun getAllLessons(level: String? = null, category: String? = null): Result<List<LessonDto>>
    suspend fun getRecommendedLessons(): Result<List<LessonDto>>
    suspend fun getLessonById(id: String): Result<LessonDto>
    suspend fun completeLesson(id: String, stars: Int = 3): Result<LessonDto>
    suspend fun getExamQuestions(level: String): Result<List<com.example.luminalearn.data.model.ExamQuestionDto>>
    suspend fun claimChestReward(unitNumber: Int): Result<com.example.luminalearn.data.model.ChestRewardDto>
    suspend fun getJourneyUnits(level: String): Result<List<com.example.luminalearn.data.model.JourneyUnitDto>>
    suspend fun getReviewQuestions(id: String): Result<List<com.example.luminalearn.data.model.QuickReviewQuestionDto>>
    suspend fun submitReview(id: String): Result<com.example.luminalearn.data.model.ReviewResultDto>
    suspend fun getHeartStatus(): Result<com.example.luminalearn.data.model.HeartStatusDto>
    suspend fun consumeHeart(): Result<com.example.luminalearn.data.model.HeartStatusDto>
    suspend fun refillHearts(): Result<com.example.luminalearn.data.model.HeartStatusDto>
    suspend fun getWeeklyLeaderboard(): Result<com.example.luminalearn.data.model.LeaderboardDto>
    suspend fun getUserAnalytics(): Result<com.example.luminalearn.data.model.AnalyticsSummaryDto>
    suspend fun getGrammarStructures(level: String? = null): Result<List<com.example.luminalearn.data.model.GrammarStructureDto>>
}

class LessonRepositoryImpl(
    private val lessonApiService: LessonApiService = RetrofitClient.lessonApiService
) : LessonRepository {

    override suspend fun getAllLessons(level: String?, category: String?): Result<List<LessonDto>> {
        return try {
            val response = lessonApiService.getAllLessons(level, category)
            if (response.isSuccessful && response.body()?.data != null) {
                val data = response.body()!!.data!!
                if (level == null && category == null) {
                    LessonCacheManager.cacheLessons(data)
                }
                Result.success(data)
            } else {
                val cached = LessonCacheManager.getCachedLessons()
                if (!cached.isNullOrEmpty()) {
                    var filtered = cached
                    if (!level.isNullOrBlank()) filtered = filtered.filter { it.level.equals(level, ignoreCase = true) }
                    if (!category.isNullOrBlank()) filtered = filtered.filter { it.category.equals(category, ignoreCase = true) }
                    Result.success(filtered)
                } else {
                    val errorMsg = response.body()?.message ?: "Không thể tải danh sách bài học"
                    Result.failure(Exception(errorMsg))
                }
            }
        } catch (e: Exception) {
            val cached = LessonCacheManager.getCachedLessons()
            if (!cached.isNullOrEmpty()) {
                var filtered = cached
                if (!level.isNullOrBlank()) filtered = filtered.filter { it.level.equals(level, ignoreCase = true) }
                if (!category.isNullOrBlank()) filtered = filtered.filter { it.category.equals(category, ignoreCase = true) }
                Result.success(filtered)
            } else {
                Result.failure(e)
            }
        }
    }

    override suspend fun getRecommendedLessons(): Result<List<LessonDto>> {
        return try {
            val response = lessonApiService.getRecommendedLessons()
            if (response.isSuccessful && response.body()?.data != null) {
                val data = response.body()!!.data!!
                LessonCacheManager.cacheRecommendedLessons(data)
                Result.success(data)
            } else {
                val cached = LessonCacheManager.getCachedRecommendedLessons()
                if (!cached.isNullOrEmpty()) {
                    Result.success(cached)
                } else {
                    val errorMsg = response.body()?.message ?: "Không thể tải danh sách bài học đề xuất"
                    Result.failure(Exception(errorMsg))
                }
            }
        } catch (e: Exception) {
            val cached = LessonCacheManager.getCachedRecommendedLessons()
            if (!cached.isNullOrEmpty()) {
                Result.success(cached)
            } else {
                Result.failure(e)
            }
        }
    }

    override suspend fun getLessonById(id: String): Result<LessonDto> {
        return try {
            val response = lessonApiService.getLessonById(id)
            if (response.isSuccessful && response.body()?.data != null) {
                val data = response.body()!!.data!!
                LessonCacheManager.cacheLessonDetail(data)
                Result.success(data)
            } else {
                val cached = LessonCacheManager.getCachedLessonDetail(id)
                if (cached != null) {
                    Result.success(cached)
                } else {
                    val errorMsg = response.body()?.message ?: "Không tìm thấy bài học"
                    Result.failure(Exception(errorMsg))
                }
            }
        } catch (e: Exception) {
            val cached = LessonCacheManager.getCachedLessonDetail(id)
            if (cached != null) {
                Result.success(cached)
            } else {
                Result.failure(e)
            }
        }
    }

    override suspend fun completeLesson(id: String, stars: Int): Result<LessonDto> {
        return try {
            val response = lessonApiService.completeLesson(id, stars)
            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!)
            } else {
                val errorMsg = response.body()?.message ?: "Không thể ghi nhận hoàn thành bài học"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getExamQuestions(level: String): Result<List<com.example.luminalearn.data.model.ExamQuestionDto>> {
        return try {
            val response = lessonApiService.getExamQuestions(level)
            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!)
            } else {
                val errorMsg = response.body()?.message ?: "Không thể tải danh sách câu hỏi thi"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun claimChestReward(unitNumber: Int): Result<com.example.luminalearn.data.model.ChestRewardDto> {
        return try {
            val response = lessonApiService.claimChestReward(unitNumber)
            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!)
            } else {
                val errorMsg = response.body()?.message ?: "Không thể mở rương phần thưởng"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getJourneyUnits(level: String): Result<List<com.example.luminalearn.data.model.JourneyUnitDto>> {
        return try {
            val response = lessonApiService.getJourneyUnits(level)
            if (response.isSuccessful && response.body()?.data != null) {
                val data = response.body()!!.data!!
                LessonCacheManager.cacheUnits(data)
                Result.success(data)
            } else {
                val cached = LessonCacheManager.getCachedUnits()
                if (!cached.isNullOrEmpty()) {
                    Result.success(cached)
                } else {
                    val errorMsg = response.body()?.message ?: "Không thể tải thông tin các Unit"
                    Result.failure(Exception(errorMsg))
                }
            }
        } catch (e: Exception) {
            val cached = LessonCacheManager.getCachedUnits()
            if (!cached.isNullOrEmpty()) {
                Result.success(cached)
            } else {
                Result.failure(e)
            }
        }
    }

    override suspend fun getReviewQuestions(id: String): Result<List<com.example.luminalearn.data.model.QuickReviewQuestionDto>> {
        return try {
            val response = lessonApiService.getReviewQuestions(id)
            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!)
            } else {
                val errorMsg = response.body()?.message ?: "Không thể tải câu hỏi ôn tập"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun submitReview(id: String): Result<com.example.luminalearn.data.model.ReviewResultDto> {
        return try {
            val response = lessonApiService.submitReview(id)
            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!)
            } else {
                val errorMsg = response.body()?.message ?: "Không thể hoàn thành ôn tập"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getHeartStatus(): Result<com.example.luminalearn.data.model.HeartStatusDto> {
        return try {
            val response = lessonApiService.getHeartStatus()
            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!)
            } else {
                val errorMsg = response.body()?.message ?: "Không thể lấy thông tin trái tim"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun consumeHeart(): Result<com.example.luminalearn.data.model.HeartStatusDto> {
        return try {
            val response = lessonApiService.consumeHeart()
            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!)
            } else {
                val errorMsg = response.body()?.message ?: "Không thể tiêu hao trái tim"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun refillHearts(): Result<com.example.luminalearn.data.model.HeartStatusDto> {
        return try {
            val response = lessonApiService.refillHearts()
            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!)
            } else {
                val errorMsg = response.body()?.message ?: "Không thể nạp trái tim"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getWeeklyLeaderboard(): Result<com.example.luminalearn.data.model.LeaderboardDto> {
        return try {
            val response = lessonApiService.getWeeklyLeaderboard()
            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!)
            } else {
                val errorMsg = response.body()?.message ?: "Không thể tải bảng xếp hạng tuần"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserAnalytics(): Result<com.example.luminalearn.data.model.AnalyticsSummaryDto> {
        return try {
            val response = lessonApiService.getUserAnalytics()
            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!)
            } else {
                val errorMsg = response.body()?.message ?: "Không thể tải phân tích học tập"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getGrammarStructures(level: String?): Result<List<com.example.luminalearn.data.model.GrammarStructureDto>> {
        return try {
            val response = lessonApiService.getGrammarStructures(level)
            if (response.isSuccessful && response.body()?.data != null) {
                val data = response.body()!!.data!!
                if (level == null) {
                    LessonCacheManager.cacheGrammars(data)
                }
                Result.success(data)
            } else {
                val cached = LessonCacheManager.getCachedGrammars()
                if (!cached.isNullOrEmpty()) {
                    Result.success(cached)
                } else {
                    val errorMsg = response.body()?.message ?: "Không thể tải danh sách ngữ pháp"
                    Result.failure(Exception(errorMsg))
                }
            }
        } catch (e: Exception) {
            val cached = LessonCacheManager.getCachedGrammars()
            if (!cached.isNullOrEmpty()) {
                Result.success(cached)
            } else {
                Result.failure(e)
            }
        }
    }
}

