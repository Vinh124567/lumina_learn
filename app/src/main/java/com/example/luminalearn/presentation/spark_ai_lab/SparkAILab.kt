package com.example.luminalearn.presentation.spark_ai_lab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.presentation.main.component.TopBar
import com.example.luminalearn.presentation.spark_ai_lab.component.DEFAULT_SCENARIOS
import com.example.luminalearn.presentation.spark_ai_lab.component.RoleplayFilterAndSearchBar
import com.example.luminalearn.presentation.spark_ai_lab.component.RoleplayPracticeDialog
import com.example.luminalearn.presentation.spark_ai_lab.component.RoleplayScenarioCard
import com.example.luminalearn.presentation.spark_ai_lab.component.RoleplayScenarioData
import com.example.luminalearn.presentation.spark_ai_lab.component.RoleplayStudioBanner
import com.example.luminalearn.presentation.spark_ai_lab.component.SparkAiHeader
import com.example.luminalearn.presentation.spark_ai_lab.component.SparkAiMode
import com.example.luminalearn.presentation.spark_ai_lab.component.SparkAiModeTabs
import com.example.luminalearn.presentation.spark_ai_lab.component.SparkPromptCard

@Composable
fun SparkAILab(
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var selectedMode by remember { mutableStateOf(SparkAiMode.ROLEPLAY) }
    var selectedFilterId by remember { mutableStateOf("all") }
    var searchQuery by remember { mutableStateOf("") }
    var promptText by remember { mutableStateOf("") }
    var activeScenario by remember { mutableStateOf<RoleplayScenarioData?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .statusBarsPadding()
    ) {
        TopBar(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
            streakDays = 7,
            points = 445
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            SparkAiHeader()

            Spacer(modifier = Modifier.height(14.dp))

            SparkAiModeTabs(
                selectedMode = selectedMode,
                onSelectMode = { selectedMode = it }
            )

            Spacer(modifier = Modifier.height(14.dp))

            when (selectedMode) {
                SparkAiMode.ROLEPLAY -> {
                    RoleplaySection(
                        selectedFilterId = selectedFilterId,
                        onSelectFilter = { selectedFilterId = it },
                        searchQuery = searchQuery,
                        onSearchChange = { searchQuery = it },
                        onStartScenario = { activeScenario = it }
                    )
                }
                SparkAiMode.MICRO_PROMPT -> {
                    SparkPromptCard(
                        prompt = promptText,
                        onPromptChange = { promptText = it },
                        onGenerateClick = { /* Handle generate */ }
                    )
                }
                SparkAiMode.REFLEX_QUIZ -> {
                    ReflexQuizSection()
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }

    if (activeScenario != null) {
        RoleplayPracticeDialog(
            scenario = activeScenario!!,
            onDismiss = { activeScenario = null }
        )
    }
}

@Composable
private fun RoleplaySection(
    selectedFilterId: String,
    onSelectFilter: (String) -> Unit,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onStartScenario: (RoleplayScenarioData) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        RoleplayStudioBanner()

        Spacer(modifier = Modifier.height(14.dp))

        RoleplayFilterAndSearchBar(
            selectedFilterId = selectedFilterId,
            onSelectFilter = onSelectFilter,
            searchQuery = searchQuery,
            onSearchChange = onSearchChange
        )

        Spacer(modifier = Modifier.height(12.dp))

        val filteredScenarios = remember(selectedFilterId, searchQuery) {
            DEFAULT_SCENARIOS.filter { scenario ->
                val matchesFilter = selectedFilterId == "all" || scenario.levelCategory == selectedFilterId
                val matchesQuery = searchQuery.isBlank() ||
                        scenario.title.contains(searchQuery, ignoreCase = true) ||
                        scenario.pinyinTitle.contains(searchQuery, ignoreCase = true) ||
                        scenario.description.contains(searchQuery, ignoreCase = true) ||
                        scenario.aiRole.contains(searchQuery, ignoreCase = true) ||
                        scenario.userRole.contains(searchQuery, ignoreCase = true)
                matchesFilter && matchesQuery
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            filteredScenarios.forEach { scenario ->
                RoleplayScenarioCard(
                    scenario = scenario,
                    onStartClick = { onStartScenario(scenario) }
                )
            }
        }
    }
}

@Composable
private fun ReflexQuizSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp)
    ) {
        Text(
            text = "Thử thách trắc nghiệm phản xạ tiếng Trung",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E293B)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Luyện phản xạ nhanh trong 5 giây cho mỗi câu hỏi giao tiếp đời sống.",
            fontSize = 13.sp,
            color = Color(0xFF64748B)
        )
    }
}