package com.example.luminalearn.presentation.vocabulary.component

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.presentation.vocabulary.model.VocabWordItem
import com.example.luminalearn.ui.theme.PlusJakartaSans
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

// ── Plain Game Data Objects (Zero-Allocation Pool) ──
private class TargetObject(
    val id: String,
    val wordItem: VocabWordItem,
    val isCorrect: Boolean,
    val baseX: Float,
    val baseY: Float,
    var currentX: Float,
    var currentY: Float,
    val width: Float = 210f,
    val height: Float = 110f,
    val phase: Float,
    var hoverTimer: Float = 0f,
    var alpha: Float = 0f,
    var scale: Float = 0.65f,
    var isDying: Boolean = false,
    var hitFlashTimer: Float = 0f
)

private class LaserObject(
    var x: Float,
    var y: Float,
    val vy: Float = -28f
)

private class ParticleObject(
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    val color: Color,
    val size: Float,
    var life: Float = 1f
)

private class TextPopupObject(
    val text: String,
    var x: Float,
    var y: Float,
    val colorInt: Int,
    var life: Float = 1f
)

private data class StarData(
    val xRatio: Float,
    val yRatio: Float,
    val radius: Float,
    val color: Color
)

/**
 * Minigame "Pháo Thủ Hán Tự (Hanzi Space Shooter)" - Kiến trúc Buttery-Smooth Zero-Jitter Canvas Engine:
 * - Chuyển động lơ lửng điều hòa hình Sin (Sinusoidal Harmonic Hover) triệt tiêu hoàn toàn giật cục và rung lắc hai bên.
 * - Tuyệt đối không giật chữ: Loại bỏ hoàn toàn screen shake thô bạo, thay bằng hiệu ứng chớp viền đỏ thanh thoát và sóng hạt.
 * - Đội hình chiến thuật 3 cánh (V-Formation) phân bổ không gian hợp lý, không bao giờ đè lên nhau.
 * - 100% Zero-Recomposition trong chu kỳ render 60-120 FPS.
 */
