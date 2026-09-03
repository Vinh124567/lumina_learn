package com.example.luminalearn.presentation.spark_ai_lab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.luminalearn.presentation.spark_ai_lab.component.SparkAiHeader
import com.example.luminalearn.presentation.spark_ai_lab.component.SparkPromptCard

@Composable
fun SparkAILab(
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var promptText by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .statusBarsPadding()
            .verticalScroll(scrollState)
            .padding(20.dp)
    ) {
        SparkAiHeader()

        Spacer(modifier = Modifier.height(20.dp))

        SparkPromptCard(
            prompt = promptText,
            onPromptChange = { promptText = it },
            onGenerateClick = {
                // Xử lý generate
            }
        )

        Spacer(modifier = Modifier.height(96.dp))
    }
}