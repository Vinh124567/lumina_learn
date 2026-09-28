package com.example.luminalearn.core.util

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import java.util.Locale

class TextToSpeechHelper(private val context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var isInitialized = false

    init {
        tts = TextToSpeech(context.applicationContext, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val ttsInstance = tts ?: return
            var result = ttsInstance.setLanguage(Locale.SIMPLIFIED_CHINESE)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                result = ttsInstance.setLanguage(Locale.CHINESE)
            }
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                result = ttsInstance.setLanguage(Locale.CHINA)
            }

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.w("TextToSpeechHelper", "Ngôn ngữ tiếng Trung chưa được hỗ trợ hoặc thiếu gói giọng nói.")
            } else {
                isInitialized = true
                Log.d("TextToSpeechHelper", "Khởi tạo TextToSpeech tiếng Trung thành công.")
            }
        } else {
            Log.e("TextToSpeechHelper", "Khởi tạo TTS thất bại với mã status: $status")
        }
    }

    fun speak(text: String, rate: Float = 1.0f) {
        val cleanText = text.trim()
        if (cleanText.isEmpty()) return

        val ttsInstance = tts
        if (ttsInstance == null || !isInitialized) {
            Toast.makeText(context, "Đang tải dữ liệu phát âm tiếng Trung...", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            ttsInstance.setSpeechRate(rate)
            ttsInstance.setPitch(1.0f)
            val result = ttsInstance.speak(cleanText, TextToSpeech.QUEUE_FLUSH, null, cleanText)
            if (result == TextToSpeech.ERROR) {
                Toast.makeText(context, "Phát âm không thành công", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("TextToSpeechHelper", "Lỗi phát âm: ${e.message}", e)
        }
    }

    fun speakSlow(text: String) {
        speak(text, rate = 0.65f)
    }

    fun stop() {
        tts?.stop()
    }

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
            tts = null
            isInitialized = false
        } catch (e: Exception) {
            Log.e("TextToSpeechHelper", "Lỗi đóng TTS: ${e.message}", e)
        }
    }
}