@Composable
fun HanziShooterDialog(
    isOpen: Boolean,
    vocabList: List<VocabWordItem>,
    onDismiss: () -> Unit
) {
    if (!isOpen) return

    val defaultVocabs = remember {
        listOf(
            VocabWordItem("s1", "你好", "nǐ hǎo", "Nhĩ hảo", "Xin chào", "Thán từ", "Giao tiếp", "", "", "", "", "", "HSK 1"),
            VocabWordItem("s2", "谢谢", "xiè xie", "Tạ tạ", "Cảm ơn", "Động từ", "Giao tiếp", "", "", "", "", "", "HSK 1"),
            VocabWordItem("s3", "再见", "zài jiàn", "Tái kiến", "Tạm biệt", "Thán từ", "Giao tiếp", "", "", "", "", "", "HSK 1"),
            VocabWordItem("s4", "朋友", "péng you", "Bằng hữu", "Bạn bè", "Danh từ", "Đời sống", "", "", "", "", "", "HSK 1"),
            VocabWordItem("s5", "高兴", "gāo xìng", "Cao hứng", "Vui vẻ", "Tính từ", "Cảm xúc", "", "", "", "", "", "HSK 1"),
            VocabWordItem("s6", "学习", "xué xí", "Học tập", "Học tập", "Động từ", "Giáo dục", "", "", "", "", "", "HSK 1"),
            VocabWordItem("s7", "老师", "lǎo shī", "Lão sư", "Giáo viên", "Danh từ", "Nghề nghiệp", "", "", "", "", "", "HSK 1"),
            VocabWordItem("s8", "学生", "xué sheng", "Học sinh", "Học sinh", "Danh từ", "Giáo dục", "", "", "", "", "", "HSK 1"),
            VocabWordItem("s9", "喜欢", "xǐ huan", "Hỉ hoan", "Thích", "Động từ", "Cảm xúc", "", "", "", "", "", "HSK 1"),
            VocabWordItem("s10", "明天", "míng tiān", "Minh thiên", "Ngày mai", "Danh từ", "Thời gian", "", "", "", "", "", "HSK 1"),
            VocabWordItem("s11", "中国", "zhōng guó", "Trung Quốc", "Trung Quốc", "Danh từ", "Địa lý", "", "", "", "", "", "HSK 1"),
            VocabWordItem("s12", "吃饭", "chī fàn", "Ngật phạn", "Ăn cơm", "Động từ", "Ăn uống", "", "", "", "", "", "HSK 1")
        )
    }

    val poolVocabs = remember(vocabList) {
        val combined = (vocabList.shuffled() + defaultVocabs).distinctBy { it.hanzi }
        if (combined.size >= 8) combined else defaultVocabs
    }

    val scope = rememberCoroutineScope()

    var timeLeft by remember { mutableIntStateOf(60) }
    var score by remember { mutableIntStateOf(0) }
    var lives by remember { mutableIntStateOf(3) }
    var combo by remember { mutableIntStateOf(1) }
    var maxCombo by remember { mutableIntStateOf(1) }
    var totalShots by remember { mutableIntStateOf(0) }
    var hits by remember { mutableIntStateOf(0) }
    var isGameOver by remember { mutableStateOf(false) }

    // Đề bài mục tiêu hiện tại
    var currentTargetWord by remember { mutableStateOf<VocabWordItem?>(null) }

    // Bộ sưu tập đối tượng trò chơi (Plain Lists - Không gây Recomposition)
    val targetsList = remember { mutableListOf<TargetObject>() }
    val lasersList = remember { mutableListOf<LaserObject>() }
    val particlesList = remember { mutableListOf<ParticleObject>() }
    val popupsList = remember { mutableListOf<TextPopupObject>() }

    // Vị trí người chơi (0f .. 1f) và cờ chạm giữ màn hình
    var playerPosRatio by remember { mutableFloatStateOf(0.5f) }
    var isFingerTouching by remember { mutableStateOf(false) }
    var renderTick by remember { mutableLongStateOf(0L) }
    var isWaveTransitioning by remember { mutableStateOf(false) }

    // Pre-allocated Starfield
    val starfield = remember {
        List(24) { index ->
            StarData(
                xRatio = Random.nextFloat(),
                yRatio = Random.nextFloat(),
                radius = if (index % 3 == 0) 2.4f else 1.5f,
                color = if (index % 2 == 0) Color(0xFF38BDF8) else Color(0xFFE2E8F0)
            )
        }
    }

    // Các cọ vẽ Native Paint khởi tạo 1 lần duy nhất trên GPU
    val hanziPaint = remember {
        Paint().apply {
            color = android.graphics.Color.BLACK
            textSize = 44f
            typeface = Typeface.DEFAULT_BOLD
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
    }
    val pinyinPaint = remember {
        Paint().apply {
            color = android.graphics.Color.rgb(100, 116, 139)
            textSize = 22f
            typeface = Typeface.DEFAULT_BOLD
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
    }
    val popupPaint = remember {
        Paint().apply {
            textSize = 38f
            typeface = Typeface.DEFAULT_BOLD
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
    }

    // Tái sử dụng Path vẽ phi thuyền tránh GC
    val reusableFlamePath = remember { Path() }
    val reusableInnerFlame = remember { Path() }
    val reusableShipPath = remember { Path() }

    // Sinh đợt phi thuyền chữ Hán mới theo đội hình V-Formation mượt mà
    fun spawnWave(widthPx: Float) {
        if (poolVocabs.size < 4 || widthPx < 100f) return
        val correct = poolVocabs.random()
        val distractors = poolVocabs.filter { it.id != correct.id }.shuffled().take(2)
        val all = (listOf(correct) + distractors).shuffled()

        currentTargetWord = correct
        targetsList.clear()

        // Phân bổ 3 vị trí vàng trên màn hình: Trái (26%), Giữa (50%), Phải (74%)
        val formations = listOf(
            Triple(widthPx * 0.26f, 280f, 0f),
            Triple(widthPx * 0.50f, 410f, 2.1f),
            Triple(widthPx * 0.74f, 280f, 4.2f)
        )

        all.forEachIndexed { i, word ->
            val (baseX, baseY, phase) = formations[i % formations.size]
            targetsList.add(
                TargetObject(
                    id = "${word.id}_$i",
                    wordItem = word,
                    isCorrect = word.id == correct.id,
                    baseX = baseX,
                    baseY = baseY,
                    currentX = baseX,
                    currentY = baseY - 70f, // Lướt nhẹ từ trên xuống
                    phase = phase,
                    alpha = 0f,
                    scale = 0.65f
                )
            )
        }
    }

    // Nổ hạt vụn pixel
    fun createExplosion(x: Float, y: Float, isSuccess: Boolean) {
        val colors = if (isSuccess) {
            listOf(PixelColors.PixelYellow, PixelColors.PixelOrange, Color(0xFF38BDF8), Color.White)
        } else {
            listOf(PixelColors.PixelRed, PixelColors.PixelRedDark, Color(0xFFFB923C), Color.White)
        }
        repeat(16) {
            val angle = Random.nextFloat() * 2f * Math.PI.toFloat()
            val speed = Random.nextFloat() * 8.5f + 2.5f
            particlesList.add(
                ParticleObject(
                    x = x,
                    y = y,
                    vx = cos(angle) * speed,
                    vy = sin(angle) * speed,
                    color = colors.random(),
                    size = Random.nextFloat() * 5f + 2.5f
                )
            )
        }
    }

    // Bắn đạn laser nòng kép
    fun fireLaser(currentShipX: Float, currentShipY: Float) {
        if (isGameOver) return
        totalShots++
        lasersList.add(LaserObject(x = currentShipX - 26f, y = currentShipY - 26f))
        lasersList.add(LaserObject(x = currentShipX + 26f, y = currentShipY - 26f))
    }

    // Đếm ngược 60s
    LaunchedEffect(isGameOver) {
        if (!isGameOver) {
            while (timeLeft > 0 && lives > 0) {
                delay(1000)
                timeLeft--
            }
            isGameOver = true
        }
    }

    // Kiểm tra hết máu
    LaunchedEffect(lives) {
        if (lives <= 0) {
            isGameOver = true
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF030712))
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            val widthPx = constraints.maxWidth.toFloat()
            val heightPx = constraints.maxHeight.toFloat()
            val playerY = heightPx - 240f

            LaunchedEffect(widthPx) {
                if (targetsList.isEmpty() && widthPx > 100f) {
                    spawnWave(widthPx)
                }
            }

            // ── VÒNG LẶP AUTO-FIRE KHI NGƯỜI CHƠI CHẠM GIỮ MÀN HÌNH ──
            LaunchedEffect(isGameOver, widthPx) {
                while (!isGameOver) {
                    if (isFingerTouching) {
                        val currentShipX = (playerPosRatio * (widthPx - 100f)) + 50f
                        fireLaser(currentShipX, playerY)
                    }
                    delay(220)
                }
            }

            // ── HIGH-PERFORMANCE 60/120 FPS GAME ENGINE VẬT LÝ NATIVE ──
            LaunchedEffect(isGameOver, widthPx) {
                var lastFrameNanos = 0L

                while (!isGameOver && widthPx > 100f) {
                    withFrameNanos { nowNanos ->
                        if (lastFrameNanos == 0L) {
                            lastFrameNanos = nowNanos
                            return@withFrameNanos
                        }
                        val dt = ((nowNanos - lastFrameNanos) / 1_000_000_000f).coerceIn(0.001f, 0.05f)
                        lastFrameNanos = nowNanos
                        val timeScale = dt * 60f

                        // 1. Cập nhật chuyển động điều hòa hình Sin lơ lửng êm ái tuyệt đối
                        targetsList.forEach { target ->
                            target.hoverTimer += 0.038f * timeScale

                            // Hạ cánh mượt mà khi vừa spawn
                            if (target.currentY < target.baseY) {
                                target.currentY += (target.baseY - target.currentY) * 0.12f * timeScale
                            } else {
                                target.currentY = target.baseY
                            }

                            // Chuyển động sóng điều hòa (Sinusoidal Sway): Không đổi hướng đột ngột, không đập biên
                            val swayX = sin(target.hoverTimer + target.phase) * 18f
                            target.currentX = target.baseX + swayX

                            // Hoạt ảnh Scale & Alpha vào đợt
                            if (!target.isDying) {
                                if (target.alpha < 1f) {
                                    target.alpha = (target.alpha + 0.08f * timeScale).coerceAtMost(1f)
                                }
                                if (target.scale < 1f) {
                                    target.scale = (target.scale + 0.06f * timeScale).coerceAtMost(1f)
                                }
                            } else {
                                target.scale = (target.scale - 0.09f * timeScale).coerceAtLeast(0f)
                                target.alpha = (target.alpha - 0.1f * timeScale).coerceAtLeast(0f)
                            }

                            if (target.hitFlashTimer > 0f) {
                                target.hitFlashTimer = (target.hitFlashTimer - 0.06f * timeScale).coerceAtLeast(0f)
                            }
                        }

                        // 2. Cập nhật đạn Laser & Xử lý va chạm
                        lasersList.removeAll { laser ->
                            laser.y += laser.vy * timeScale
                            var shouldRemove = false

                            val hitTarget = targetsList.firstOrNull { t ->
                                !t.isDying && t.alpha > 0.5f &&
                                    laser.x in (t.currentX - t.width / 2f)..(t.currentX + t.width / 2f) &&
                                    laser.y in (t.currentY)..(t.currentY + t.height)
                            }

                            if (hitTarget != null) {
                                shouldRemove = true

                                if (hitTarget.isCorrect) {
                                    // ── BẮN TRÚNG MỤC TIÊU ĐÚNG! ──
                                    if (!isWaveTransitioning) {
                                        isWaveTransitioning = true
                                        hitTarget.isDying = true

                                        hits++
                                        val earned = 20 * combo
                                        score += earned
                                        combo++
                                        if (combo > maxCombo) maxCombo = combo

                                        createExplosion(hitTarget.currentX, hitTarget.currentY + hitTarget.height / 2f, true)
                                        popupsList.add(
                                            TextPopupObject(
                                                text = "+$earned PTS!",
                                                x = hitTarget.currentX,
                                                y = hitTarget.currentY,
                                                colorInt = android.graphics.Color.rgb(250, 204, 21)
                                            )
                                        )

                                        scope.launch {
                                            delay(200)
                                            targetsList.forEach { if (it.id != hitTarget.id) it.isDying = true }
                                            delay(140)
                                            spawnWave(widthPx)
                                            isWaveTransitioning = false
                                        }
                                    }
                                } else {
                                    // ── BẮN SAI! TRỪ MẠNG (Chớp viền thanh lịch, không giật màn hình) ──
                                    lives--
                                    combo = 1
                                    hitTarget.hitFlashTimer = 1f
                                    createExplosion(hitTarget.currentX, hitTarget.currentY + hitTarget.height / 2f, false)
                                    popupsList.add(
                                        TextPopupObject(
                                            text = "MISS! -1 HP",
                                            x = hitTarget.currentX,
                                            y = hitTarget.currentY,
                                            colorInt = android.graphics.Color.rgb(239, 68, 68)
                                        )
                                    )
                                }
                            } else if (laser.y < 200f) {
                                shouldRemove = true
                            }
                            shouldRemove
                        }

                        // 3. Cập nhật hạt nổ (In-place removal)
                        particlesList.removeAll { p ->
                            p.x += p.vx * timeScale
                            p.y += p.vy * timeScale
                            p.life -= 0.045f * timeScale
                            p.life <= 0f
                        }

                        // 4. Cập nhật Text Popup điểm bay (In-place removal)
                        popupsList.removeAll { pop ->
                            pop.y -= 2f * timeScale
                            pop.life -= 0.035f * timeScale
                            pop.life <= 0f
                        }

                        // Báo hiệu vẽ frame mới trên GPU Canvas
                        renderTick++
                    }
                }
            }

            // ══════════════════════════════════════════════════════════
            // ── GPU CANVAS ENGINE: KHÔNG RECOMPOSE TRONG SUỐT GAME ──
            // ══════════════════════════════════════════════════════════
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(widthPx) {
                        awaitEachGesture {
                            val down = awaitFirstDown(requireUnconsumed = false)
                            isFingerTouching = true

                            val newRatio = ((down.position.x - 50f) / (widthPx - 100f).coerceAtLeast(1f)).coerceIn(0f, 1f)
                            playerPosRatio = newRatio
                            val shipX = (newRatio * (widthPx - 100f)) + 50f
                            fireLaser(shipX, playerY)

                            var pointerId = down.id
                            while (true) {
                                val event = awaitPointerEvent()
                                val change = event.changes.firstOrNull { it.id == pointerId }
                                if (change == null || !change.pressed) {
                                    isFingerTouching = false
                                    break
                                }
                                val deltaX = change.position.x - change.previousPosition.x
                                if (deltaX != 0f) {
                                    val deltaRatio = deltaX / (widthPx - 100f).coerceAtLeast(1f)
                                    playerPosRatio = (playerPosRatio + deltaRatio).coerceIn(0f, 1f)
                                }
                                change.consume()
                            }
                            isFingerTouching = false
                        }
                    }
            ) {
                @Suppress("UNUSED_VARIABLE")
                val tick = renderTick
                val canvasWidth = size.width
                val canvasHeight = size.height
                val currentShipX = (playerPosRatio * (canvasWidth - 100f)) + 50f

                // 1. Vẽ bầu trời sao tĩnh lấp lánh
                starfield.forEach { star ->
                    drawCircle(
                        color = star.color,
                        radius = star.radius,
                        center = Offset(star.xRatio * canvasWidth, star.yRatio * canvasHeight)
                    )
                }

                // 2. Vẽ các đợt đạn Laser Plasma đôi
                lasersList.forEach { laser ->
                    drawLine(
                        color = Color(0xFF38BDF8),
                        start = Offset(laser.x, laser.y),
                        end = Offset(laser.x, laser.y - 24f),
                        strokeWidth = 5f
                    )
                    drawLine(
                        color = Color.White,
                        start = Offset(laser.x, laser.y),
                        end = Offset(laser.x, laser.y - 24f),
                        strokeWidth = 2f
                    )
                }

                // 3. Vẽ các phi thuyền mang chữ Hán (Sinusoidal Harmonic Hover)
                targetsList.forEach { target ->
                    if (target.alpha > 0.02f) {
                        val centerX = target.currentX
                        val hoverY = cos(target.hoverTimer * 1.4f + target.phase) * 6f
                        val centerY = target.currentY + target.height / 2f + hoverY

                        val halfW = (target.width / 2f) * target.scale
                        val halfH = (target.height / 2f) * target.scale
                        val drawX = centerX - halfW
                        val drawY = centerY - halfH
                        val actualW = halfW * 2f
                        val actualH = halfH * 2f

                        val currentAlpha = target.alpha.coerceIn(0f, 1f)
                        val alphaInt = (currentAlpha * 255).toInt()

                        val borderColor = if (target.hitFlashTimer > 0f) {
                            PixelColors.PixelRed.copy(alpha = currentAlpha)
                        } else {
                            PixelColors.BorderDark.copy(alpha = currentAlpha)
                        }

                        // Bóng cứng 3D
                        drawRoundRect(
                            color = PixelColors.ShadowDark.copy(alpha = currentAlpha * 0.85f),
                            topLeft = Offset(drawX + 4f * target.scale, drawY + 4f * target.scale),
                            size = Size(actualW, actualH),
                            cornerRadius = CornerRadius(10f * target.scale, 10f * target.scale)
                        )

                        // Thân phi thuyền trắng
                        drawRoundRect(
                            color = Color.White.copy(alpha = currentAlpha),
                            topLeft = Offset(drawX, drawY),
                            size = Size(actualW, actualH),
                            cornerRadius = CornerRadius(10f * target.scale, 10f * target.scale)
                        )

                        // Viền đậm phong cách Retro Pixel (Chớp đỏ thanh lịch khi bắn trượt)
                        drawRoundRect(
                            color = borderColor,
                            topLeft = Offset(drawX, drawY),
                            size = Size(actualW, actualH),
                            cornerRadius = CornerRadius(10f * target.scale, 10f * target.scale),
                            style = Stroke(width = if (target.hitFlashTimer > 0f) 3.8f * target.scale else 2.8f * target.scale)
                        )

                        // Chữ Hán & Pinyin bằng Native Paint (Tọa độ tuyệt đối mượt mà)
                        hanziPaint.alpha = alphaInt
                        hanziPaint.textSize = 44f * target.scale
                        drawContext.canvas.nativeCanvas.drawText(
                            target.wordItem.hanzi,
                            centerX,
                            centerY - 2f * target.scale,
                            hanziPaint
                        )

                        pinyinPaint.alpha = alphaInt
                        pinyinPaint.textSize = 22f * target.scale
                        drawContext.canvas.nativeCanvas.drawText(
                            target.wordItem.pinyin,
                            centerX,
                            centerY + 32f * target.scale,
                            pinyinPaint
                        )

                        // Lửa phản lực phía dưới
                        drawCircle(
                            color = Color(0xFFFB923C).copy(alpha = currentAlpha),
                            radius = 5.5f * target.scale,
                            center = Offset(centerX, drawY + actualH + 7f * target.scale)
                        )
                    }
                }

                // 4. Vẽ hạt nổ Pixel Particles
                particlesList.forEach { p ->
                    drawCircle(
                        color = p.color.copy(alpha = p.life.coerceIn(0f, 1f)),
                        radius = p.size,
                        center = Offset(p.x, p.y)
                    )
                }

                // 5. Vẽ Popup điểm bay lên
                popupsList.forEach { pop ->
                    popupPaint.color = pop.colorInt
                    popupPaint.alpha = (pop.life.coerceIn(0f, 1f) * 255).toInt()
                    drawContext.canvas.nativeCanvas.drawText(
                        pop.text,
                        pop.x,
                        pop.y,
                        popupPaint
                    )
                }

                // 6. Vẽ Chiến cơ phi thuyền người chơi
                drawOptimizedPlayerSpaceship(
                    x = currentShipX,
                    y = playerY,
                    flamePath = reusableFlamePath,
                    innerFlame = reusableInnerFlame,
                    shipPath = reusableShipPath
                )
            }

            // ══════════════════════════════════════════════════════════
            // ── TOP GAME HUD: BẢNG NHIỆM VỤ, MÁU & ĐIỂM SỐ ──
            // ══════════════════════════════════════════════════
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                // Hàng 1: Máu HP + Đồng hồ + Điểm số
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Mạng tim HP
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        repeat(3) { index ->
                            val isAlive = index < lives
                            Text(
                                text = if (isAlive) "❤️" else "🖤",
                                fontSize = 16.sp
                            )
                        }
                    }

                    // Đồng hồ đếm ngược
                    PixelCardBox(
                        backgroundColor = if (timeLeft <= 10) PixelColors.PixelRed else PixelColors.CardBg,
                        borderColor = PixelColors.BorderDark,
                        shadowColor = PixelColors.ShadowDark,
                        borderWidth = 1.2.dp,
                        shadowOffset = 2.dp,
                        cornerRadius = 4.dp,
                        showRivets = false
                    ) {
                        Text(
                            text = "⏱️ ${timeLeft}S",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = if (timeLeft <= 10) Color.White else PixelColors.BorderDark,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    // Điểm số & Combo
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        if (combo > 1) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(PixelColors.PixelOrange)
                                    .border(BorderStroke(1.dp, PixelColors.BorderDark), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 5.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "x$combo 🔥",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                            }
                        }

                        Text(
                            text = "$score PTS",
                            fontFamily = PlusJakartaSans,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = PixelColors.PixelYellow
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Hàng 2: Mục Tiêu Chiến Dịch (Mission Target Banner)
                PixelCardBox(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Color(0xFF1E1B4B),
                    borderColor = PixelColors.BorderDark,
                    shadowColor = PixelColors.ShadowDark,
                    borderWidth = 1.5.dp,
                    shadowOffset = 3.dp,
                    cornerRadius = 8.dp,
                    showRivets = false
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp, horizontal = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🎯 TIÊU DIỆT TỪ CÓ NGHĨA:",
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF38BDF8),
                            letterSpacing = 0.5.sp
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = "“${currentTargetWord?.meaning ?: "..."}”",
                            fontFamily = PlusJakartaSans,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            color = PixelColors.PixelYellow,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // ══════════════════════════════════════════════════════════
            // ── BOTTOM CONTROLS: HƯỚNG DẪN & NÚT THOÁT ──
            // ══════════════════════════════════════════════════
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Nút thoát trận
                Text(
                    text = "✕ THOÁT",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF94A3B8),
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .clickable { onDismiss() }
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                )

                // Hướng dẫn vuốt ngón tay
                Text(
                    text = "◀ KÉO LÁI & GIỮ ĐỂ XẢ ĐẠN LIÊN THANH ▶",
                    fontSize = 8.5.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF64748B)
                )

                // Nút Bắn Nhanh Pháo Thủ (Rapid Fire Button)
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(PixelColors.PixelRed)
                        .border(BorderStroke(2.dp, PixelColors.BorderDark), CircleShape)
                        .bounceClick(scaleDown = 0.88f) {
                            val currentShipX = (playerPosRatio * (widthPx - 100f)) + 50f
                            fireLaser(currentShipX, playerY)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "🚀", fontSize = 13.sp)
                        Text(
                            text = "FIRE",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                }
            }

            // ══════════════════════════════════════════════════════════
            // ── MÀN HÌNH TỔNG KẾT CHIẾN DỊCH (GAME OVER) ──
            // ══════════════════════════════════════════════════
            if (isGameOver) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF030712).copy(alpha = 0.94f))
                        .padding(18.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val accuracy = if (totalShots > 0) ((hits.toFloat() / totalShots) * 100).toInt() else 0
                    val rankTitle = when {
                        score >= 250 -> "👑 THIỆN XẠ VŨ TRỤ (RANK S)"
                        score >= 150 -> "⚡ PHI ĐỘI TRƯỞNG (RANK A)"
                        else -> "🚀 TÂN BINH KHÔNG GIAN (RANK B)"
                    }
                    val rankColor = when {
                        score >= 250 -> PixelColors.PixelYellow
                        score >= 150 -> PixelColors.PixelBlue
                        else -> PixelColors.PixelGreen
                    }

                    PixelCardBox(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = Color(0xFF0F172A),
                        borderColor = PixelColors.BorderDark,
                        shadowColor = PixelColors.ShadowDark,
                        borderWidth = 2.dp,
                        shadowOffset = 5.dp,
                        cornerRadius = 10.dp,
                        headerTitle = "🏆 CHIẾN DỊCH KHÔNG GIAN",
                        headerBadge = "HOÀN TẤT",
                        headerColor = PixelColors.PixelYellow,
                        showRivets = true
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(4.dp))

                            PixelCardBox(
                                backgroundColor = rankColor,
                                borderColor = PixelColors.BorderDark,
                                shadowColor = PixelColors.ShadowDark,
                                borderWidth = 1.5.dp,
                                shadowOffset = 2.dp,
                                cornerRadius = 6.dp,
                                showRivets = false
                            ) {
                                Text(
                                    text = rankTitle,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Black,
                                    color = PixelColors.BorderDark,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = if (lives <= 0) "TÀU CHIẾN BỊ BẮN HẠ!" else "CHIẾN DỊCH KẾT THÚC!",
                                fontFamily = PlusJakartaSans,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Thẻ điểm số High-Score
                            PixelCardBox(
                                modifier = Modifier.fillMaxWidth(),
                                backgroundColor = Color(0xFF1E1B4B),
                                borderColor = PixelColors.BorderDark,
                                shadowColor = PixelColors.ShadowDark,
                                borderWidth = 1.5.dp,
                                shadowOffset = 3.dp,
                                cornerRadius = 8.dp,
                                showRivets = false
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "$score PTS",
                                        fontFamily = PlusJakartaSans,
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.Black,
                                        color = PixelColors.PixelYellow
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceAround
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(
                                                text = "TIÊU DIỆT",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Black,
                                                color = Color(0xFF94A3B8)
                                            )
                                            Text(
                                                text = "$hits mục tiêu",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Black,
                                                color = Color.White
                                            )
                                        }

                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(
                                                text = "ĐỘ CHÍNH XÁC",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Black,
                                                color = Color(0xFF94A3B8)
                                            )
                                            Text(
                                                text = "$accuracy%",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Black,
                                                color = PixelColors.PixelGreen
                                            )
                                        }

                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(
                                                text = "MAX COMBO",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Black,
                                                color = Color(0xFF94A3B8)
                                            )
                                            Text(
                                                text = "x$maxCombo 🔥",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Black,
                                                color = PixelColors.PixelOrange
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    // Thưởng EXP & Coins
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(Color(0xFF312E81))
                                                .border(BorderStroke(1.dp, Color(0xFF6366F1)), RoundedCornerShape(4.dp))
                                                .padding(horizontal = 8.dp, vertical = 3.dp)
                                        ) {
                                            Text(
                                                text = "⭐ +40 EXP",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Black,
                                                color = PixelColors.PixelYellow
                                            )
                                        }

                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(Color(0xFF064E3B))
                                                .border(BorderStroke(1.dp, Color(0xFF10B981)), RoundedCornerShape(4.dp))
                                                .padding(horizontal = 8.dp, vertical = 3.dp)
                                        ) {
                                            Text(
                                                text = "🪙 +40 COINS",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Black,
                                                color = PixelColors.PixelGreen
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(modifier = Modifier.weight(1f)) {
                                    PixelArcadeButton(
                                        text = "✕ RỜI CHIẾN TRƯỜNG",
                                        color = Color(0xFFE2E8F0),
                                        textColor = PixelColors.BorderDark,
                                        onClick = onDismiss,
                                        height = 44.dp,
                                        fillMaxWidth = true
                                    )
                                }

                                Box(modifier = Modifier.weight(1f)) {
                                    PixelArcadeButton(
                                        text = "▶ BẮN LẠI ⚡",
                                        color = PixelColors.PixelRed,
                                        textColor = Color.White,
                                        onClick = {
                                            timeLeft = 60
                                            lives = 3
                                            score = 0
                                            combo = 1
                                            maxCombo = 1
                                            totalShots = 0
                                            hits = 0
                                            isGameOver = false
                                            spawnWave(widthPx)
                                        },
                                        height = 44.dp,
                                        fillMaxWidth = true
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ── CÁC HÀM VẼ GRAPHICS TỐI ƯU HÓA TRỰC TIẾP TRÊN GPU CANVAS (ZERO ALLOCATIONS) ──

private fun DrawScope.drawOptimizedPlayerSpaceship(
    x: Float,
    y: Float,
    flamePath: Path,
    innerFlame: Path,
    shipPath: Path
) {
    // 1. Ngọn lửa phản lực ngoài
    flamePath.rewind()
    flamePath.moveTo(x - 11f, y + 24f)
    flamePath.lineTo(x, y + 40f)
    flamePath.lineTo(x + 11f, y + 24f)
    flamePath.close()
    drawPath(flamePath, color = Color(0xFFFB923C))

    // 2. Ngọn lửa phản lực lõi vàng
    innerFlame.rewind()
    innerFlame.moveTo(x - 5f, y + 24f)
    innerFlame.lineTo(x, y + 34f)
    innerFlame.lineTo(x + 5f, y + 24f)
    innerFlame.close()
    drawPath(innerFlame, color = Color(0xFFFACC15))

    // 3. Thân phi thuyền Cyber Fighter
    shipPath.rewind()
    shipPath.moveTo(x, y - 30f) // Mũi tàu
    shipPath.lineTo(x + 26f, y + 22f) // Cánh phải
    shipPath.lineTo(x + 13f, y + 18f)
    shipPath.lineTo(x, y + 24f) // Đuôi tàu
    shipPath.lineTo(x - 13f, y + 18f)
    shipPath.lineTo(x - 26f, y + 22f) // Cánh trái
    shipPath.close()
    drawPath(shipPath, color = Color(0xFF6366F1))
    drawPath(shipPath, color = Color(0xFF1E1B4B), style = Stroke(width = 2.8f))

    // 4. Buồng lái phi thuyền
    drawCircle(
        color = Color(0xFF38BDF8),
        radius = 6.5f,
        center = Offset(x, y - 6f)
    )

    // 5. Hai nòng pháo laser ở 2 bên cánh
    drawRect(
        color = Color(0xFFFACC15),
        topLeft = Offset(x - 30f, y - 5f),
        size = Size(4.5f, 20f)
    )
    drawRect(
        color = Color(0xFFFACC15),
        topLeft = Offset(x + 25.5f, y - 5f),
        size = Size(4.5f, 20f)
    )
}
