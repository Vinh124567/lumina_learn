package com.example.luminalearn.presentation.vocabulary.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.presentation.vocabulary.model.VocabWordItem
import com.example.luminalearn.ui.theme.PlusJakartaSans
import kotlin.math.roundToInt
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private data class WordBlock(
    val id: String,
    val text: String,
    val pinyin: String
)

private data class SentencePuzzle(
    val id: String,
    val vietnamese: String,
    val correctBlocks: List<WordBlock>
)

/**
 * Minigame "Xếp Từ Thành Câu (Sentence Builder Puzzle)" - 100% Dữ Liệu Backend & Database:
 * - Trích xuất và phân tích tự động các câu ví dụ ngữ pháp (`exampleHanzi`, `examplePinyin`, `exampleMeaning`) từ kho dữ liệu Backend.
 * - Người chơi chạm vào các khối từ vựng Hán tự để xếp thành câu ngữ pháp hoàn chỉnh theo nghĩa tiếng Việt.
 * - Hỗ trợ chạm đưa lên khay câu và chạm gỡ về kho từ.
 * - Kiểm tra đúng -> Phát âm TextToSpeech, nổ hạt thưởng, tăng combo và qua vòng tự động.
 * - Bảng tổng kết kết quả High-Score chuẩn Arcade Hall of Fame.
 */
