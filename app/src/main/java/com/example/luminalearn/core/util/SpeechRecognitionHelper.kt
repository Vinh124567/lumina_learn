package com.example.luminalearn.core.util

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

sealed class SpeechRecognitionState {
    object Idle : SpeechRecognitionState()
    data class Listening(val rmsDb: Float = 0f) : SpeechRecognitionState()
    object Processing : SpeechRecognitionState()
    data class Success(
        val spokenText: String,
        val targetText: String,
        val score: Int,
        val feedback: String,
        val isAccurate: Boolean
    ) : SpeechRecognitionState()
    data class Error(val message: String) : SpeechRecognitionState()
}

class SpeechRecognitionHelper(private val context: Context) {

    private var speechRecognizer: SpeechRecognizer? = null
    private var currentTargetText: String = ""

    private val _state = MutableStateFlow<SpeechRecognitionState>(SpeechRecognitionState.Idle)
    val state: StateFlow<SpeechRecognitionState> = _state.asStateFlow()

    init {
        initRecognizer()
    }

    private fun initRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(context)) {
            try {
                speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
                    setRecognitionListener(createListener())
                }
            } catch (e: Exception) {
                Log.e("SpeechRecognitionHelper", "Lỗi khởi tạo SpeechRecognizer: ${e.message}", e)
            }
        }
    }

    private fun createListener(): RecognitionListener {
        return object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                _state.value = SpeechRecognitionState.Listening(0f)
            }

            override fun onBeginningOfSpeech() {
                _state.value = SpeechRecognitionState.Listening(0f)
            }

            override fun onRmsChanged(rmsdB: Float) {
                if (_state.value is SpeechRecognitionState.Listening) {
                    _state.value = SpeechRecognitionState.Listening(rmsdB)
                }
            }

            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {
                _state.value = SpeechRecognitionState.Processing
            }

            override fun onError(error: Int) {
                val errorMsg = when (error) {
                    SpeechRecognizer.ERROR_AUDIO -> "Lỗi thu âm, vui lòng kiểm tra micro"
                    SpeechRecognizer.ERROR_CLIENT -> "Lỗi kết nối bộ nhận diện giọng nói"
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Chưa cấp quyền ghi âm micro"
                    SpeechRecognizer.ERROR_NETWORK, SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Lỗi kết nối mạng nhận diện"
                    SpeechRecognizer.ERROR_NO_MATCH -> "Chưa nghe rõ giọng nói. Hãy nói to và sát micro hơn nhé!"
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Bộ nhận diện đang bận, vui lòng thử lại sau giây lát"
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "Hết thời gian chờ giọng nói"
                    else -> "Không thể nhận diện giọng nói (Mã $error)"
                }
                Log.w("SpeechRecognitionHelper", "SpeechRecognizer error: $errorMsg ($error)")
                _state.value = SpeechRecognitionState.Error(errorMsg)
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val spokenText = matches?.firstOrNull() ?: ""
                evaluateSpeech(spokenText, currentTargetText)
            }

            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        }
    }

    fun startListening(targetText: String) {
        currentTargetText = targetText.trim()
        if (speechRecognizer == null) {
            initRecognizer()
        }

        val recognizer = speechRecognizer
        if (recognizer == null) {
            _state.value = SpeechRecognitionState.Error("Thiết bị chưa hỗ trợ nhận diện giọng nói tiếng Trung")
            return
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "zh-CN")
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "zh-CN")
            putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, "zh-CN")
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false)
        }

        try {
            _state.value = SpeechRecognitionState.Listening(0f)
            recognizer.startListening(intent)
        } catch (e: Exception) {
            Log.e("SpeechRecognitionHelper", "Lỗi startListening: ${e.message}", e)
            _state.value = SpeechRecognitionState.Error("Không thể kích hoạt micro: ${e.message}")
        }
    }

    fun stopListening() {
        try {
            speechRecognizer?.stopListening()
        } catch (e: Exception) {
            Log.e("SpeechRecognitionHelper", "Lỗi stopListening: ${e.message}", e)
        }
    }

    fun resetState() {
        _state.value = SpeechRecognitionState.Idle
    }

    private fun evaluateSpeech(spokenText: String, target: String) {
        val cleanSpoken = spokenText.trim().replace(" ", "").lowercase(Locale.ROOT)
        val cleanTarget = target.trim().replace(" ", "").lowercase(Locale.ROOT)

        val (score, feedback, isAccurate) = when {
            cleanSpoken.isEmpty() -> Triple(
                0,
                "Không nhận được âm thanh rõ ràng. Hãy thử đọc lại!",
                false
            )
            cleanSpoken == cleanTarget -> Triple(
                100,
                "Tuyệt vời! Phát âm chuẩn xác 100%, chuẩn âm bản xứ ✨",
                true
            )
            cleanSpoken.contains(cleanTarget) || cleanTarget.contains(cleanSpoken) -> Triple(
                90,
                "Rất tốt! Âm điệu rất rõ ràng và chuẩn xác 👍",
                true
            )
            else -> {
                // Tính tỷ lệ ký tự trùng khớp
                val matchedChars = cleanTarget.count { cleanSpoken.contains(it) }
                val ratio = if (cleanTarget.isNotEmpty()) matchedChars.toFloat() / cleanTarget.length else 0f
                if (ratio >= 0.5f) {
                    val calculatedScore = (60 + ratio * 30).toInt().coerceIn(60, 85)
                    Triple(
                        calculatedScore,
                        "Khá tốt! Bạn hãy chú ý thêm cao độ thanh điệu nhé.",
                        true
                    )
                } else {
                    Triple(
                        45,
                        "Chưa nhận diện đúng chữ này. Hãy bấm nghe mẫu rồi đọc lại nhé!",
                        false
                    )
                }
            }
        }

        _state.value = SpeechRecognitionState.Success(
            spokenText = spokenText,
            targetText = target,
            score = score,
            feedback = feedback,
            isAccurate = isAccurate
        )
    }

    fun destroy() {
        try {
            speechRecognizer?.destroy()
            speechRecognizer = null
        } catch (e: Exception) {
            Log.e("SpeechRecognitionHelper", "Lỗi destroy SpeechRecognizer: ${e.message}", e)
        }
    }
}
