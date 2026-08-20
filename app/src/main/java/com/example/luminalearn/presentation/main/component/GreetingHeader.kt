package com.example.luminalearn.presentation.main.component

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier

@Composable
fun GreetingHeader() {
    Column {
        Box(
            modifier = Modifier.clip(CircleShape)
                .background(Color(0xFF5C50F6).copy(alpha = 0.1f))
        ) {

        }
    }
}