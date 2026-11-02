package com.example.luminalearn.presentation.main.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.presentation.vocabulary.component.PixelArcadeButton
import com.example.luminalearn.presentation.vocabulary.component.PixelCardBox
import com.example.luminalearn.presentation.vocabulary.component.PixelColors

/**
 * Thẻ Thử Thách Hằng Ngày (Spark Challenge) chuẩn Pixel Retro Arcade 16-Bit:
 * - Đầy đặn, sống động với mục tiêu thử thách (Quest Target), cặp thanh điệu đối chiếu trực quan.
 * - Thanh tiến độ EXP nhiệm vụ RPG.
 * - Huy hiệu phần thưởng vàng + sao kinh nghiệm.
 */
/**
 * Thẻ Thử Thách Hằng Ngày (Spark Challenge) chuẩn Pixel Retro Arcade 16-Bit:
 * - Võ đài thanh điệu đối kháng (Tone VS Battle Arena) trực quan với ký hiệu hướng âm.
 * - Thanh tiến độ nhiệm vụ chia 5 ô pixel rõ ràng.
 * - Cụm chiến lợi phẩm EXP + Vàng dập nổi cạnh nút Arcade hoàn thành.
 */
@Composable
fun SparkChallengeCard(
    modifier: Modifier = Modifier,
    bonusSparks: Int = 20,
    title: String = stringResource(R.string.challenge_title),
    description: String = stringResource(R.string.challenge_desc),
    onCompleteClick: () -> Unit = {}
) {
    PixelCardBox(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = Color(0xFFFFF7ED), // Kem cam phiêu lưu cổ điển
        headerTitle = "🔥 DAILY BOUNTY QUEST",
        headerBadge = "+$bonusSparks G",
        headerColor = PixelColors.PixelOrange
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            // 1. Tóm tắt Mục tiêu Nhiệm vụ (Quest Mission Briefing)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🎯 PHÂN BIỆT THANH ĐIỆU PINYIN",
                    color = Color(0xFF9A3412),
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                )

                Box(
                    modifier = Modifier
                        .clip(com.example.luminalearn.presentation.vocabulary.component.PixelStepCornerShape(1.dp))
                        .background(Color(0xFFFFEDD5))
                        .border(
                            BorderStroke(1.dp, Color(0xFFF97316)),
                            com.example.luminalearn.presentation.vocabulary.component.PixelStepCornerShape(1.dp)
                        )
                        .padding(horizontal = 5.dp, vertical = 1.5.dp)
                ) {
                    Text(
                        text = "HSK 1-2 • 5 CẶP",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFC2410C)
                    )
                }
            }

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = "Luyện tai phản xạ phân biệt cặp thanh điệu đối nghịch để mở khóa kho báu.",
                color = Color(0xFF78350F),
                fontSize = 11.5.sp,
                lineHeight = 15.5.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 2. Võ Đài Đối Đầu Thanh Điệu (Tone VS Battle Arena - Không lồng thêm CardBox viền đen)
            val arenaShape = com.example.luminalearn.presentation.vocabulary.component.PixelStepCornerShape(2.5.dp)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(arenaShape)
                    .background(Color(0xFFFEF3C7)) // Giấy da vàng ấm
                    .border(BorderStroke(1.2.dp, Color(0xFFF59E0B)), arenaShape)
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Cặp đấu 1: mā (Thanh 1) vs má (Thanh 2)
                    ToneVsDuelItem(
                        modifier = Modifier.weight(1f),
                        pairName = "Cặp A: Ngang vs Lên",
                        soundA = "mā",
                        symbolA = "—",
                        typeA = "Thanh 1",
                        soundB = "má",
                        symbolB = "↗",
                        typeB = "Thanh 2"
                    )

                    // Cặp đấu 2: mǎ (Thanh 3) vs mà (Thanh 4)
                    ToneVsDuelItem(
                        modifier = Modifier.weight(1f),
                        pairName = "Cặp B: Uốn vs Xuống",
                        soundA = "mǎ",
                        symbolA = "∨",
                        typeA = "Thanh 3",
                        soundB = "mà",
                        symbolB = "↘",
                        typeB = "Thanh 4"
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 3. Thanh Tiến Độ Nhiệm Vụ 5 Ô Pixel (Segmented Quest Meter)
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "TIẾN ĐỘ THỬ THÁCH",
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF78350F),
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "2 / 5 CẶP (40%)",
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFEA580C)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Thanh 5 ô gạch pixel dập nổi
                val progressShape = com.example.luminalearn.presentation.vocabulary.component.PixelStepCornerShape(1.5.dp)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .clip(progressShape)
                        .background(Color(0xFFFED7AA))
                        .border(BorderStroke(1.2.dp, PixelColors.BorderDark), progressShape)
                        .padding(1.5.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        for (i in 0 until 5) {
                            val isCompleted = i < 2
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .background(
                                        if (isCompleted) PixelColors.PixelOrange else Color(0xFFFFEDD5)
                                    )
                                    .border(
                                        BorderStroke(0.5.dp, if (isCompleted) Color(0xFFC2410C) else Color(0xFFFDBA74))
                                    )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 4. Chân thẻ: Chiến lợi phẩm + Nút Hành Động Arcade
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Huy hiệu chiến lợi phẩm
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    PixelQuestRewardBadge(icon = "⭐", text = "+$bonusSparks EXP")
                    PixelQuestRewardBadge(icon = "🪙", text = "+$bonusSparks G")
                }

                // Nút hành động Arcade
                PixelArcadeButton(
                    text = "HOÀN THÀNH ▶",
                    color = PixelColors.PixelOrange,
                    onClick = onCompleteClick
                )
            }
        }
    }
}

