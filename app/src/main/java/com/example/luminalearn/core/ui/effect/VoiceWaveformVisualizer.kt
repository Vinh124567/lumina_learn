package com.example.luminalearn.core.ui.effect

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Hiệu ứng cột sóng âm nhảy múa sinh động (Audio Waveform) khi người dùng phát âm hoặc AI đang nói.
 */
@Composable
fun VoiceWaveformVisualizer(
    isSpeaking: Boolean = true,
    barCount: Int = 5,
    maxHeight: Dp = 24.dp,
    minHeight: Dp = 6.dp,
    barWidth: Dp = 3.5.dp,
    barSpacing: Dp = 3.dp,
    gradient: Brush = Brush.verticalGradient(
        listOf(Color(0xFF5C50F6), Color(0xFF9333EA))
    ),
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "waveform_anim")
    val durations = listOf(420, 600, 360, 540, 460, 640, 380)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(barSpacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0 until barCount) {
            val duration = durations[i % durations.size]
            val heightMultiplier by transition.animateFloat(
                initialValue = 0.25f,
                targetValue = if (isSpeaking) 1.0f else 0.25f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = duration, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "bar_$i"
            )

            val currentHeight = minHeight + (maxHeight - minHeight) * heightMultiplier

            Box(
                modifier = Modifier
                    .width(barWidth)
                    .height(currentHeight)
                    .clip(RoundedCornerShape(50.dp))
                    .background(gradient)
            )
        }
    }
}
