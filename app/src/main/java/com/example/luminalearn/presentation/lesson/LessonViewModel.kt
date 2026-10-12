package com.example.luminalearn.presentation.lesson

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luminalearn.data.local.LessonCacheManager
import com.example.luminalearn.data.model.toChineseLessonData
import com.example.luminalearn.data.repository.LessonRepository
import com.example.luminalearn.data.repository.LessonRepositoryImpl
import com.example.luminalearn.presentation.lesson.component.ChineseLessonData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HskLevelCardData(
    val id: String,
    val title: String,
    val lessonCount: Int,
    val maxScore: String? = null,
    val passScore: String? = null,
    val tagBgColor: Color,
    val tagTextColor: Color
)

const val LESSON_PAGE_SIZE = 4

data class LessonUiState(
    val allLessons: List<ChineseLessonData> = emptyList(),
    val backendUnits: List<com.example.luminalearn.data.model.JourneyUnitDto> = emptyList(),
    val heartStatus: com.example.luminalearn.data.model.HeartStatusDto = com.example.luminalearn.data.model.HeartStatusDto(),
    val leaderboard: com.example.luminalearn.data.model.LeaderboardDto? = null,
    val analytics: com.example.luminalearn.data.model.AnalyticsSummaryDto? = null,
    val grammarStructures: List<com.example.luminalearn.data.model.GrammarStructureDto> = emptyList(),
    val isGrammarLoading: Boolean = false,
    val isLoading: Boolean = false,
    val isOfflineMode: Boolean = false,
    val errorMessage: String? = null,
    val selectedHskLevel: String = "HSK 1",
    val selectedCategory: String = "Tất cả",
    val searchQuery: String = "",
    val currentPage: Int = 1,
    val activeLessonForDialog: ChineseLessonData? = null
) {
    val hskLevels: List<HskLevelCardData>
        get() {
            val hsk1Count = allLessons.count { it.level.equals("HSK 1", ignoreCase = true) }
            val hsk2Count = allLessons.count { it.level.equals("HSK 2", ignoreCase = true) }
            val hsk3Count = allLessons.count { it.level.equals("HSK 3", ignoreCase = true) }
            val hsk4Count = allLessons.count { it.level.equals("HSK 4", ignoreCase = true) }
            val hsk5Count = allLessons.count { it.level.equals("HSK 5", ignoreCase = true) }
            val hsk6Count = allLessons.count { it.level.equals("HSK 6", ignoreCase = true) }

            return listOf(
                HskLevelCardData(
                    id = "HSK 1",
                    title = "HSK 1",
                    lessonCount = if (hsk1Count > 0) hsk1Count else 5,
                    maxScore = "Thang 200đ",
                    passScore = "Đỗ: 120đ",
                    tagBgColor = Color(0xFFE0F2FE),
                    tagTextColor = Color(0xFF0284C7)
                ),
                HskLevelCardData(
                    id = "HSK 2",
                    title = "HSK 2",
                    lessonCount = if (hsk2Count > 0) hsk2Count else 4,
                    maxScore = "Thang 200đ",
                    passScore = "Đỗ: 120đ",
                    tagBgColor = Color(0xFFDCFCE7),
                    tagTextColor = Color(0xFF16A34A)
                ),
                HskLevelCardData(
                    id = "HSK 3",
                    title = "HSK 3",
                    lessonCount = if (hsk3Count > 0) hsk3Count else 4,
                    maxScore = "Thang 300đ",
                    passScore = "Đỗ: 180đ",
                    tagBgColor = Color(0xFFFFEDD5),
                    tagTextColor = Color(0xFFEA580C)
                ),
                HskLevelCardData(
                    id = "HSK 4",
                    title = "HSK 4",
                    lessonCount = if (hsk4Count > 0) hsk4Count else 3,
                    maxScore = "Thang 300đ",
                    passScore = "Đỗ: 180đ",
                    tagBgColor = Color(0xFFFEF3C7),
                    tagTextColor = Color(0xFFD97706)
                ),
                HskLevelCardData(
                    id = "HSK 5",
                    title = "HSK 5",
                    lessonCount = if (hsk5Count > 0) hsk5Count else 2,
                    maxScore = "Thang 300đ",
                    passScore = "Đỗ: 180đ",
                    tagBgColor = Color(0xFFFCE7F3),
                    tagTextColor = Color(0xFFBE185D)
                ),
                HskLevelCardData(
                    id = "HSK 6",
                    title = "HSK 6",
                    lessonCount = if (hsk6Count > 0) hsk6Count else 2,
                    maxScore = "Thang 300đ",
                    passScore = "Đỗ: 180đ",
                    tagBgColor = Color(0xFFFFE4E6),
                    tagTextColor = Color(0xFFE11D48)
                )
            )
        }

    val currentLevelLessons: List<ChineseLessonData>
        get() {
            val matched = allLessons.filter { it.level.equals(selectedHskLevel, ignoreCase = true) }
            return if (matched.isNotEmpty()) matched else allLessons
        }

    val completedCount: Int
        get() = currentLevelLessons.count { it.isCompleted }

    val totalCount: Int
        get() = currentLevelLessons.size

    val progressPercent: Int
        get() = if (totalCount > 0) ((completedCount.toFloat() / totalCount) * 100).toInt() else 0

    val currentLessonIndex: Int
        get() {
            val idx = currentLevelLessons.indexOfFirst { !it.isCompleted }
            return if (idx >= 0) idx else maxOf(0, currentLevelLessons.size - 1)
        }

    val filteredLessons: List<ChineseLessonData>
        get() {
            return allLessons.filter { lesson ->
                val matchesHsk = selectedHskLevel == "all" ||
                        lesson.level.equals(selectedHskLevel, ignoreCase = true)

                val matchesCategory = selectedCategory == "Tất cả" || selectedCategory == "All" ||
                        lesson.category.contains(selectedCategory, ignoreCase = true) ||
                        selectedCategory.contains(lesson.category, ignoreCase = true)

                val matchesQuery = searchQuery.isBlank() ||
                        lesson.title.contains(searchQuery, ignoreCase = true) ||
                        lesson.pinyinHanziTitle.contains(searchQuery, ignoreCase = true) ||
                        lesson.description.contains(searchQuery, ignoreCase = true)

                matchesHsk && matchesCategory && matchesQuery
            }
        }

    val totalPages: Int
        get() = maxOf(1, (filteredLessons.size + LESSON_PAGE_SIZE - 1) / LESSON_PAGE_SIZE)

    val safePage: Int
        get() = currentPage.coerceIn(1, totalPages)

    val pagedLessons: List<ChineseLessonData>
        get() {
            val start = (safePage - 1) * LESSON_PAGE_SIZE
            return filteredLessons.drop(start).take(LESSON_PAGE_SIZE)
        }
}

