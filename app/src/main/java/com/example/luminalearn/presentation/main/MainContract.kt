package com.example.luminalearn.presentation.main

import com.example.luminalearn.core.base.UiEffect
import com.example.luminalearn.core.base.UiIntent
import com.example.luminalearn.core.base.UiState

/**
 * State duy nhất đại diện cho trạng thái màn hình Main.
 */
data class MainUiState(
    val counter: Int = 0,
    val isLoading: Boolean = false,
    val message: String = "Chào mừng bạn đến với LuminaLearn MVI Base Project!"
) : UiState

/**
 * Các Intent đại diện cho hành động người dùng gửi từ View.
 */
sealed interface MainUiIntent : UiIntent {
    data object IncrementCounter : MainUiIntent
    data object DecrementCounter : MainUiIntent
    data object ResetCounter : MainUiIntent
    data object ShowToastRequested : MainUiIntent
}

/**
 * Các Side-Effect (Toast, Dialog, Navigation) chỉ xảy ra 1 lần.
 */
sealed interface MainUiEffect : UiEffect {
    data class ShowToast(val message: String) : MainUiEffect
}
