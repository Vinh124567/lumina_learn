package com.example.luminalearn.presentation.vocabulary

import com.example.luminalearn.core.base.UiEffect
import com.example.luminalearn.core.base.UiIntent
import com.example.luminalearn.core.base.UiState
import com.example.luminalearn.presentation.vocabulary.model.HskLevelFilter
import com.example.luminalearn.presentation.vocabulary.model.TopicItem
import com.example.luminalearn.presentation.vocabulary.model.VocabConstants
import com.example.luminalearn.presentation.vocabulary.model.VocabSourceFilter
import com.example.luminalearn.presentation.vocabulary.model.VocabStudyMode
import com.example.luminalearn.presentation.vocabulary.model.VocabWordItem

private const val PAGE_SIZE = 15

/**
 * State duy nhất cho màn hình Từ vựng (Unidirectional Data Flow)
 */
data class VocabularyUiState(
    val hskLevels: List<HskLevelFilter> = listOf(HskLevelFilter(VocabConstants.ALL_LEVELS)),
    val selectedHskIndex: Int = 0,
    val selectedSource: VocabSourceFilter = VocabSourceFilter.ALL,
    val topics: List<TopicItem> = listOf(
        TopicItem(
            id = VocabConstants.TOPIC_ALL_ID,
            name = VocabConstants.ALL_TOPICS,
            icon = "🌐",
            count = 0
        ),
        TopicItem(
            id = "custom",
            name = "Từ tôi tự thêm",
            icon = "✍️",
            count = 0
        )
    ),
    val selectedTopic: String = VocabConstants.ALL_TOPICS,
    val vocabList: List<VocabWordItem> = emptyList(),
    val selectedMode: VocabStudyMode = VocabStudyMode.LIST,
    val searchQuery: String = "",
    val currentPage: Int = 1,
    val currentFlashcardIndex: Int = 0,
    val isCardFlipped: Boolean = false,
    val isLoadingVocab: Boolean = false,
    val isLevelsLoading: Boolean = false,
    val isTopicsLoading: Boolean = false,
    val quizState: ReflexQuizState = ReflexQuizState(),
    val activeDetailWord: VocabWordItem? = null,
    val activeDetailTab: Int = 0,
    val isAddVocabSheetOpen: Boolean = false,
    val isSubmittingVocab: Boolean = false,
    val isAiLookingUp: Boolean = false
) : UiState {

    val selectedHskTitle: String?
        get() = hskLevels.getOrNull(selectedHskIndex)?.title?.let {
            if (it == VocabConstants.ALL_LEVELS) null else it
        }

    val filteredList: List<VocabWordItem>
        get() = vocabList.filter { item ->
            val matchQuery = searchQuery.isBlank() ||
                    item.hanzi.contains(searchQuery, ignoreCase = true) ||
                    item.pinyin.contains(searchQuery, ignoreCase = true) ||
                    item.hanViet.contains(searchQuery, ignoreCase = true) ||
                    item.meaning.contains(searchQuery, ignoreCase = true)
            val matchTopic = selectedTopic == VocabConstants.ALL_TOPICS ||
                    selectedTopic == "Tất cả chủ đề" ||
                    selectedTopic.equals("all", ignoreCase = true) ||
                    item.matchesCategory(selectedTopic)
            val matchSource = when (selectedSource) {
                VocabSourceFilter.ALL -> true
                VocabSourceFilter.DUE_TODAY -> item.isDueToday
                VocabSourceFilter.CUSTOM -> item.isCustom
                VocabSourceFilter.MASTERED -> item.isMastered
            }
            matchQuery && matchTopic && matchSource
        }

    val totalCount: Int
        get() = vocabList.size

    val dueTodayCount: Int
        get() = vocabList.count { it.isDueToday }

    val masteredCount: Int
        get() = vocabList.count { it.isMastered }

    val progressPercent: Int
        get() = if (totalCount > 0) (masteredCount * 100 / totalCount) else 0

    val totalPages: Int
        get() = maxOf(1, (filteredList.size + PAGE_SIZE - 1) / PAGE_SIZE)

    val safePage: Int
        get() = currentPage.coerceIn(1, totalPages)

    val pagedList: List<VocabWordItem>
        get() {
            val start = (safePage - 1) * PAGE_SIZE
            return filteredList.drop(start).take(PAGE_SIZE)
        }

    val safeFlashcardIndex: Int
        get() = if (filteredList.isEmpty()) 0 else currentFlashcardIndex.coerceIn(0, filteredList.size - 1)

    val currentFlashcardWord: VocabWordItem?
        get() = filteredList.getOrNull(safeFlashcardIndex)
}

/**
 * Dữ liệu cho một câu hỏi trắc nghiệm phản xạ
 */
data class ReflexQuizQuestion(
    val word: VocabWordItem,
    val options: List<String>,
    val correctAnswer: String
)

/**
 * Trạng thái của bài luyện phản xạ
 */
data class ReflexQuizState(
    val questions: List<ReflexQuizQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val score: Int = 0,
    val correctCount: Int = 0,
    val selectedOption: String? = null,
    val isAnswerChecked: Boolean = false,
    val isFinished: Boolean = false
) {
    val currentQuestion: ReflexQuizQuestion?
        get() = questions.getOrNull(currentIndex)
}

/**
 * Các Intent từ phía View gửi tới ViewModel
 */
sealed interface VocabularyUiIntent : UiIntent {
    data object LoadInitialData : VocabularyUiIntent
    data class SelectSource(val source: VocabSourceFilter) : VocabularyUiIntent
    data class SelectHskLevel(val index: Int) : VocabularyUiIntent
    data class SelectTopic(val topicName: String) : VocabularyUiIntent
    data class SelectMode(val mode: VocabStudyMode) : VocabularyUiIntent
    data class UpdateSearchQuery(val query: String) : VocabularyUiIntent
    data class ChangePage(val page: Int) : VocabularyUiIntent
    data class ToggleMastered(val wordId: String) : VocabularyUiIntent
    data object FlipFlashcard : VocabularyUiIntent
    data object NextFlashcard : VocabularyUiIntent
    data object PreviousFlashcard : VocabularyUiIntent
    data object StartReflexQuiz : VocabularyUiIntent
    data class SelectQuizOption(val option: String) : VocabularyUiIntent
    data object RestartReflexQuiz : VocabularyUiIntent
    data class OpenWordDetail(val word: VocabWordItem, val initialTab: Int = 0) : VocabularyUiIntent
    data object DismissWordDetail : VocabularyUiIntent
    data class SetAddVocabSheetVisible(val visible: Boolean) : VocabularyUiIntent
    data class AddNewVocabulary(val request: com.example.luminalearn.data.model.CreateVocabularyRequest, val onSuccess: () -> Unit) : VocabularyUiIntent
    data class RateWordSrs(val wordId: String, val rating: com.example.luminalearn.presentation.vocabulary.model.SrsRating) : VocabularyUiIntent
}

/**
 * Side-effects 1 lần (Toast, Thông báo)
 */
sealed interface VocabularyUiEffect : UiEffect {
    data class ShowToast(val message: String) : VocabularyUiEffect
}
