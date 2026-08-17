package com.example.luminalearn.presentation.login

import com.example.luminalearn.core.base.BaseViewModel

/**
 * LoginViewModel xử lý logic đăng nhập theo pattern MVI.
 */
class LoginViewModel : BaseViewModel<LoginUiState, LoginUiIntent, LoginUiEffect>(
    initialState = LoginUiState()
) {

    override fun handleIntent(intent: LoginUiIntent) {
        when (intent) {
            is LoginUiIntent.EmailChanged -> {
                setState { copy(email = intent.email, emailError = null) }
            }
            is LoginUiIntent.PasswordChanged -> {
                setState { copy(password = intent.password, passwordError = null) }
            }
            is LoginUiIntent.LoginClicked -> {
                handleLogin()
            }
        }
    }

    private fun handleLogin() {
        val email = currentState.email.trim()
        val password = currentState.password

        // Validate đơn giản
        if (email.isBlank()) {
            setState { copy(emailError = "Email không được để trống") }
            return
        }
        if (password.isBlank()) {
            setState { copy(passwordError = "Mật khẩu không được để trống") }
            return
        }
        setState { copy(isLoading = true) }
        setEffect(LoginUiEffect.NavigateToMain)
    }
}
