package com.example.luminalearn.presentation.vocabulary

import androidx.lifecycle.viewModelScope
import com.example.luminalearn.core.base.BaseViewModel
import com.example.luminalearn.data.remote.RetrofitClient
import com.example.luminalearn.presentation.vocabulary.model.HskLevelFilter
import com.example.luminalearn.presentation.vocabulary.model.TopicItem
import com.example.luminalearn.presentation.vocabulary.model.VocabConstants
import com.example.luminalearn.presentation.vocabulary.model.VocabStudyMode
import com.example.luminalearn.presentation.vocabulary.model.VocabWordItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class VocabularyViewModel : BaseViewModel<VocabularyUiState, VocabularyUiIntent, VocabularyUiEffect>(
    VocabularyUiState()
) {

    init {
        processIntent(VocabularyUiIntent.LoadInitialData)
    }

    override fun handleIntent(intent: VocabularyUiIntent) {
        when (intent) {
            is VocabularyUiIntent.LoadInitialData -> loadInitialData()
            is VocabularyUiIntent.SelectHskLevel -> handleSelectHskLevel(intent.index)
            is VocabularyUiIntent.SelectTopic -> handleSelectTopic(intent.topicName)
            is VocabularyUiIntent.SelectMode -> handleSelectMode(intent.mode)
            is VocabularyUiIntent.UpdateSearchQuery -> handleUpdateSearchQuery(intent.query)
            is VocabularyUiIntent.ChangePage -> setState { copy(currentPage = intent.page) }
            is VocabularyUiIntent.ToggleMastered -> handleToggleMastered(intent.wordId)
            is VocabularyUiIntent.FlipFlashcard -> setState { copy(isCardFlipped = !isCardFlipped) }
            is VocabularyUiIntent.NextFlashcard -> handleNextFlashcard()
            is VocabularyUiIntent.PreviousFlashcard -> handlePrevFlashcard()
            is VocabularyUiIntent.StartReflexQuiz -> startReflexQuiz()
            is VocabularyUiIntent.RestartReflexQuiz -> startReflexQuiz()
            is VocabularyUiIntent.SelectQuizOption -> handleSelectQuizOption(intent.option)
            is VocabularyUiIntent.OpenWordDetail -> setState { copy(activeDetailWord = intent.word) }
            is VocabularyUiIntent.DismissWordDetail -> setState { copy(activeDetailWord = null) }
            is VocabularyUiIntent.SetAddVocabSheetVisible -> setState { copy(isAddVocabSheetOpen = intent.visible) }
            is VocabularyUiIntent.SelectSource -> handleSelectSource(intent.source)
            is VocabularyUiIntent.AddNewVocabulary -> handleAddNewVocabulary(intent.request, intent.onSuccess)
        }
    }

    private fun handleSelectSource(source: com.example.luminalearn.presentation.vocabulary.model.VocabSourceFilter) {
        if (currentState.selectedSource == source) return
        setState {
            copy(
                selectedSource = source,
                currentPage = 1,
                currentFlashcardIndex = 0,
                isCardFlipped = false
            )
        }
    }

    private fun loadInitialData() {
        fetchLevels()
        val currentHsk = currentState.selectedHskTitle
        fetchTopics(currentHsk)
        fetchVocabularies(currentState.selectedTopic, currentHsk)
    }

    private fun handleSelectHskLevel(index: Int) {
        if (currentState.selectedHskIndex == index) return
        setState {
            copy(
                selectedHskIndex = index,
                currentPage = 1,
                currentFlashcardIndex = 0,
                isCardFlipped = false
            )
        }
        val newHskTitle = currentState.selectedHskTitle
        fetchTopics(newHskTitle)
        fetchVocabularies(currentState.selectedTopic, newHskTitle)
    }

    private fun handleSelectTopic(topicName: String) {
        if (currentState.selectedTopic == topicName) return
        setState {
            copy(
                selectedTopic = topicName,
                currentPage = 1,
                currentFlashcardIndex = 0,
                isCardFlipped = false
            )
        }
        fetchVocabularies(topicName, currentState.selectedHskTitle)
    }

    private fun handleSelectMode(mode: VocabStudyMode) {
        setState {
            copy(
                selectedMode = mode,
                isCardFlipped = false
            )
        }
        if (mode == VocabStudyMode.REFLEX) {
            startReflexQuiz()
        }
    }

    private fun handleUpdateSearchQuery(query: String) {
        setState {
            copy(
                searchQuery = query,
                currentPage = 1,
                currentFlashcardIndex = 0,
                isCardFlipped = false
            )
        }
    }

    private fun handleToggleMastered(wordId: String) {
        val updatedList = currentState.vocabList.map { item ->
            if (item.id == wordId) item.copy(isMastered = !item.isMastered) else item
        }
        val updatedActive = if (currentState.activeDetailWord?.id == wordId) {
            currentState.activeDetailWord?.let { it.copy(isMastered = !it.isMastered) }
        } else {
            currentState.activeDetailWord
        }
        setState { copy(vocabList = updatedList, activeDetailWord = updatedActive) }

        viewModelScope.launch {
            try {
                RetrofitClient.vocabularyApiService.toggleMastered(wordId)
            } catch (_: Exception) {
            }
        }
    }

    private fun handleNextFlashcard() {
        val size = currentState.filteredList.size
        if (size == 0) return
        val nextIndex = if (currentState.currentFlashcardIndex < size - 1) {
            currentState.currentFlashcardIndex + 1
        } else {
            0
        }
        setState { copy(currentFlashcardIndex = nextIndex, isCardFlipped = false) }
    }

    private fun handlePrevFlashcard() {
        val size = currentState.filteredList.size
        if (size == 0) return
        val prevIndex = if (currentState.currentFlashcardIndex > 0) {
            currentState.currentFlashcardIndex - 1
        } else {
            size - 1
        }
        setState { copy(currentFlashcardIndex = prevIndex, isCardFlipped = false) }
    }

    private fun fetchLevels() {
        viewModelScope.launch {
            setState { copy(isLevelsLoading = true) }
            try {
                val response = RetrofitClient.vocabularyApiService.getLevels()
                if (response.isSuccessful && response.body()?.data != null) {
                    val remoteLevels = response.body()!!.data!!.map { dto ->
                        HskLevelFilter(title = dto.title, scoreRange = dto.scoreRange)
                    }
                    if (remoteLevels.isNotEmpty()) {
                        setState { copy(hskLevels = remoteLevels) }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                setState { copy(isLevelsLoading = false) }
            }
        }
    }

    private fun fetchTopics(hskLevel: String?) {
        viewModelScope.launch {
            setState { copy(isTopicsLoading = true) }
            try {
                val queryHsk = if (hskLevel == VocabConstants.ALL_LEVELS) null else hskLevel
                val response = RetrofitClient.vocabularyApiService.getTopics(hskLevel = queryHsk)
                if (response.isSuccessful && response.body()?.data != null) {
                    val remoteTopics = response.body()!!.data!!.map { dto ->
                        TopicItem(id = dto.id, name = dto.name, icon = dto.icon, count = dto.count)
                    }
                    if (remoteTopics.isNotEmpty()) {
                        val customCount = currentState.vocabList.count { it.isCustom }
                        val hasCustom = remoteTopics.any { it.id == "custom" || it.name.contains("tự thêm") }
                        val finalTopics = if (!hasCustom) {
                            val allIdx = remoteTopics.indexOfFirst { it.id == "all" }
                            val customItem = TopicItem(id = "custom", name = "Từ tôi tự thêm", icon = "✍️", count = customCount)
                            if (allIdx != -1) {
                                remoteTopics.toMutableList().apply { add(allIdx + 1, customItem) }
                            } else {
                                listOf(customItem) + remoteTopics
                            }
                        } else {
                            remoteTopics
                        }

                        val topicStillExists = finalTopics.any { it.name == currentState.selectedTopic }
                        val activeTopic = if (topicStillExists) currentState.selectedTopic else finalTopics.first().name
                        setState {
                            copy(
                                topics = finalTopics,
                                selectedTopic = activeTopic
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                setState { copy(isTopicsLoading = false) }
            }
        }
    }

    private fun fetchVocabularies(topicName: String, hskLevel: String?) {
        viewModelScope.launch {
            setState { copy(isLoadingVocab = true) }
            try {
                val isAllTopic = topicName == VocabConstants.ALL_TOPICS ||
                        topicName == "Tất cả chủ đề" ||
                        topicName.equals("all", ignoreCase = true)
                val isCustomTopic = topicName.contains("tự thêm") || topicName.contains("tôi tự thêm") || topicName.contains("từ của tôi") || topicName == "custom"
                val queryTopic = if (isAllTopic || isCustomTopic) null else topicName
                val queryHsk = if (hskLevel == VocabConstants.ALL_LEVELS) null else hskLevel
                val response = RetrofitClient.vocabularyApiService.getVocabularies(
                    topic = queryTopic,
                    hskLevel = queryHsk
                )
                if (response.isSuccessful && response.body()?.data != null) {
                    val remoteList = response.body()!!.data!!.map { dto ->
                        VocabWordItem(
                            id = dto.id,
                            hanzi = dto.hanzi,
                            pinyin = dto.pinyin,
                            hanViet = dto.hanViet,
                            meaning = dto.meaning,
                            partOfSpeech = dto.partOfSpeech,
                            topic = dto.topic,
                            radical = dto.radical,
                            strokes = dto.strokes,
                            exampleHanzi = dto.exampleHanzi,
                            examplePinyin = dto.examplePinyin,
                            exampleMeaning = dto.exampleMeaning,
                            hskLevel = dto.hskLevel,
                            targetScore = dto.targetScore,
                            isMastered = dto.isMastered,
                            userId = dto.userId
                        )
                    }
                    val customCount = remoteList.count { it.isCustom }
                    val updatedTopics = currentState.topics.map { topic ->
                        if (topic.id == "custom" || topic.name.contains("tự thêm")) {
                            topic.copy(count = customCount)
                        } else {
                            topic
                        }
                    }
                    setState { copy(vocabList = remoteList, topics = updatedTopics) }
                    if (currentState.selectedMode == VocabStudyMode.REFLEX) {
                        startReflexQuiz()
                    }
                } else {
                    setState { copy(vocabList = emptyList()) }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                setState { copy(vocabList = emptyList()) }
            } finally {
                setState { copy(isLoadingVocab = false) }
            }
        }
    }

    private fun startReflexQuiz() {
        val questions = generateQuestions(currentState.filteredList)
        setState {
            copy(
                quizState = ReflexQuizState(
                    questions = questions,
                    currentIndex = 0,
                    score = 0,
                    correctCount = 0,
                    selectedOption = null,
                    isAnswerChecked = false,
                    isFinished = false
                )
            )
        }
    }

    private fun generateQuestions(vocabList: List<VocabWordItem>): List<ReflexQuizQuestion> {
        if (vocabList.isEmpty()) return emptyList()
        val shuffledWords = vocabList.shuffled().take(10)
        val allMeanings = vocabList.map { it.meaning }.distinct()

        return shuffledWords.map { targetWord ->
            val wrongOptions = allMeanings
                .filter { it != targetWord.meaning }
                .shuffled()
                .take(3)

            val options = (wrongOptions + targetWord.meaning).shuffled()
            ReflexQuizQuestion(
                word = targetWord,
                options = options,
                correctAnswer = targetWord.meaning
            )
        }
    }

    private fun handleSelectQuizOption(option: String) {
        val currentQuiz = currentState.quizState
        if (currentQuiz.isAnswerChecked) return
        val currentQ = currentQuiz.currentQuestion ?: return

        val isCorrect = option == currentQ.correctAnswer
        val newScore = if (isCorrect) currentQuiz.score + 10 else currentQuiz.score
        val newCorrectCount = if (isCorrect) currentQuiz.correctCount + 1 else currentQuiz.correctCount

        setState {
            copy(
                quizState = currentQuiz.copy(
                    selectedOption = option,
                    isAnswerChecked = true,
                    score = newScore,
                    correctCount = newCorrectCount
                )
            )
        }

        viewModelScope.launch {
            delay(1200L)
            val nextIndex = currentState.quizState.currentIndex + 1
            if (nextIndex < currentState.quizState.questions.size) {
                setState {
                    copy(
                        quizState = currentState.quizState.copy(
                            currentIndex = nextIndex,
                            selectedOption = null,
                            isAnswerChecked = false
                        )
                    )
                }
            } else {
                setState {
                    copy(
                        quizState = currentState.quizState.copy(
                            isFinished = true
                        )
                    )
                }
            }
        }
    }

    private fun handleAddNewVocabulary(
        request: com.example.luminalearn.data.model.CreateVocabularyRequest,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            setState { copy(isSubmittingVocab = true) }
            try {
                val response = RetrofitClient.vocabularyApiService.createVocabulary(request)
                if (response.isSuccessful && response.body()?.data != null) {
                    val dto = response.body()!!.data!!
                    val newWord = VocabWordItem(
                        id = dto.id,
                        hanzi = dto.hanzi,
                        pinyin = dto.pinyin,
                        hanViet = dto.hanViet,
                        meaning = dto.meaning,
                        partOfSpeech = dto.partOfSpeech,
                        topic = dto.topic,
                        radical = dto.radical,
                        strokes = dto.strokes,
                        exampleHanzi = dto.exampleHanzi,
                        examplePinyin = dto.examplePinyin,
                        exampleMeaning = dto.exampleMeaning,
                        hskLevel = dto.hskLevel,
                        targetScore = dto.targetScore,
                        isMastered = dto.isMastered,
                        userId = dto.userId
                    )
                    val updatedList = listOf(newWord) + currentState.vocabList
                    setState {
                        copy(
                            vocabList = updatedList,
                            isAddVocabSheetOpen = false
                        )
                    }
                    setEffect(VocabularyUiEffect.ShowToast("Thêm từ '${dto.hanzi}' thành công!"))
                    fetchTopics(currentState.selectedHskTitle)
                    onSuccess()
                } else {
                    setEffect(VocabularyUiEffect.ShowToast("Không thể thêm từ: ${response.message()}"))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                setEffect(VocabularyUiEffect.ShowToast("Lỗi kết nối máy chủ: ${e.localizedMessage}"))
            } finally {
                setState { copy(isSubmittingVocab = false) }
            }
        }
    }

    fun lookupAi(query: String, onResult: (com.example.luminalearn.data.model.VocabularyDto?) -> Unit) {
        viewModelScope.launch {
            setState { copy(isAiLookingUp = true) }
            try {
                val response = RetrofitClient.vocabularyApiService.aiLookup(query)
                if (response.isSuccessful && response.body()?.data != null) {
                    onResult(response.body()!!.data)
                } else {
                    onResult(null)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(null)
            } finally {
                setState { copy(isAiLookingUp = false) }
            }
        }
    }
}
