package com.example.luminalearn.presentation.lesson

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedHskLevel: String = "all",
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
                    id = "all",
                    title = "Tất cả cấp",
                    lessonCount = allLessons.size,
                    tagBgColor = Color(0xFF5C50F6),
                    tagTextColor = Color.White
                ),
                HskLevelCardData(
                    id = "HSK 1",
                    title = "HSK 1",
                    lessonCount = if (hsk1Count > 0) hsk1Count else 3,
                    maxScore = "Thang 200đ",
                    passScore = "Đỗ: 120đ",
                    tagBgColor = Color(0xFFE0F2FE),
                    tagTextColor = Color(0xFF0284C7)
                ),
                HskLevelCardData(
                    id = "HSK 2",
                    title = "HSK 2",
                    lessonCount = if (hsk2Count > 0) hsk2Count else 3,
                    maxScore = "Thang 200đ",
                    passScore = "Đỗ: 120đ",
                    tagBgColor = Color(0xFFDCFCE7),
                    tagTextColor = Color(0xFF16A34A)
                ),
                HskLevelCardData(
                    id = "HSK 3",
                    title = "HSK 3",
                    lessonCount = if (hsk3Count > 0) hsk3Count else 3,
                    maxScore = "Thang 300đ",
                    passScore = "Đỗ: 180đ",
                    tagBgColor = Color(0xFFFFEDD5),
                    tagTextColor = Color(0xFFEA580C)
                ),
                HskLevelCardData(
                    id = "HSK 4",
                    title = "HSK 4",
                    lessonCount = if (hsk4Count > 0) hsk4Count else 2,
                    maxScore = "Thang 300đ",
                    passScore = "Đỗ: 180đ",
                    tagBgColor = Color(0xFFFEF3C7),
                    tagTextColor = Color(0xFFD97706)
                ),
                HskLevelCardData(
                    id = "HSK 5",
                    title = "HSK 5",
                    lessonCount = if (hsk5Count > 0) hsk5Count else 1,
                    maxScore = "Thang 300đ",
                    passScore = "Đỗ: 180đ",
                    tagBgColor = Color(0xFFFCE7F3),
                    tagTextColor = Color(0xFFBE185D)
                ),
                HskLevelCardData(
                    id = "HSK 6",
                    title = "HSK 6",
                    lessonCount = if (hsk6Count > 0) hsk6Count else 1,
                    maxScore = "Thang 300đ",
                    passScore = "Đỗ: 180đ",
                    tagBgColor = Color(0xFFFFE4E6),
                    tagTextColor = Color(0xFFE11D48)
                )
            )
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
    }

    fun loadLessons() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = lessonRepository.getAllLessons()
            result.onSuccess { dtoList ->
                val lessons = dtoList.map { it.toChineseLessonData() }
                _uiState.update {
                    it.copy(
                        allLessons = lessons,
                        isLoading = false,
                        errorMessage = null,
                        currentPage = 1
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.localizedMessage ?: "Lỗi tải bài học"
                    )
                }
            }
        }
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
}