@Composable
fun SentenceOrderPuzzleDialog(
    isOpen: Boolean,
    vocabList: List<VocabWordItem> = emptyList(),
    onSpeak: (String) -> Unit = {},
    onDismiss: () -> Unit
) {
    if (!isOpen) return

    // ── 1. TRÍCH XUẤT CÂU TỰ ĐỘNG TỪ DỮ LIỆU BACKEND ──
    val backendPuzzles = remember(vocabList) {
        buildBackendPuzzles(vocabList)
    }

    // Danh sách câu dự phòng chất lượng cao nếu backend đang tải
    val defaultPuzzles = remember {
        listOf(
            SentencePuzzle(
                "d1",
                "Tôi là học sinh.",
                listOf(WordBlock("d1_1", "我", "wǒ"), WordBlock("d1_2", "是", "shì"), WordBlock("d1_3", "学生", "xué sheng"))
            ),
            SentencePuzzle(
                "d2",
                "Hôm nay tôi rất vui vẻ.",
                listOf(WordBlock("d2_1", "今天", "jīn tiān"), WordBlock("d2_2", "我", "wǒ"), WordBlock("d2_3", "很", "hěn"), WordBlock("d2_4", "高兴", "gāo xìng"))
            ),
            SentencePuzzle(
                "d3",
                "Cô ấy là giáo viên tiếng Trung.",
                listOf(WordBlock("d3_1", "她", "tā"), WordBlock("d3_2", "是", "shì"), WordBlock("d3_3", "汉语", "hàn yǔ"), WordBlock("d3_4", "老师", "lǎo shī"))
            ),
            SentencePuzzle(
                "d4",
                "Anh ấy là bạn tốt của tôi.",
                listOf(WordBlock("d4_1", "他", "tā"), WordBlock("d4_2", "是", "shì"), WordBlock("d4_3", "我的", "wǒ de"), WordBlock("d4_4", "好朋友", "hǎo péng you"))
            ),
            SentencePuzzle(
                "d5",
                "Tôi thích học tiếng Trung.",
                listOf(WordBlock("d5_1", "我", "wǒ"), WordBlock("d5_2", "喜欢", "xǐ huan"), WordBlock("d5_3", "学", "xué"), WordBlock("d5_4", "汉语", "hàn yǔ"))
            ),
            SentencePuzzle(
                "d6",
                "Bạn muốn đi đâu đấy?",
                listOf(WordBlock("d6_1", "你", "nǐ"), WordBlock("d6_2", "想去", "xiǎng qù"), WordBlock("d6_3", "哪里", "nǎ lǐ"))
            ),
            SentencePuzzle(
                "d7",
                "Chúng tôi đều đi Bắc Kinh.",
                listOf(WordBlock("d7_1", "我们", "wǒ men"), WordBlock("d7_2", "都", "dōu"), WordBlock("d7_3", "去", "qù"), WordBlock("d7_4", "北京", "běi jīng"))
            )
        )
    }

    // Chọn ngẫu nhiên 5 câu từ kho dữ liệu backend (hoặc default nếu kho rỗng)
    val poolPuzzles = remember(backendPuzzles) {
        if (backendPuzzles.size >= 5) backendPuzzles else defaultPuzzles
    }

    var selectedPuzzles by remember(poolPuzzles) {
        mutableStateOf(poolPuzzles.shuffled().take(5))
    }

    val totalStages = selectedPuzzles.size
    val scope = rememberCoroutineScope()

    var currentStageIndex by remember { mutableIntStateOf(0) }
    val currentPuzzle = selectedPuzzles.getOrNull(currentStageIndex) ?: selectedPuzzles.first()

    var timeLeft by remember { mutableIntStateOf(60) }
    var score by remember { mutableIntStateOf(0) }
    var lives by remember { mutableIntStateOf(3) }
    var combo by remember { mutableIntStateOf(1) }
    var maxCombo by remember { mutableIntStateOf(1) }
    var correctCount by remember { mutableIntStateOf(0) }
    var isGameOver by remember { mutableStateOf(false) }

    // Danh sách khối từ đã chọn vào khay ghép
    var assembledBlocks by remember { mutableStateOf<List<WordBlock>>(emptyList()) }
    // Danh sách khối từ còn lại dưới kho từ
    var availableBlocks by remember { mutableStateOf<List<WordBlock>>(emptyList()) }

    var isChecking by remember { mutableStateOf(false) }
    var isSuccessAnim by remember { mutableStateOf(false) }
    var isErrorAnim by remember { mutableStateOf(false) }

    // ── TRẠNG THÁI KÉO THẢ (DRAG & DROP) ──
    var draggingBlock by remember { mutableStateOf<WordBlock?>(null) }
    var isDraggingFromTray by remember { mutableStateOf(false) }
    var dragPosition by remember { mutableStateOf(Offset.Zero) }
    var trayBounds by remember { mutableStateOf(Rect.Zero) }
    // Lưu tọa độ hình chữ nhật của từng thẻ trên khay câu để tính vị trí chèn/đổi chỗ
    val trayChipBounds = remember { mutableStateMapOf<String, Rect>() }

    val isHoveringTray = remember(dragPosition, trayBounds, draggingBlock) {
        draggingBlock != null && (
            trayBounds.contains(dragPosition) ||
            (dragPosition.y >= trayBounds.top - 24f && dragPosition.y <= trayBounds.bottom + 24f &&
             dragPosition.x >= trayBounds.left - 20f && dragPosition.x <= trayBounds.right + 20f)
        )
    }

    // ── TÍNH TOÁN VỊ TRÍ CHÈN / ĐỔI CHỖ TRONG CÂU ──
    fun calculateInsertIndex(targetPos: Offset, excludeId: String?): Int {
        val candidates = assembledBlocks.filter { it.id != excludeId }
        if (candidates.isEmpty()) return 0

        for (i in candidates.indices) {
            val bId = candidates[i].id
            val bounds = trayChipBounds[bId] ?: continue

            // Nếu ngón tay thả ở dòng phía trên thẻ này
            if (targetPos.y < bounds.top - 10f) {
                return i
            }
            // Nếu cùng hàng với thẻ này (y nằm trong khoảng thẻ)
            val isSameRow = targetPos.y >= bounds.top - 20f && targetPos.y <= bounds.bottom + 20f
            if (isSameRow) {
                // Nếu ngón tay nằm ở bên trái (đằng trước) tâm của thẻ này -> chèn ngay trước thẻ này
                if (targetPos.x < bounds.center.x) {
                    return i
                }
            }
        }
        // Nằm sau tất cả các thẻ -> đặt vào cuối
        return candidates.size
    }

    // Khởi tạo vòng chơi
    fun loadPuzzle(index: Int) {
        val puzzle = selectedPuzzles.getOrNull(index) ?: return
        assembledBlocks = emptyList()
        availableBlocks = puzzle.correctBlocks.shuffled()
        isSuccessAnim = false
        isErrorAnim = false
        isChecking = false
        draggingBlock = null
        trayChipBounds.clear()
    }

    LaunchedEffect(currentStageIndex, selectedPuzzles) {
        loadPuzzle(currentStageIndex)
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

    // Kiểm tra hết mạng
    LaunchedEffect(lives) {
        if (lives <= 0) {
            isGameOver = true
        }
    }

    // Chạm chuyển khối từ lên khay ghép (mặc định chèn vào cuối nếu chạm)
    fun pickBlock(block: WordBlock) {
        if (isChecking) return
        availableBlocks = availableBlocks - block
        assembledBlocks = assembledBlocks + block
    }

    // Chạm gỡ khối từ từ khay về lại kho
    fun unpickBlock(block: WordBlock) {
        if (isChecking) return
        trayChipBounds.remove(block.id)
        assembledBlocks = assembledBlocks - block
        availableBlocks = availableBlocks + block
    }

    // Thả khối từ sau khi kéo: Hỗ trợ cả chèn vào giữa/đầu và đổi chỗ (Reorder)
    fun onDropBlock(block: WordBlock, fromTray: Boolean) {
        if (isChecking) return

        val isDroppedInsideTray = trayBounds.contains(dragPosition) ||
            (dragPosition.y >= trayBounds.top - 28f && dragPosition.y <= trayBounds.bottom + 28f &&
             dragPosition.x >= trayBounds.left - 20f && dragPosition.x <= trayBounds.right + 20f)

        if (!fromTray) {
            // 1. Kéo từ kho từ lên khay: chèn vào đúng vị trí ngón tay chỉ vào (đầu, giữa hoặc cuối)
            if (isDroppedInsideTray) {
                val insertIndex = calculateInsertIndex(dragPosition, excludeId = null)
                availableBlocks = availableBlocks - block
                val mutableList = assembledBlocks.toMutableList()
                val safeIndex = insertIndex.coerceIn(0, mutableList.size)
                mutableList.add(safeIndex, block)
                assembledBlocks = mutableList
            }
        } else {
            // 2. Kéo từ khay câu
            if (!isDroppedInsideTray) {
                // Kéo văng ra ngoài khay -> gỡ khỏi câu
                unpickBlock(block)
            } else {
                // Di chuyển đổi chỗ các từ trong câu (Reorder: đằng trước, đằng sau hoặc giữa)
                val remaining = assembledBlocks.filter { it.id != block.id }.toMutableList()
                val insertIndex = calculateInsertIndex(dragPosition, excludeId = block.id)
                val safeIndex = insertIndex.coerceIn(0, remaining.size)
                remaining.add(safeIndex, block)
                assembledBlocks = remaining
            }
        }
        draggingBlock = null
    }

    // Xếp lại từ đầu (Reset khay)
    fun resetAssembly() {
        if (isChecking) return
        availableBlocks = (availableBlocks + assembledBlocks).shuffled()
        assembledBlocks = emptyList()
        isErrorAnim = false
        draggingBlock = null
        trayChipBounds.clear()
    }

    // Kiểm tra kết quả
    fun checkResult() {
        if (assembledBlocks.isEmpty() || isChecking) return
        isChecking = true

        val userSentence = assembledBlocks.joinToString("") { it.text }
        val correctSentence = currentPuzzle.correctBlocks.joinToString("") { it.text }

        if (userSentence == correctSentence) {
            // ── ĐÚNG NGỮ PHÁP! ──
            isSuccessAnim = true
            val earned = 25 * combo
            score += earned
            combo++
            if (combo > maxCombo) maxCombo = combo
            correctCount++

            // Phát âm câu tiếng Trung hoàn chỉnh
            onSpeak(correctSentence)

            scope.launch {
                delay(900)
                if (currentStageIndex + 1 < totalStages) {
                    currentStageIndex++
                } else {
                    // Hoàn thành xuất sắc tất cả câu
                    score += timeLeft * 2 + 50
                    isGameOver = true
                }
            }
        } else {
            // ── SAI THỨ TỰ CÂU! ──
            isErrorAnim = true
            lives--
            combo = 1
            scope.launch {
                delay(600)
                isErrorAnim = false
                isChecking = false
            }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF030712).copy(alpha = 0.90f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp, vertical = 20.dp),
                contentAlignment = Alignment.Center
            ) {
            PixelCardBox(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color(0xFF1E1B4B),
                borderColor = PixelColors.BorderDark,
                shadowColor = PixelColors.ShadowDark,
                borderWidth = 1.5.dp,
                shadowOffset = 3.dp,
                cornerRadius = 12.dp,
                headerTitle = "🧩 XẾP TỪ THÀNH CÂU",
                headerBadge = "CÂU ${currentStageIndex + 1}/$totalStages",
                headerColor = PixelColors.PixelGreen,
                showRivets = true
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (isGameOver) {
                        // ══════════════════════════════════════════════════
                        // ── BẢNG TỔNG KẾT KẾT QUẢ VICTORY ──
                        // ══════════════════════════════════════════════════
                        val rankTitle = when {
                            score >= 180 -> "👑 THIÊN TÀI NGỮ PHÁP (RANK S)"
                            score >= 100 -> "⚡ BẬC THẦY CÂU (RANK A)"
                            else -> "🧩 TÂN BINH GHÉP TỪ (RANK B)"
                        }
                        val rankColor = when {
                            score >= 180 -> PixelColors.PixelYellow
                            score >= 100 -> PixelColors.PixelBlue
                            else -> PixelColors.PixelGreen
                        }

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
                            text = if (lives <= 0) "HẾT MẠNG RỒI!" else "HOÀN TẤT THỬ THÁCH!",
                            fontFamily = PlusJakartaSans,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Thẻ điểm số High-Score
                        PixelCardBox(
                            modifier = Modifier.fillMaxWidth(),
                            backgroundColor = Color(0xFF0F172A),
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
                                            text = "ĐÃ ĐÚNG",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Black,
                                            color = Color(0xFF94A3B8)
                                        )
                                        Text(
                                            text = "$correctCount/$totalStages câu",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Black,
                                            color = Color.White
                                        )
                                    }

                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = "THỜI GIAN",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Black,
                                            color = Color(0xFF94A3B8)
                                        )
                                        Text(
                                            text = "${60 - timeLeft}s",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Black,
                                            color = PixelColors.PixelBlue
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
                                            text = "⭐ +35 EXP",
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
                                            text = "🪙 +35 COINS",
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
                                    text = "✕ THOÁT",
                                    color = Color(0xFFE2E8F0),
                                    textColor = PixelColors.BorderDark,
                                    onClick = onDismiss,
                                    height = 44.dp,
                                    fillMaxWidth = true
                                )
                            }

                            Box(modifier = Modifier.weight(1f)) {
                                PixelArcadeButton(
                                    text = "▶ CHƠI LẠI ⚡",
                                    color = PixelColors.PixelGreen,
                                    textColor = Color.White,
                                    onClick = {
                                        timeLeft = 60
                                        lives = 3
                                        score = 0
                                        combo = 1
                                        maxCombo = 1
                                        correctCount = 0
                                        currentStageIndex = 0
                                        isGameOver = false
                                        selectedPuzzles = poolPuzzles.shuffled().take(5)
                                        loadPuzzle(0)
                                    },
                                    height = 44.dp,
                                    fillMaxWidth = true
                                )
                            }
                        }
                    } else {
                        // ══════════════════════════════════════════════════
                        // ── GAMEPLAY ĐANG DIỄN RA ──
                        // ══════════════════════════════════════════════════

                        // HUD: Máu + Đồng hồ + Điểm số
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Mạng HP
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

                            // Đồng hồ
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

                            // Điểm & Combo
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

                        Spacer(modifier = Modifier.height(10.dp))

                        // Bảng Nghĩa Tiếng Việt Cần Ghép (Mission Prompt)
                        PixelCardBox(
                            modifier = Modifier.fillMaxWidth(),
                            backgroundColor = Color(0xFF0F172A),
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
                                    .padding(vertical = 8.dp, horizontal = 12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "🎯 HÃY XẾP THÀNH CÂU CÓ NGHĨA:",
                                        fontSize = 9.5.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color(0xFF38BDF8),
                                        letterSpacing = 0.5.sp
                                    )

                                    if (backendPuzzles.isNotEmpty()) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(3.dp))
                                                .background(Color(0xFF064E3B))
                                                .border(BorderStroke(1.dp, Color(0xFF10B981)), RoundedCornerShape(3.dp))
                                                .padding(horizontal = 4.dp, vertical = 1.dp)
                                        ) {
                                            Text(
                                                text = "BACKEND DATA",
                                                fontSize = 7.5.sp,
                                                fontWeight = FontWeight.Black,
                                                color = PixelColors.PixelGreen
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(3.dp))

                                Text(
                                    text = "“${currentPuzzle.vietnamese}”",
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Black,
                                    color = PixelColors.PixelYellow,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // ── KHAY LẮP GHÉP CÂU (ASSEMBLY TRAY) ──
                        val trayBorderColor = when {
                            isSuccessAnim -> PixelColors.PixelGreen
                            isErrorAnim -> PixelColors.PixelRed
                            isHoveringTray -> PixelColors.PixelYellow
                            else -> PixelColors.BorderDark
                        }
                        val trayBgColor = when {
                            isSuccessAnim -> Color(0xFF064E3B)
                            isErrorAnim -> Color(0xFF450A0A)
                            isHoveringTray -> Color(0xFF312E81)
                            else -> Color(0xFF0F172A)
                        }

                        PixelCardBox(
                            modifier = Modifier
                                .fillMaxWidth()
                                .onGloballyPositioned { coords ->
                                    trayBounds = coords.boundsInRoot()
                                },
                            backgroundColor = trayBgColor,
                            borderColor = trayBorderColor,
                            shadowColor = if (isHoveringTray) PixelColors.PixelYellow else PixelColors.ShadowDark,
                            borderWidth = if (isHoveringTray) 2.5.dp else 2.dp,
                            shadowOffset = 3.dp,
                            cornerRadius = 8.dp,
                            showRivets = false
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = if (isHoveringTray) "▼ THẢ VÀO ĐÂY ĐỂ GHÉP VÀO CÂU! ▼"
                                    else if (assembledBlocks.isEmpty()) "◀ CHẠM HOẶC KÉO TỪ BÊN DƯỚI LÊN ĐÂY ▶"
                                    else "KHAY GHÉP CÂU (CHẠM HOẶC KÉO RA ĐỂ GỠ):",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (isErrorAnim) PixelColors.PixelRed else if (isHoveringTray) PixelColors.PixelYellow else Color(0xFF94A3B8)
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                val hoverInsertIndex = if (isHoveringTray && draggingBlock != null) {
                                    calculateInsertIndex(dragPosition, if (isDraggingFromTray) draggingBlock?.id else null)
                                } else -1

                                val trayDisplayBlocks = if (isDraggingFromTray) assembledBlocks.filter { it.id != draggingBlock?.id } else assembledBlocks

                                SimpleFlowRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalSpacing = 6.dp,
                                    verticalSpacing = 8.dp
                                ) {
                                    if (assembledBlocks.isEmpty() && (!isHoveringTray || draggingBlock == null)) {
                                        // Các ô slot chờ rỗng
                                        repeat(currentPuzzle.correctBlocks.size) {
                                            Box(
                                                modifier = Modifier
                                                    .padding(horizontal = 4.dp)
                                                    .size(width = 64.dp, height = 48.dp)
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .border(BorderStroke(1.2.dp, Color(0xFF334155)), RoundedCornerShape(6.dp))
                                                    .background(Color(0xFF1E293B).copy(alpha = 0.5f)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(text = "···", color = Color(0xFF64748B), fontSize = 16.sp)
                                            }
                                        }
                                    } else {
                                        trayDisplayBlocks.forEachIndexed { index, block ->
                                            if (index == hoverInsertIndex) {
                                                // Vạch chỉ báo vị trí chèn màu vàng neon
                                                Box(
                                                    modifier = Modifier
                                                        .padding(horizontal = 2.dp)
                                                        .size(width = 5.dp, height = 48.dp)
                                                        .clip(RoundedCornerShape(2.5.dp))
                                                        .background(PixelColors.PixelYellow)
                                                )
                                            }

                                            PuzzleWordChip(
                                                block = block,
                                                isSelected = true,
                                                isBeingDragged = draggingBlock?.id == block.id,
                                                onClick = { unpickBlock(block) },
                                                onPositioned = { bounds ->
                                                    trayChipBounds[block.id] = bounds
                                                },
                                                onDragStart = { pos ->
                                                    draggingBlock = block
                                                    isDraggingFromTray = true
                                                    dragPosition = pos
                                                },
                                                onDragMove = { delta ->
                                                    dragPosition += delta
                                                },
                                                onDragEnd = {
                                                    onDropBlock(block, fromTray = true)
                                                }
                                            )
                                        }

                                        if (hoverInsertIndex >= trayDisplayBlocks.size && hoverInsertIndex != -1) {
                                            // Vạch chỉ báo vị trí chèn ở cuối
                                            Box(
                                                modifier = Modifier
                                                    .padding(horizontal = 2.dp)
                                                    .size(width = 5.dp, height = 48.dp)
                                                    .clip(RoundedCornerShape(2.5.dp))
                                                    .background(PixelColors.PixelYellow)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // ── KHO TỪ ĐÃ XÁO TRỘN (SCRAMBLED WORD POOL) ──
                        Text(
                            text = "KHO TỪ VỰNG (CHẠM HOẶC KÉO VÀO Ô TRÊN):",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF94A3B8),
                            modifier = Modifier.align(Alignment.Start)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        SimpleFlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalSpacing = 6.dp,
                            verticalSpacing = 8.dp
                        ) {
                            availableBlocks.forEach { block ->
                                PuzzleWordChip(
                                    block = block,
                                    isSelected = false,
                                    isBeingDragged = draggingBlock?.id == block.id,
                                    onClick = { pickBlock(block) },
                                    onDragStart = { pos ->
                                        draggingBlock = block
                                        isDraggingFromTray = false
                                        dragPosition = pos
                                    },
                                    onDragMove = { delta ->
                                        dragPosition += delta
                                    },
                                    onDragEnd = {
                                        onDropBlock(block, fromTray = false)
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // ── NÚT ĐIỀU KHIỂN ARCADE: RESET & CHECK ──
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Nút xếp lại
                            Box(modifier = Modifier.weight(1f)) {
                                PixelArcadeButton(
                                    text = "↺ XẾP LẠI",
                                    color = Color(0xFFE2E8F0),
                                    textColor = PixelColors.BorderDark,
                                    onClick = { resetAssembly() },
                                    height = 46.dp,
                                    fillMaxWidth = true
                                )
                            }

                            // Nút Kiểm Tra Kết Quả
                            val isReady = assembledBlocks.size == currentPuzzle.correctBlocks.size
                            Box(modifier = Modifier.weight(1.5f)) {
                                PixelArcadeButton(
                                    text = if (isSuccessAnim) "✨ CHÍNH XÁC!" else if (isErrorAnim) "❌ SAI RỒI!" else "⚡ KIỂM TRA",
                                    color = if (isSuccessAnim) PixelColors.PixelGreen else if (isErrorAnim) PixelColors.PixelRed else if (isReady) PixelColors.PixelYellow else Color(0xFF64748B),
                                    textColor = if (isSuccessAnim || isErrorAnim) Color.White else PixelColors.BorderDark,
                                    onClick = { checkResult() },
                                    height = 46.dp,
                                    fillMaxWidth = true
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Nút thoát minigame
                        Text(
                            text = "✕ THOÁT TRÒ CHƠI",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF64748B),
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .clickable { onDismiss() }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

            // ── THẺ NỔI BAY THEO NGÓN TAY KHI ĐANG KÉO (FLOATING DRAG GHOST) ──
            if (draggingBlock != null) {
                val block = draggingBlock!!
                val isTrayHover = isHoveringTray || isDraggingFromTray
                val ghostBgColor = if (isTrayHover) Color(0xFF312E81) else Color.White
                val ghostTextColor = if (isTrayHover) PixelColors.PixelYellow else PixelColors.BorderDark
                val ghostPinyinColor = if (isTrayHover) Color(0xFF38BDF8) else Color(0xFF64748B)
                val ghostBorderColor = if (isTrayHover) PixelColors.PixelYellow else PixelColors.BorderDark

                Box(
                    modifier = Modifier
                        .offset {
                            IntOffset(
                                (dragPosition.x - 42.dp.toPx()).roundToInt(),
                                (dragPosition.y - 32.dp.toPx()).roundToInt()
                            )
                        }
                        .graphicsLayer {
                            scaleX = 1.15f
                            scaleY = 1.15f
                            shadowElevation = 18.dp.toPx()
                        }
                ) {
                    PixelCardBox(
                        backgroundColor = ghostBgColor,
                        borderColor = ghostBorderColor,
                        shadowColor = PixelColors.ShadowDark,
                        borderWidth = 2.2.dp,
                        shadowOffset = 4.dp,
                        cornerRadius = 6.dp,
                        showRivets = false
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = block.text,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Black,
                                color = ghostTextColor
                            )
                            Text(
                                text = block.pinyin,
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = ghostPinyinColor
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Thuật toán tách câu ví dụ từ Backend thành các khối từ vựng ngữ pháp tự nhiên.
 */
private fun buildBackendPuzzles(vocabList: List<VocabWordItem>): List<SentencePuzzle> {
    if (vocabList.isEmpty()) return emptyList()

    // 1. Tạo từ điển tra cứu Hán tự -> Pinyin từ backend
    val pinyinMap = HashMap<String, String>(vocabList.size + 40)
    vocabList.forEach {
        if (it.hanzi.isNotBlank() && it.pinyin.isNotBlank()) {
            pinyinMap[it.hanzi] = it.pinyin
        }
    }

    // Bổ sung các đại từ & trợ từ thông dụng trong câu
    val commonTokens = listOf(
        "我" to "wǒ", "你" to "nǐ", "他" to "tā", "她" to "tā", "它" to "tā",
        "我们" to "wǒmen", "你们" to "nǐmen", "他们" to "tāmen", "她们" to "tāmen",
        "是" to "shì", "在" to "zài", "有" to "yǒu", "很" to "hěn", "不" to "bù",
        "都" to "dōu", "也" to "yě", "的" to "de", "了" to "le", "吗" to "ma",
        "呢" to "ne", "去" to "qù", "来" to "lái", "看" to "kàn", "吃" to "chī",
        "喝" to "hē", "想" to "xiǎng", "会" to "huì", "能" to "néng", "好" to "hǎo",
        "大" to "dà", "小" to "xiǎo", "多" to "duō", "少" to "shǎo", "这" to "zhè",
        "那" to "nà", "什么" to "shénme", "谁" to "shéi", "哪里" to "nǎlǐ", "怎么" to "zěnme"
    )
    commonTokens.forEach { (h, p) ->
        if (!pinyinMap.containsKey(h)) pinyinMap[h] = p
    }

    val wordSet = pinyinMap.keys

    // 2. Lọc các câu ví dụ từ backend: dài từ 4 đến 16 chữ, có nghĩa tiếng Việt rõ ràng
    val candidateItems = vocabList.filter { item ->
        val text = item.exampleHanzi.replace(Regex("[。？！,.?!，、 ]"), "").trim()
        text.length in 4..16 && item.exampleMeaning.isNotBlank()
    }

    val puzzles = mutableListOf<SentencePuzzle>()

    candidateItems.forEach { item ->
        val cleanHanzi = item.exampleHanzi.replace(Regex("[。？！,.?!，、]"), "").trim()
        val blocks = mutableListOf<WordBlock>()
        var i = 0

        while (i < cleanHanzi.length) {
            var matched: String? = null
            for (len in 4 downTo 2) {
                if (i + len <= cleanHanzi.length) {
                    val sub = cleanHanzi.substring(i, i + len)
                    if (wordSet.contains(sub)) {
                        matched = sub
                        break
                    }
                }
            }
            if (matched != null) {
                val py = pinyinMap[matched] ?: ""
                blocks.add(WordBlock("${item.id}_${blocks.size}", matched, py))
                i += matched.length
            } else {
                val singleChar = cleanHanzi[i].toString()
                val py = pinyinMap[singleChar] ?: ""
                blocks.add(WordBlock("${item.id}_${blocks.size}", singleChar, py))
                i++
            }
        }

        // Chỉ chọn các câu vừa vặn được tách thành 3 đến 6 khối từ
        if (blocks.size in 3..6) {
            puzzles.add(
                SentencePuzzle(
                    id = item.id,
                    vietnamese = item.exampleMeaning.trim(),
                    correctBlocks = blocks
                )
            )
        }
    }

    return puzzles
}

/**
 * Chip khối từ vựng phong cách Pixel Block dập nổi 3D:
 * - Khi ở kho: Mặt thẻ trắng viền đen đậm, bóng đổ 3D.
 * - Khi ở trên khay: Mặt thẻ xanh Cyber viền vàng rực rỡ.
 */
@Composable
private fun PuzzleWordChip(
    block: WordBlock,
    isSelected: Boolean,
    isBeingDragged: Boolean = false,
    onClick: () -> Unit,
    onPositioned: (Rect) -> Unit = {},
    onDragStart: (Offset) -> Unit = {},
    onDragMove: (Offset) -> Unit = {},
    onDragEnd: () -> Unit = {}
) {
    var chipOriginInRoot by remember { mutableStateOf(Offset.Zero) }

    val bgColor = if (isSelected) Color(0xFF312E81) else Color.White
    val textColor = if (isSelected) PixelColors.PixelYellow else PixelColors.BorderDark
    val pinyinColor = if (isSelected) Color(0xFF38BDF8) else Color(0xFF64748B)
    val borderColor = if (isSelected) Color(0xFF818CF8) else PixelColors.BorderDark

    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .graphicsLayer {
                alpha = if (isBeingDragged) 0.25f else 1f
            }
            .onGloballyPositioned { coords ->
                chipOriginInRoot = coords.positionInRoot()
                onPositioned(coords.boundsInRoot())
            }
            .pointerInput(block.id) {
                awaitEachGesture {
                    val down = awaitFirstDown(requireUnconsumed = false)
                    var isDragging = false
                    var totalDrag = 0f
                    val pointerId = down.id
                    val touchSlop = 14f

                    while (true) {
                        val event = awaitPointerEvent()
                        val change = event.changes.firstOrNull { it.id == pointerId }
                        if (change == null || !change.pressed) {
                            if (isDragging) {
                                onDragEnd()
                            } else if (totalDrag < touchSlop) {
                                // Người dùng chỉ chạm nhẹ -> kích hoạt onClick
                                onClick()
                            }
                            break
                        }

                        val dragAmount = change.position - change.previousPosition
                        val dist = dragAmount.getDistance()
                        totalDrag += dist

                        if (!isDragging && totalDrag > touchSlop) {
                            isDragging = true
                            change.consume()
                            val initialScreenPos = chipOriginInRoot + change.position
                            onDragStart(initialScreenPos)
                        } else if (isDragging) {
                            change.consume()
                            onDragMove(dragAmount)
                        }
                    }
                }
            }
    ) {
        PixelCardBox(
            backgroundColor = bgColor,
            borderColor = borderColor,
            shadowColor = PixelColors.ShadowDark,
            borderWidth = 1.8.dp,
            shadowOffset = 2.5.dp,
            cornerRadius = 6.dp,
            showRivets = false
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = block.text,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = textColor
                )
                Text(
                    text = block.pinyin,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = pinyinColor
                )
            }
        }
    }
}

/**
 * Custom Wrap/Flow Row tự động xuống dòng và căn giữa:
 * - Không phụ thuộc ExperimentalLayoutApi hay các phiên bản binary FlowRow khác nhau của Compose Foundation.
 * - Triệt tiêu 100% lỗi runtime NoSuchMethodError khi chạy trên các thiết bị Android thực tế.
 */
@Composable
private fun SimpleFlowRow(
    modifier: Modifier = Modifier,
    horizontalSpacing: Dp = 8.dp,
    verticalSpacing: Dp = 8.dp,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val hSpacingPx = horizontalSpacing.roundToPx()
        val vSpacingPx = verticalSpacing.roundToPx()

        val rows = mutableListOf<List<Placeable>>()
        val rowHeights = mutableListOf<Int>()
        val rowWidths = mutableListOf<Int>()

        var currentRow = mutableListOf<Placeable>()
        var currentRowWidth = 0
        var currentRowHeight = 0

        val placeables = measurables.map { it.measure(constraints.copy(minWidth = 0, minHeight = 0)) }

        for (placeable in placeables) {
            val neededWidth = if (currentRow.isEmpty()) placeable.width else currentRowWidth + hSpacingPx + placeable.width
            if (neededWidth <= constraints.maxWidth || currentRow.isEmpty()) {
                currentRow.add(placeable)
                currentRowWidth = neededWidth
                currentRowHeight = maxOf(currentRowHeight, placeable.height)
            } else {
                rows.add(currentRow)
                rowHeights.add(currentRowHeight)
                rowWidths.add(currentRowWidth)

                currentRow = mutableListOf(placeable)
                currentRowWidth = placeable.width
                currentRowHeight = placeable.height
            }
        }
        if (currentRow.isNotEmpty()) {
            rows.add(currentRow)
            rowHeights.add(currentRowHeight)
            rowWidths.add(currentRowWidth)
        }

        val totalHeight = (rowHeights.sum() + (rows.size - 1).coerceAtLeast(0) * vSpacingPx).coerceIn(constraints.minHeight, constraints.maxHeight)
        val totalWidth = constraints.maxWidth

        layout(totalWidth, totalHeight) {
            var yOffset = 0
            rows.forEachIndexed { index, row ->
                val rowW = rowWidths[index]
                var xOffset = (totalWidth - rowW) / 2
                for (placeable in row) {
                    placeable.placeRelative(xOffset, yOffset)
                    xOffset += placeable.width + hSpacingPx
                }
                yOffset += rowHeights[index] + vSpacingPx
            }
        }
    }
}