class LessonViewModel(
    private val lessonRepository: LessonRepository = LessonRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow(LessonUiState(isLoading = true))
    val uiState: StateFlow<LessonUiState> = _uiState.asStateFlow()

    init {
        loadLessons()
        loadHeartStatus()
        loadLeaderboard()
        loadAnalytics()
    }

    fun loadLessons() {
        viewModelScope.launch {
            // 1. Tải trước từ Offline Cache để hiển thị ngay lập tức (0ms delay)
            val cachedLessons = LessonCacheManager.getCachedLessons()
            val cachedUnits = LessonCacheManager.getCachedUnits()
            if (!cachedLessons.isNullOrEmpty()) {
                val cachedData = cachedLessons.map { dto ->
                    val data = dto.toChineseLessonData()
                    data.copy(needsReview = data.isCompleted && (data.stars in 1..2))
                }
                _uiState.update {
                    it.copy(
                        allLessons = cachedData,
                        backendUnits = cachedUnits ?: emptyList(),
                        isLoading = false,
                        isOfflineMode = true,
                        errorMessage = null
                    )
                }
            } else {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            }

            // 2. Đồng bộ ngầm từ API Backend
            val result = lessonRepository.getAllLessons()
            val unitsResult = lessonRepository.getJourneyUnits(_uiState.value.selectedHskLevel)
            val units = unitsResult.getOrNull() ?: emptyList()

            result.onSuccess { dtoList ->
                // Lưu vào Offline Cache cho các lần mở app sau
                LessonCacheManager.cacheLessons(dtoList)
                if (units.isNotEmpty()) {
                    LessonCacheManager.cacheUnits(units)
                }

                val lessons = dtoList.map { dto ->
                    val data = dto.toChineseLessonData()
                    data.copy(needsReview = data.isCompleted && (data.stars in 1..2))
                }
                _uiState.update {
                    it.copy(
                        allLessons = lessons,
                        backendUnits = units,
                        isLoading = false,
                        isOfflineMode = false,
                        errorMessage = null,
                        currentPage = 1
                    )
                }
            }.onFailure { error ->
                // Nếu đã có cache từ trước thì giữ trạng thái Offline Mode, không hiển thị lỗi gây chặn màn hình
                val hasCachedData = _uiState.value.allLessons.isNotEmpty()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isOfflineMode = hasCachedData,
                        errorMessage = if (hasCachedData) null else (error.localizedMessage ?: "Lỗi tải bài học")
                    )
                }
            }
        }
    }

    fun loadReviewQuestions(
        lessonId: String,
        onSuccess: (List<com.example.luminalearn.data.model.QuickReviewQuestionDto>) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val result = lessonRepository.getReviewQuestions(lessonId)
            result.onSuccess { questions ->
                onSuccess(questions)
            }.onFailure { error ->
                onError(error.localizedMessage ?: "Không thể tải câu hỏi ôn tập từ máy chủ")
            }
        }
    }

    fun submitReview(
        lessonId: String,
        onSuccess: (sparksBonus: Int, newStars: Int, message: String) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val result = lessonRepository.submitReview(lessonId)
            result.onSuccess { res ->
                _uiState.update { state ->
                    val updated = state.allLessons.map { lesson ->
                        if (lesson.id == lessonId) {
                            lesson.copy(
                                isCompleted = true,
                                stars = res.newStars,
                                sparks = lesson.sparks + res.sparksBonus,
                                needsReview = false
                            )
                        } else lesson
                    }
                    state.copy(allLessons = updated)
                }
                onSuccess(res.sparksBonus, res.newStars, res.message)
            }.onFailure { error ->
                onError(error.localizedMessage ?: "Không thể gửi kết quả ôn tập lên máy chủ")
            }
        }
    }

    fun completeQuickReview(lessonId: String, sparksBonus: Int, newStars: Int = 3) {
        submitReview(
            lessonId = lessonId,
            onSuccess = { _, _, _ -> },
            onError = { }
        )
    }

    fun selectHskLevel(level: String) {
        if (_uiState.value.selectedHskLevel == level) return
        _uiState.update { it.copy(selectedHskLevel = level, currentPage = 1) }
    }

    fun selectCategory(category: String) {
        if (_uiState.value.selectedCategory == category) return
        _uiState.update { it.copy(selectedCategory = category, currentPage = 1) }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query, currentPage = 1) }
    }

    fun changePage(page: Int) {
        _uiState.update { it.copy(currentPage = page) }
    }

    fun openLessonDialog(lesson: ChineseLessonData) {
        _uiState.update { it.copy(activeLessonForDialog = lesson) }
    }

    fun dismissLessonDialog() {
        _uiState.update { it.copy(activeLessonForDialog = null) }
    }

    fun completeLesson(lessonId: String, stars: Int = 3) {
        val earnedStars = stars.coerceIn(1, 3)
        _uiState.update { state ->
            val updated = state.allLessons.map { lesson ->
                if (lesson.id == lessonId) {
                    lesson.copy(
                        isCompleted = true,
                        stars = maxOf(lesson.stars, earnedStars)
                    )
                } else lesson
            }
            state.copy(allLessons = updated)
        }

        viewModelScope.launch {
            lessonRepository.completeLesson(lessonId, earnedStars)
        }
    }

    fun loadExamQuestions(
        level: String,
        onSuccess: (List<com.example.luminalearn.presentation.lesson.component.BossQuestion>) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val result = lessonRepository.getExamQuestions(level)
            result.onSuccess { dtoList ->
                val questions = dtoList.map { dto ->
                    com.example.luminalearn.presentation.lesson.component.BossQuestion(
                        id = dto.id,
                        typeTitle = dto.typeTitle,
                        question = dto.question,
                        pinyinHint = dto.pinyinHint,
                        options = dto.options,
                        correctIndex = dto.correctIndex,
                        explanation = dto.explanation
                    )
                }
                onSuccess(questions)
            }.onFailure { error ->
                onError(error.localizedMessage ?: "Không thể tải đề thi từ máy chủ")
            }
        }
    }

    fun claimChest(
        unitNumber: Int,
        onSuccess: (sparks: Int, exp: Int) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val result = lessonRepository.claimChestReward(unitNumber)
            result.onSuccess { dto ->
                onSuccess(dto.sparksEarned, dto.expEarned)
            }.onFailure { error ->
                onError(error.localizedMessage ?: "Không thể mở rương")
            }
        }
    }

    fun loadHeartStatus() {
        viewModelScope.launch {
            val result = lessonRepository.getHeartStatus()
            result.onSuccess { status ->
                _uiState.update { it.copy(heartStatus = status) }
            }
        }
    }

    fun consumeHeart(onDepleted: (() -> Unit)? = null) {
        viewModelScope.launch {
            val result = lessonRepository.consumeHeart()
            result.onSuccess { status ->
                _uiState.update { it.copy(heartStatus = status) }
                if (status.currentHearts <= 0) {
                    onDepleted?.invoke()
                }
            }
        }
    }

    fun refillHearts(onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val result = lessonRepository.refillHearts()
            result.onSuccess { status ->
                _uiState.update { it.copy(heartStatus = status) }
                onSuccess(status.message.ifBlank { "Đã nạp đầy 5 trái tim thành công!" })
            }.onFailure { error ->
                onError(error.localizedMessage ?: "Không thể nạp trái tim")
            }
        }
    }

    fun loadLeaderboard() {
        viewModelScope.launch {
            val result = lessonRepository.getWeeklyLeaderboard()
            result.onSuccess { lb ->
                _uiState.update { it.copy(leaderboard = lb) }
            }
        }
    }

    fun loadAnalytics() {
        viewModelScope.launch {
            val result = lessonRepository.getUserAnalytics()
            result.onSuccess { an ->
                _uiState.update { it.copy(analytics = an) }
            }
        }
    }

    fun loadGrammarStructures(level: String? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isGrammarLoading = true) }
            val result = lessonRepository.getGrammarStructures(level)
            result.onSuccess { list ->
                _uiState.update { it.copy(grammarStructures = list, isGrammarLoading = false) }
            }.onFailure {
                _uiState.update { it.copy(isGrammarLoading = false) }
            }
        }
    }
}

