package com.example.luminalearn.presentation.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

/**
 * Hiệu ứng pháo hoa / hoa giấy nổ tung từ giữa màn hình (Confetti Explosion)
 * Mỗi khi [triggerKey] thay đổi (tăng lên), một đợt pháo hoa mới sẽ nổ rực rỡ từ tâm màn hình.
 */
@Composable
fun CelebrationEffect(
    triggerKey: Int,
    modifier: Modifier = Modifier
) {
    if (triggerKey > 0) {
        key(triggerKey) {
            val fireworksParty = remember(triggerKey) {
                Party(
                    speed = 8f,
                    maxSpeed = 36f,
                    damping = 0.9f,
                    spread = 360, // Bắn tròn 360 độ từ tâm
                    colors = listOf(
                        0xFF5C50F6.toInt(), // Tím Lumina
                        0xFFF59E0B.toInt(), // Vàng Gold
                        0xFFEF4444.toInt(), // Đỏ may mắn
                        0xFF10B981.toInt(), // Xanh ngọc
                        0xFFEC4899.toInt(), // Hồng phấn
                        0xFF6366F1.toInt()  // Indigo
                    ),
                    emitter = Emitter(duration = 250, TimeUnit.MILLISECONDS).max(150),
                    position = Position.Relative(0.5, 0.42) // Vị trí tâm màn hình (50% ngang, 42% dọc)
                )
            }

            KonfettiView(
                modifier = modifier.fillMaxSize(),
                parties = listOf(fireworksParty)
            )
        }
    }
}
