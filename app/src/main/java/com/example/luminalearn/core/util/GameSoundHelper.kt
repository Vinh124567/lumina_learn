package com.example.luminalearn.core.util

import android.media.AudioManager
import android.media.ToneGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Trợ lý phát âm thanh hiệu ứng Game hóa nhẹ nhàng (Sound Effects - SFX)
 * Dùng ToneGenerator tích hợp sẵn của Android: không tốn dung lượng file, phản hồi tức thì (0ms latency).
 */
object GameSoundHelper {

    var isSoundEnabled: Boolean = true

    private var toneGenerator: ToneGenerator? = null

    init {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 85)
        } catch (_: Exception) {
            toneGenerator = null
        }
    }

    /**
     * Tiếng chạm cơ học nhẹ (Click)
     */
    fun playClick() {
        if (!isSoundEnabled) return
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 40)
        } catch (_: Exception) {}
    }

    /**
     * Tiếng chuông vui nhộn khi chọn đáp án đúng
     */
    fun playCorrect() {
        if (!isSoundEnabled) return
        CoroutineScope(Dispatchers.Default).launch {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 90)
                delay(100)
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_PROMPT, 130)
            } catch (_: Exception) {}
        }
    }

    /**
     * Tiếng rung cảnh báo khi chọn sai
     */
    fun playIncorrect() {
        if (!isSoundEnabled) return
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_NACK, 160)
        } catch (_: Exception) {}
    }

    /**
     * Tiếng pháo hoa bung nắp rương nhận thưởng
     */
    fun playChestOpen() {
        if (!isSoundEnabled) return
        CoroutineScope(Dispatchers.Default).launch {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP2, 80)
                delay(90)
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 110)
                delay(110)
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_PROMPT, 180)
            } catch (_: Exception) {}
        }
    }

    /**
     * Kèn hiệu chúc mừng tốt nghiệp kỳ thi Boss
     */
    fun playVictoryFanfare() {
        if (!isSoundEnabled) return
        CoroutineScope(Dispatchers.Default).launch {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 120)
                delay(140)
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 120)
                delay(140)
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_PROMPT, 260)
            } catch (_: Exception) {}
        }
    }
}
