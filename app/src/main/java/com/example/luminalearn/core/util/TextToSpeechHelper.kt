package com.example.luminalearn.core.util

import android.content.Context
import android.speech.tts.TextToSpeech

class TextToSpeechHelper(context: Context) : TextToSpeech.OnInitListener {
    private val tts = TextToSpeech(context, this)
    override fun onInit(status: Int) {  }
}