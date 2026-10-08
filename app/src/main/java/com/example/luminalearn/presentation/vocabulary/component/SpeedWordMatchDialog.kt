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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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

private val BrandIndigo = Color(0xFF6366F1)
private val CorrectGreen = Color(0xFF10B981)
private val ErrorRed = Color(0xFFEF4444)
private val GoldAccent = Color(0xFFFEF08A)

private data class MatchItem(
    val id: String,
    val wordId: String,
    val text: String,
    val subText: String = "",
    val isHanzi: Boolean
)

/**
 * Dialog Sàn đấu phản xạ ghép từ 60s tương tác:
 * - Đếm ngược 60 giây.
 * - Chọn 1 chữ Hán và 1 nghĩa: Đúng thì nổ điểm + combo, sai thì trừ combo.
 * - Bảng vinh danh kết quả khi kết thúc.
 */
@Composable
fun SpeedWordMatchDialog(
    isOpen: Boolean,
    vocabList: List<VocabWordItem>,
    onDismiss: () -> Unit
) {
    if (!isOpen) return

    val sampleVocabs = remember(vocabList) {
        if (vocabList.size >= 4) vocabList.shuffled().take(12)
        else listOf(
            VocabWordItem("1", "你好", "nǐ hǎo", "Nhĩ hảo", "Xin chào", "Thán từ", "Giao tiếp", "", "", "", "", "", "HSK 1"),
            VocabWordItem("2", "谢谢", "xiè xie", "Tạ tạ", "Cảm ơn", "Động từ", "Giao tiếp", "", "", "", "", "", "HSK 1"),
            VocabWordItem("3", "再见", "zài jiàn", "Tái kiến", "Tạm biệt", "Thán từ", "Giao tiếp", "", "", "", "", "", "HSK 1"),
            VocabWordItem("4", "朋友", "péng you", "Bằng hữu", "Bạn bè", "Danh từ", "Đời sống", "", "", "", "", "", "HSK 1"),
            VocabWordItem("5", "高兴", "gāo xìng", "Cao hứng", "Vui vẻ", "Tính từ", "Cảm xúc", "", "", "", "", "", "HSK 1"),
            VocabWordItem("6", "学习", "xué xí", "Học tập", "Học tập", "Động từ", "Giáo dục", "", "", "", "", "", "HSK 1")
        )
    }

    var timeLeft by remember { mutableIntStateOf(60) }
    var score by remember { mutableIntStateOf(0) }
    var combo by remember { mutableIntStateOf(1) }
    var matchedPairsCount by remember { mutableIntStateOf(0) }
    var isGameOver by remember { mutableStateOf(false) }

    // Danh sách 4 cặp từ hiện tại đang thi đấu
    var currentRoundIndex by remember { mutableIntStateOf(0) }
    var leftItems by remember { mutableStateOf<List<MatchItem>>(emptyList()) }
    var rightItems by remember { mutableStateOf<List<MatchItem>>(emptyList()) }

    var selectedLeftId by remember { mutableStateOf<String?>(null) }
    var selectedRightId by remember { mutableStateOf<String?>(null) }
    var matchedWordIds by remember { mutableStateOf<Set<String>>(emptySet()) }
    var errorPair by remember { mutableStateOf<Pair<String, String>?>(null) }

    fun loadNewRound() {
        val pool = sampleVocabs.shuffled().take(4)
        leftItems = pool.map { MatchItem(it.id + "_L", it.id, it.hanzi, it.pinyin, true) }
        rightItems = pool.map { MatchItem(it.id + "_R", it.id, it.meaning, "", false) }.shuffled()
        matchedWordIds = emptySet()
        selectedLeftId = null
        selectedRightId = null
    }

    LaunchedEffect(Unit) {
        loadNewRound()
    }

    // Đếm ngược thời gian
    LaunchedEffect(isGameOver) {
        if (!isGameOver) {
            while (timeLeft > 0) {
                delay(1000)
                timeLeft--
            }
            isGameOver = true
        }
    }

    // Xử lý logic ghép cặp
    LaunchedEffect(selectedLeftId, selectedRightId) {
        val lId = selectedLeftId
        val rId = selectedRightId
        if (lId != null && rId != null) {
            val leftItem = leftItems.find { it.id == lId }
            val rightItem = rightItems.find { it.id == rId }

            if (leftItem != null && rightItem != null) {
                if (leftItem.wordId == rightItem.wordId) {
                    // ĐÚNG CẶP!
                    matchedWordIds = matchedWordIds + leftItem.wordId
                    score += 10 * combo
                    combo++
                    matchedPairsCount++
                    selectedLeftId = null
                    selectedRightId = null

                    // Nếu đã ghép hết 4 cặp của round này -> sang round mới
                    if (matchedWordIds.size >= 4) {
                        delay(250)
                        loadNewRound()
                    }
                } else {
                    // SAI CẶP!
                    errorPair = Pair(lId, rId)
                    combo = 1
                    delay(350)
                    errorPair = null
                    selectedLeftId = null
                    selectedRightId = null
                }
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
                .background(Color(0xFF0F172A).copy(alpha = 0.95f))
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                border = BorderStroke(1.dp, Color(0xFFA855F7).copy(alpha = 0.5f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0xFF1E1B4B),
                                    Color(0xFF161131),
                                    Color(0xFF0F172A)
                                )
                            )
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (isGameOver) {
                            // ── BẢNG TỔNG KẾT KHI HẾT GIỜ ──
                            Text(text = "🎉", fontSize = 42.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Hết Giờ Thi Đấu!",
                                fontFamily = PlusJakartaSans,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(14.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color.White.copy(alpha = 0.10f))
                                    .padding(16.dp)
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = "$score ĐIỂM",
                                        fontFamily = PlusJakartaSans,
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Black,
                                        color = GoldAccent
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Ghép đúng: $matchedPairsCount cặp từ vựng",
                                        fontFamily = PlusJakartaSans,
                                        fontSize = 13.sp,
                                        color = Color(0xFFCBD5E1)
                                    )
                                    Text(
                                        text = "Thưởng hoàn thành: +30 Sparks ⚡",
                                        fontFamily = PlusJakartaSans,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF38BDF8)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(42.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color.White.copy(alpha = 0.15f))
                                        .bounceClick { onDismiss() },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Đóng",
                                        fontFamily = PlusJakartaSans,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(42.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Brush.horizontalGradient(listOf(Color(0xFF7C3AED), Color(0xFFDB2777))))
                                        .bounceClick {
                                            timeLeft = 60
                                            score = 0
                                            combo = 1
                                            matchedPairsCount = 0
                                            isGameOver = false
                                            loadNewRound()
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Đấu lại ⚡",
                                        fontFamily = PlusJakartaSans,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        } else {
                            // ── MÀN HÌNH CHƠI GAME ĐANG DIỄN RA ──
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Thời gian còn lại
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(
                                            if (timeLeft <= 10) ErrorRed.copy(alpha = 0.25f)
                                            else Color.White.copy(alpha = 0.12f)
                                        )
                                        .border(
                                            BorderStroke(
                                                0.5.dp,
                                                if (timeLeft <= 10) ErrorRed else Color.White.copy(alpha = 0.3f)
                                            ),
                                            RoundedCornerShape(10.dp)
                                        )
                                        .padding(horizontal = 10.dp, vertical = 5.dp)
                                ) {
                                    Text(
                                        text = "⏱️ ${timeLeft}s",
                                        fontFamily = PlusJakartaSans,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = if (timeLeft <= 10) ErrorRed else Color.White
                                    )
                                }

                                // Điểm số & Combo
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "$score đ",
                                        fontFamily = PlusJakartaSans,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Black,
                                        color = GoldAccent
                                    )
                                    if (combo > 1) {
                                        Text(
                                            text = "COMBO x$combo 🔥",
                                            fontFamily = PlusJakartaSans,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color(0xFFFB923C)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Chạm 1 chữ Hán và 1 nghĩa tương ứng",
                                fontFamily = PlusJakartaSans,
                                fontSize = 11.5.sp,
                                color = Color(0xFFCBD5E1)
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // ── 2 CỘT GHÉP CẶP TỪ VỰNG ──
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
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

                                        val cardBg = when {
                                            isMatched -> CorrectGreen.copy(alpha = 0.20f)
                                            isError -> ErrorRed.copy(alpha = 0.25f)
                                            isSelected -> BrandIndigo.copy(alpha = 0.35f)
                                            else -> Color.White.copy(alpha = 0.10f)
                                        }

                                        val cardBorder = when {
                                            isMatched -> CorrectGreen
                                            isError -> ErrorRed
                                            isSelected -> Color(0xFFA855F7)
                                            else -> Color.White.copy(alpha = 0.15f)
                                        }

                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(54.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(cardBg)
                                                .border(BorderStroke(1.dp, cardBorder), RoundedCornerShape(12.dp))
                                                .clickable(enabled = !isMatched) {
                                                    selectedLeftId = item.id
                                                }
                                                .padding(horizontal = 8.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Text(
                                                    text = item.text,
                                                    fontSize = 17.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isMatched) CorrectGreen else Color.White
                                                )
                                                if (item.subText.isNotBlank()) {
                                                    Text(
                                                        text = item.subText,
                                                        fontFamily = PlusJakartaSans,
                                                        fontSize = 9.sp,
                                                        color = Color(0xFF94A3B8)
                                                    )
                                                }
                                            }
                                        }
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

                                        val cardBg = when {
                                            isMatched -> CorrectGreen.copy(alpha = 0.20f)
                                            isError -> ErrorRed.copy(alpha = 0.25f)
                                            isSelected -> BrandIndigo.copy(alpha = 0.35f)
                                            else -> Color.White.copy(alpha = 0.10f)
                                        }

                                        val cardBorder = when {
                                            isMatched -> CorrectGreen
                                            isError -> ErrorRed
                                            isSelected -> Color(0xFFA855F7)
                                            else -> Color.White.copy(alpha = 0.15f)
                                        }

                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(54.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(cardBg)
                                                .border(BorderStroke(1.dp, cardBorder), RoundedCornerShape(12.dp))
                                                .clickable(enabled = !isMatched) {
                                                    selectedRightId = item.id
                                                }
                                                .padding(horizontal = 8.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = item.text,
                                                fontFamily = PlusJakartaSans,
                                                fontSize = 11.5.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = if (isMatched) CorrectGreen else Color.White,
                                                textAlign = TextAlign.Center,
                                                maxLines = 2,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Đóng",
                                fontFamily = PlusJakartaSans,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF94A3B8),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { onDismiss() }
                                    .padding(horizontal = 14.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
