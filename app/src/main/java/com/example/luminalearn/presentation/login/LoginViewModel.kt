package com.example.luminalearn.presentation.login

import com.example.luminalearn.core.base.BaseViewModel
import com.example.luminalearn.utils.ValidationUtils

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
        val email = currentState.email
        val password = currentState.password
        val emailError = ValidationUtils.validateField(
            value = email,
            blankMessage = "The email field cannot be left blank.",
            invalidMessage = "Invalid email"
        )
        val passwordError = ValidationUtils.validateField(
            value = password,
            blankMessage = "The password field cannot be left blank.",
            invalidMessage = "Invalid password"
        )
        setState { copy(emailError = emailError, passwordError = passwordError) }
        setState { copy(isLoading = true) }
        setEffect(LoginUiEffect.NavigateToMain)
    }
}

