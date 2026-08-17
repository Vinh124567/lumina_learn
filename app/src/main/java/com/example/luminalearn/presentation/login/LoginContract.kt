package com.example.luminalearn.presentation.login

import com.example.luminalearn.core.base.UiEffect
import com.example.luminalearn.core.base.UiIntent
import com.example.luminalearn.core.base.UiState

/**
 * State màn hình Login.
 */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null
) : UiState


sealed interface LoginUiIntent : UiIntent {
    data class EmailChanged(val email: String) : LoginUiIntent
    data class PasswordChanged(val password: String) : LoginUiIntent
    data object LoginClicked : LoginUiIntent
}

/**
 * Side-Effect của màn hình Login.
 */
sealed interface LoginUiEffect : UiEffect {
    data object NavigateToMain : LoginUiEffect
    data class ShowError(val message: String) : LoginUiEffect
}
