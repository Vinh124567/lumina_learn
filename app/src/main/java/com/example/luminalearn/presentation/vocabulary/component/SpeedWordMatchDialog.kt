package com.example.luminalearn.presentation.vocabulary.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.presentation.vocabulary.model.VocabWordItem
import com.example.luminalearn.ui.theme.PlusJakartaSans
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private data class MatchItem(
    val id: String,
    val wordId: String,
    val text: String,
    val subText: String = "",
    val isHanzi: Boolean
)

/**
 * Màn Sàn Đấu Phản Xạ Ghép Từ Siêu Tốc 60s phong cách Retro 16-Bit Arcade Arena:
 * - Chuẩn 3 VÒNG (Stages), mỗi vòng 4 cặp từ mới không trùng lặp.
 * - TỰ ĐỘNG NEXT VÒNG khi ghép đủ 4 cặp (xử lý coroutine scope không bị cancel bởi state update).
 * - TỰ ĐỘNG TỔNG KẾT CHIẾN THẮNG ngay khi hoàn thành tất cả các vòng (kèm Time Bonus điểm thưởng).
 * - HUD: Đồng hồ đếm ngược, vạch năng lượng, bảng điểm High-Score và Combo nổ lửa.
 */
@Composable
fun SpeedWordMatchDialog(
    isOpen: Boolean,
    vocabList: List<VocabWordItem>,
    onDismiss: () -> Unit
) {
    if (!isOpen) return

    val defaultVocabs = remember {
        listOf(
            VocabWordItem("d1", "你好", "nǐ hǎo", "Nhĩ hảo", "Xin chào", "Thán từ", "Giao tiếp", "", "", "", "", "", "HSK 1"),
            VocabWordItem("d2", "谢谢", "xiè xie", "Tạ tạ", "Cảm ơn", "Động từ", "Giao tiếp", "", "", "", "", "", "HSK 1"),
            VocabWordItem("d3", "再见", "zài jiàn", "Tái kiến", "Tạm biệt", "Thán từ", "Giao tiếp", "", "", "", "", "", "HSK 1"),
            VocabWordItem("d4", "朋友", "péng you", "Bằng hữu", "Bạn bè", "Danh từ", "Đời sống", "", "", "", "", "", "HSK 1"),
            VocabWordItem("d5", "高兴", "gāo xìng", "Cao hứng", "Vui vẻ", "Tính từ", "Cảm xúc", "", "", "", "", "", "HSK 1"),
            VocabWordItem("d6", "学习", "xué xí", "Học tập", "Học tập", "Động từ", "Giáo dục", "", "", "", "", "", "HSK 1"),
            VocabWordItem("d7", "中国", "zhōng guó", "Trung Quốc", "Trung Quốc", "Danh từ", "Địa lý", "", "", "", "", "", "HSK 1"),
            VocabWordItem("d8", "老师", "lǎo shī", "Lão sư", "Giáo viên", "Danh từ", "Nghề nghiệp", "", "", "", "", "", "HSK 1"),
            VocabWordItem("d9", "学生", "xué sheng", "Học sinh", "Học sinh", "Danh từ", "Giáo dục", "", "", "", "", "", "HSK 1"),
            VocabWordItem("d10", "喜欢", "xǐ huan", "Hỉ hoan", "Thích", "Động từ", "Cảm xúc", "", "", "", "", "", "HSK 1"),
            VocabWordItem("d11", "明天", "míng tiān", "Minh thiên", "Ngày mai", "Danh từ", "Thời gian", "", "", "", "", "", "HSK 1"),
            VocabWordItem("d12", "今天", "jīn tiān", "Kim thiên", "Hôm nay", "Danh từ", "Thời gian", "", "", "", "", "", "HSK 1")
        )
    }

    val sampleVocabs = remember(vocabList) {
        val pool = (vocabList.shuffled() + defaultVocabs).distinctBy { it.hanzi }.take(12)
        if (pool.size >= 12) pool else defaultVocabs
    }

    val totalStages = 3
    val scope = rememberCoroutineScope()

    var currentStage by remember { mutableIntStateOf(1) }
    var timeLeft by remember { mutableIntStateOf(60) }
    var score by remember { mutableIntStateOf(0) }
    var combo by remember { mutableIntStateOf(1) }
    var maxCombo by remember { mutableIntStateOf(1) }
    var matchedPairsCount by remember { mutableIntStateOf(0) }
    var isGameOver by remember { mutableStateOf(false) }
    var isAllStagesCleared by remember { mutableStateOf(false) }
    var stageNotification by remember { mutableStateOf<String?>(null) }
    var isCheckingMatch by remember { mutableStateOf(false) }

    // Danh sách 4 cặp từ hiện tại đang thi đấu
    var leftItems by remember { mutableStateOf<List<MatchItem>>(emptyList()) }
    var rightItems by remember { mutableStateOf<List<MatchItem>>(emptyList()) }

    var selectedLeftId by remember { mutableStateOf<String?>(null) }
    var selectedRightId by remember { mutableStateOf<String?>(null) }
    var matchedWordIds by remember { mutableStateOf<Set<String>>(emptySet()) }
    var errorPair by remember { mutableStateOf<Pair<String, String>?>(null) }

    fun loadStage(stage: Int) {
        val startIndex = ((stage - 1) * 4) % sampleVocabs.size
        val pool = sampleVocabs.drop(startIndex).take(4).let {
            if (it.size < 4) sampleVocabs.take(4) else it
        }
        leftItems = pool.map { MatchItem("${it.id}_L_$stage", it.id, it.hanzi, it.pinyin, true) }.shuffled()
        rightItems = pool.map { MatchItem("${it.id}_R_$stage", it.id, it.meaning, "", false) }.shuffled()
        matchedWordIds = emptySet()
        selectedLeftId = null
        selectedRightId = null
        isCheckingMatch = false
    }

    LaunchedEffect(Unit) {
        loadStage(1)
    }

    // Đếm ngược thời gian 60s
    LaunchedEffect(isGameOver) {
        if (!isGameOver) {
            while (timeLeft > 0) {
                delay(1000)
                timeLeft--
            }
            isGameOver = true
        }
    }

    // Hàm kiểm tra khớp cặp độc lập với Scope để không bị cancel bởi Compose recomposition
    fun triggerCheckMatch(lId: String?, rId: String?) {
        if (lId == null || rId == null || isCheckingMatch) return
        isCheckingMatch = true

        scope.launch {
            val leftItem = leftItems.find { it.id == lId }
            val rightItem = rightItems.find { it.id == rId }

            if (leftItem != null && rightItem != null) {
                if (leftItem.wordId == rightItem.wordId) {
                    // ── ĐÚNG CẶP! ──
                    val newMatchedSet = matchedWordIds + leftItem.wordId
                    matchedWordIds = newMatchedSet
                    score += 10 * combo
                    combo++
                    if (combo > maxCombo) maxCombo = combo
                    matchedPairsCount++
                    selectedLeftId = null
                    selectedRightId = null
                    isCheckingMatch = false

                    // Kiểm tra xem đã nối hết 4 cặp của vòng hiện tại chưa!
                    if (newMatchedSet.size >= leftItems.size && leftItems.isNotEmpty()) {
                        if (currentStage < totalStages) {
                            // ── TỰ ĐỘNG NEXT SANG VÒNG KẾ TIẾP ──
                            score += 20 // Thưởng qua vòng
                            stageNotification = "✨ QUA VÒNG $currentStage! +20 ĐIỂM"
                            delay(500)
                            currentStage++
                            loadStage(currentStage)
                            stageNotification = null
                        } else {
                            // ── HOÀN THÀNH TẤT CẢ CÁC VÒNG ➔ TỰ ĐỘNG TỔNG KẾT CHIẾN THẮNG! ──
                            val timeBonus = timeLeft * 2
                            score += timeBonus + 50
                            isAllStagesCleared = true
                            stageNotification = "🏆 HOÀN THÀNH TOÀN BỘ! +${timeBonus + 50}Đ"
                            delay(600)
                            isGameOver = true
                            stageNotification = null
                        }
                    }
                } else {
                    // ── SAI CẶP! ──
                    errorPair = Pair(lId, rId)
                    combo = 1
                    delay(350)
                    errorPair = null
                    selectedLeftId = null
                    selectedRightId = null
                    isCheckingMatch = false
                }
            } else {
                isCheckingMatch = false
            }
        }
    }

    fun onSelectLeft(item: MatchItem) {
        if (matchedWordIds.contains(item.wordId) || isCheckingMatch) return
        val newLId = if (selectedLeftId == item.id) null else item.id
        selectedLeftId = newLId
        triggerCheckMatch(newLId, selectedRightId)
    }

    fun onSelectRight(item: MatchItem) {
        if (matchedWordIds.contains(item.wordId) || isCheckingMatch) return
        val newRId = if (selectedRightId == item.id) null else item.id
        selectedRightId = newRId
        triggerCheckMatch(selectedLeftId, newRId)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF030712).copy(alpha = 0.88f))
                .padding(horizontal = 14.dp, vertical = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            PixelCardBox(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color(0xFF1E1B4B), // Nền buồng lái Arcade Cyber
                borderColor = PixelColors.BorderDark,
                shadowColor = PixelColors.ShadowDark,
                borderWidth = 1.5.dp,
                shadowOffset = 3.dp,
                cornerRadius = 12.dp,
                headerTitle = "⚔️ SPEED MATCH 60S",
                headerBadge = "VÒNG $currentStage/$totalStages",
                headerColor = PixelColors.PixelYellow,
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
                        // ── BẢNG TỔNG KẾT VICTORY HALL OF FAME ──
                        // ══════════════════════════════════════════════════
                        val rankTitle = when {
                            score >= 200 || isAllStagesCleared -> "👑 HUYỀN THOẠI (RANK S)"
                            score >= 120 -> "⚡ CAO THỦ (RANK A)"
                            else -> "🎮 TÂN BINH (RANK B)"
                        }
                        val rankColor = when {
                            score >= 200 || isAllStagesCleared -> PixelColors.PixelYellow
                            score >= 120 -> PixelColors.PixelBlue
                            else -> PixelColors.PixelGreen
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Huy hiệu danh hiệu
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
                            text = if (isAllStagesCleared) "CHIẾN THẮNG TUYỆT ĐỐI! 🏆" else "HẾT GIỜ THI ĐẤU!",
                            fontFamily = PlusJakartaSans,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            letterSpacing = 0.5.sp
                        )

                        if (isAllStagesCleared) {
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = "Đã vượt qua $totalStages/$totalStages vòng • Thưởng Time Bonus +${timeLeft * 2}đ",
                                fontFamily = PlusJakartaSans,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = PixelColors.PixelYellow
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Bảng điểm High-Score
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

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceAround
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = "GHÉP ĐÚNG",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Black,
                                            color = Color(0xFF94A3B8)
                                        )
                                        Text(
                                            text = "$matchedPairsCount cặp",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Black,
                                            color = Color.White
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

                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = "THỜI GIAN CÒN",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Black,
                                            color = Color(0xFF94A3B8)
                                        )
                                        Text(
                                            text = "${timeLeft}s",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Black,
                                            color = PixelColors.PixelGreen
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Chiến lợi phẩm thưởng
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
                                            text = "⭐ +30 EXP",
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
                                            text = "🪙 +30 COINS",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Black,
                                            color = PixelColors.PixelGreen
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Hai nút hành động Arcade: Đấu lại & Đóng
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                PixelArcadeButton(
                                    text = "✕ RỜI ĐẤU",
                                    color = Color(0xFFE2E8F0),
                                    textColor = PixelColors.BorderDark,
                                    onClick = onDismiss,
                                    height = 44.dp,
                                    fillMaxWidth = true
                                )
                            }

                            Box(modifier = Modifier.weight(1f)) {
                                PixelArcadeButton(
                                    text = "▶ ĐẤU LẠI ⚡",
                                    color = PixelColors.PixelYellow,
                                    textColor = PixelColors.BorderDark,
                                    onClick = {
                                        timeLeft = 60
                                        score = 0
                                        combo = 1
                                        maxCombo = 1
                                        matchedPairsCount = 0
                                        currentStage = 1
                                        isAllStagesCleared = false
                                        isGameOver = false
                                        loadStage(1)
                                    },
                                    height = 44.dp,
                                    fillMaxWidth = true
                                )
                            }
                        }
                    } else {
                        // ══════════════════════════════════════════════════
                        // ── BATTLE HUD: THỜI GIAN, ĐIỂM SỐ, COMBO ──
                        // ══════════════════════════════════════════════════
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Hộp đồng hồ đếm ngược 16-bit
                            val timerBg = if (timeLeft <= 10) PixelColors.PixelRed else PixelColors.CardBg
                            val timerText = if (timeLeft <= 10) Color.White else PixelColors.BorderDark
                            PixelCardBox(
                                backgroundColor = timerBg,
                                borderColor = PixelColors.BorderDark,
                                shadowColor = PixelColors.ShadowDark,
                                borderWidth = 1.2.dp,
                                shadowOffset = 2.dp,
                                cornerRadius = 4.dp,
                                showRivets = false
                            ) {
                                Text(
                                    text = "⏱️ ${timeLeft}S",
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black,
                                    color = timerText,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }

                            // Điểm số & Combo
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                if (combo > 1) {
                                    PixelCardBox(
                                        backgroundColor = PixelColors.PixelOrange,
                                        borderColor = PixelColors.BorderDark,
                                        shadowColor = PixelColors.ShadowDark,
                                        borderWidth = 1.dp,
                                        shadowOffset = 1.5.dp,
                                        cornerRadius = 3.dp,
                                        showRivets = false
                                    ) {
                                        Text(
                                            text = "COMBO x$combo 🔥",
                                            fontFamily = PlusJakartaSans,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Black,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                PixelCardBox(
                                    backgroundColor = PixelColors.PixelYellow,
                                    borderColor = PixelColors.BorderDark,
                                    shadowColor = PixelColors.ShadowDark,
                                    borderWidth = 1.2.dp,
                                    shadowOffset = 2.dp,
                                    cornerRadius = 4.dp,
                                    showRivets = false
                                ) {
                                    Text(
                                        text = "$score PTS",
                                        fontFamily = PlusJakartaSans,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Black,
                                        color = PixelColors.BorderDark,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Thanh đo vạch thời gian rút ngắn
                        val progressFraction = (timeLeft / 60f).coerceIn(0f, 1f)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(Color(0xFF334155))
                                .border(BorderStroke(1.dp, PixelColors.BorderDark), RoundedCornerShape(3.dp))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(progressFraction)
                                    .height(6.dp)
                                    .background(
                                        if (timeLeft <= 10) PixelColors.PixelRed
                                        else PixelColors.PixelGreen
                                    )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Thông báo chuyển vòng / Hướng dẫn
                        if (stageNotification != null) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(PixelColors.PixelGreen)
                                    .border(BorderStroke(1.2.dp, PixelColors.BorderDark), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 10.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = stageNotification!!,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    color = PixelColors.BorderDark
                                )
                            }
                        } else {
                            Text(
                                text = "CHẠM 1 CHỮ HÁN  ➔  1 NGHĨA TƯƠNG ỨNG",
                                fontFamily = PlusJakartaSans,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFC7D2FE),
                                letterSpacing = 0.5.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // ══════════════════════════════════════════════════
                        // ── SÀN ĐẤU 2 CỘT: CHỮ HÁN (TRÁI) & NGHĨA (PHẢI) ──
                        // ══════════════════════════════════════════════════
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // CỘT TRÁI: CHỮ HÁN
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                leftItems.forEach { item ->
                                    val isMatched = matchedWordIds.contains(item.wordId)
                                    val isSelected = selectedLeftId == item.id
                                    val isError = errorPair?.first == item.id

                                    MatchCartridgeButton(
                                        text = item.text,
                                        subText = item.subText,
                                        isMatched = isMatched,
                                        isSelected = isSelected,
                                        isError = isError,
                                        activeColor = PixelColors.PixelYellow,
                                        activeTextColor = PixelColors.BorderDark,
                                        onClick = {
                                            if (!isMatched && errorPair == null) {
                                                onSelectLeft(item)
                                            }
                                        }
                                    )
                                }
                            }

                            // CỘT PHẢI: NGHĨA TIẾNG VIỆT
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                rightItems.forEach { item ->
                                    val isMatched = matchedWordIds.contains(item.wordId)
                                    val isSelected = selectedRightId == item.id
                                    val isError = errorPair?.second == item.id

                                    MatchCartridgeButton(
                                        text = item.text,
                                        subText = "",
                                        isMatched = isMatched,
                                        isSelected = isSelected,
                                        isError = isError,
                                        activeColor = PixelColors.PixelPurple,
                                        activeTextColor = Color.White,
                                        onClick = {
                                            if (!isMatched && errorPair == null) {
                                                onSelectRight(item)
                                            }
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Nút thoát nhanh
                        Text(
                            text = "✕ TẠM DỪNG / THOÁT",
                            fontFamily = PlusJakartaSans,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF94A3B8),
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .clickable { onDismiss() }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Viên gạch phím bấm Arcade Cartridge trong sàn đấu:
 * - Dập nổi viền đen, bóng cứng 3D.
 * - Khi được chọn (isSelected): Nền chuyển màu active rực rỡ, phím dập thụt xuống xúc giác.
 * - Khi ghép đúng (isMatched): Nền xanh lá 1-UP, hiển thị dấu tích ✓.
 * - Khi ghép sai (isError): Nền đỏ cảnh báo ✖.
 */
@Composable
private fun MatchCartridgeButton(
    text: String,
    subText: String,
    isMatched: Boolean,
    isSelected: Boolean,
    isError: Boolean,
    activeColor: Color,
    activeTextColor: Color,
    onClick: () -> Unit
) {
    val bgColor = when {
        isError -> PixelColors.PixelRed
        isMatched -> PixelColors.CardGreen
        isSelected -> activeColor
        else -> PixelColors.CardBg
    }

    val textColor = when {
        isError -> Color.White
        isMatched -> Color(0xFF065F46)
        isSelected -> activeTextColor
        else -> PixelColors.BorderDark
    }

    val shadowOffset = if (isSelected) 0.5.dp else 2.5.dp
    val shape = PixelStepCornerShape(2.5.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .bounceClick(scaleDown = if (isMatched) 1f else 0.94f, onClick = onClick)
    ) {
        // Lớp bóng cứng 3D phía sau màu kim loại Slate
        if (!isMatched) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .offset(x = shadowOffset, y = shadowOffset)
                    .clip(shape)
                    .background(PixelColors.ShadowDark)
                    .border(BorderStroke(1.2.dp, PixelColors.BorderDark), shape)
            )
        }

        // Mặt trước phím bấm
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(shape)
                .background(bgColor)
                .border(
                    BorderStroke(
                        1.5.dp,
                        if (isMatched) Color(0xFF059669) else PixelColors.BorderDark
                    ),
                    shape
                )
                .padding(horizontal = 6.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            // Đường gờ Bevel phản quang trên nóc phím
            if (!isMatched) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .padding(top = 1.dp, start = 1.dp, end = 1.dp)
                        .border(
                            BorderStroke(1.dp, Color.White.copy(alpha = if (isSelected) 0.35f else 0.55f)),
                            PixelStepCornerShape(2.dp)
                        )
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (isMatched) {
                        Text(
                            text = "✓ ",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF059669)
                        )
                    }
                    Text(
                        text = text,
                        fontSize = if (subText.isNotBlank()) 16.5.sp else 12.sp,
                        fontWeight = FontWeight.Black,
                        color = textColor,
                        maxLines = 2,
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (subText.isNotBlank()) {
                    Text(
                        text = subText,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) textColor.copy(alpha = 0.85f) else Color(0xFF6B7280),
                        maxLines = 1
                    )
                }
            }
        }
    }
}