@Composable
private fun ToneVsDuelItem(
    pairName: String,
    soundA: String,
    symbolA: String,
    typeA: String,
    soundB: String,
    symbolB: String,
    typeB: String,
    modifier: Modifier = Modifier
) {
    val boxShape = com.example.luminalearn.presentation.vocabulary.component.PixelStepCornerShape(2.dp)
    Box(
        modifier = modifier
            .clip(boxShape)
            .background(Color.White)
            .border(BorderStroke(1.dp, Color(0xFFFDE68A)), boxShape)
            .padding(6.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = pairName,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF92400E)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Âm A
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = soundA,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = PixelColors.BorderDark
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = symbolA,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF2563EB)
                        )
                    }
                    Text(
                        text = typeA,
                        fontSize = 7.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF6B7280)
                    )
                }

                // Badge VS Retro
                Box(
                    modifier = Modifier
                        .clip(com.example.luminalearn.presentation.vocabulary.component.PixelStepCornerShape(1.dp))
                        .background(Color(0xFFEF4444))
                        .padding(horizontal = 3.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = "VS",
                        fontSize = 7.5.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }

                // Âm B
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = soundB,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = PixelColors.BorderDark
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = symbolB,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFEA580C)
                        )
                    }
                    Text(
                        text = typeB,
                        fontSize = 7.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF6B7280)
                    )
                }
            }
        }
    }
}

@Composable
private fun PixelQuestRewardBadge(
    icon: String,
    text: String,
    modifier: Modifier = Modifier
) {
    val shape = com.example.luminalearn.presentation.vocabulary.component.PixelStepCornerShape(1.5.dp)
    Box(
        modifier = modifier
            .clip(shape)
            .background(Color(0xFFFEF08A))
            .border(BorderStroke(1.dp, Color(0xFFCA8A04)), shape)
            .padding(horizontal = 5.dp, vertical = 3.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(text = icon, fontSize = 9.sp)
            Text(
                text = text,
                fontSize = 9.5.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF713F12)
            )
        }
    }
}
