package com.example.luminalearn.utils

import android.util.Patterns

object ValidationUtils {
    fun validateField(
        value: String,
        blankMessage: String,
        invalidMessage: String
    ): String? {
        val errorMessage = when {
            value.isEmpty() -> blankMessage
            !Patterns.EMAIL_ADDRESS.matcher(value).matches() -> invalidMessage
            else -> null
        }
        return errorMessage
    }
}