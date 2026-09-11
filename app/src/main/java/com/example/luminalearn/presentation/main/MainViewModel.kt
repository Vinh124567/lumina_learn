package com.example.luminalearn.presentation.main

import androidx.lifecycle.viewModelScope
import com.example.luminalearn.core.base.BaseViewModel
import com.example.luminalearn.data.model.toToneCardData
import com.example.luminalearn.data.repository.LessonRepository
import com.example.luminalearn.data.repository.LessonRepositoryImpl
import kotlinx.coroutines.launch

/**
 * MainViewModel triển khai logic xử lý MVI cho màn hình Main.
 */
class MainViewModel(
    private val lessonRepository: LessonRepository = LessonRepositoryImpl()
) : BaseViewModel<MainUiState, MainUiIntent, MainUiEffect>(
    initialState = MainUiState()
) {

    init {
        loadRecommendedLessons()
    }

    override fun handleIntent(intent: MainUiIntent) {
        when (intent) {
            is MainUiIntent.IncrementCounter -> {
                setState { copy(counter = counter + 1) }
            }
            is MainUiIntent.DecrementCounter -> {
                setState { copy(counter = counter - 1) }
            }
            is MainUiIntent.ResetCounter -> {
                setState { copy(counter = 0) }
            }
            is MainUiIntent.ShowToastRequested -> {
                setEffect(MainUiEffect.ShowToast("Giá trị counter hiện tại là: ${currentState.counter}"))
            }
            is MainUiIntent.LoadRecommendedLessons -> {
                loadRecommendedLessons()
            }
            is MainUiIntent.LoadLesson -> {
                loadLesson(intent.lessonId)
            }
            is MainUiIntent.ClearLesson -> {
                setState { copy(lessonSlides = emptyList()) }
            }
        }
    }

    fun loadRecommendedLessons() {
        viewModelScope.launch {
            setState { copy(isRecommendedLessonsLoading = true) }
            lessonRepository.getRecommendedLessons()
                .onSuccess { lessons ->
                    setState {
                        copy(
                            isRecommendedLessonsLoading = false,
                            recommendedLessons = lessons
                        )
                    }
                }
                .onFailure { error ->
                    setState { copy(isRecommendedLessonsLoading = false) }
                    setEffect(MainUiEffect.ShowToast(error.message ?: "Không thể tải danh sách bài học"))
                }
        }
    }

    fun loadLesson(lessonId: String = "lesson_pinyin_1") {
        viewModelScope.launch {
            setState { copy(isLessonLoading = true) }
            lessonRepository.getLessonById(lessonId)
                .onSuccess { lessonDto ->
                    val slides = lessonDto.slides.map { it.toToneCardData() }
                    setState {
                        copy(
                            isLessonLoading = false,
                            lessonSlides = slides
                        )
                    }
                }
                .onFailure { error ->
                    setState { copy(isLessonLoading = false) }
                    setEffect(MainUiEffect.ShowToast(error.message ?: "Không thể tải bài học"))
                }
        }
    }

    fun clearLesson() {
        setState { copy(lessonSlides = emptyList()) }
    }
}
