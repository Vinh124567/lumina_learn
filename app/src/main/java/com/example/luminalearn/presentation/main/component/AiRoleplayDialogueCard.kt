package com.example.luminalearn.presentation.main.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.presentation.vocabulary.component.PixelArcadeButton
import com.example.luminalearn.presentation.vocabulary.component.PixelCardBox
import com.example.luminalearn.presentation.vocabulary.component.PixelColors

/**
 * Thẻ Hội thoại Tình huống Nhập vai Lumina AI phong cách Pixel Retro Arcade 16-Bit:
 * - Khung hiển thị JRPG Terminal Dialogue Window trực quan, chữ Hán và Pinyin sắc nét.
 * - Mini Cartridge Chips chọn chủ đề phản xạ có dập nổi.
 * - Chân thẻ chuẩn arcade với trạng thái mic live và nút bấm nảy cơ học.
 */
@Composable
fun AiRoleplayDialogueCard(
    modifier: Modifier = Modifier,
    onStartRoleplayClick: () -> Unit = {}
) {
    PixelCardBox(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = PixelColors.CardBlue, // Sky Blue Arcade tươi sáng, không tím than
        headerTitle = "🤖 LUMINA AI DIALOGUE",
        headerBadge = "● LIVE 1:1",
        headerColor = PixelColors.PixelBlue
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            // 1. Khung Hội Thoại Game Nhập Vai JRPG Terminal
            val terminalShape = com.example.luminalearn.presentation.vocabulary.component.PixelStepCornerShape(2.5.dp)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(terminalShape)
                    .background(Color.White)
                    .border(BorderStroke(1.2.dp, PixelColors.BorderDark), terminalShape)
                    .padding(10.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Thanh trạng thái Terminal Bot
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "🤖 LUMINA BOT",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = PixelColors.PixelBlue,
                                letterSpacing = 0.5.sp
                            )
                            Box(
                                modifier = Modifier
                                    .clip(com.example.luminalearn.presentation.vocabulary.component.PixelStepCornerShape(1.dp))
                                    .background(Color(0xFFE0E7FF))
                                    .padding(horizontal = 5.dp, vertical = 1.5.dp)
                            ) {
                                Text(
                                    text = "BẢN XỨ",
                                    fontSize = 7.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF3730A3)
                                )
                            }
                        }

                        // Trạng thái mic realtime
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(com.example.luminalearn.presentation.vocabulary.component.PixelStepCornerShape(1.dp))
                                    .background(Color(0xFF16A34A))
                            )
                            Text(
                                text = "VOICE LIVE 🎙️",
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF16A34A)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Lời thoại nhân vật NPC
                    Text(
                        text = "“你好！想喝什么奶茶？加冰吗？”",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = PixelColors.BorderDark,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "Nǐ hǎo! Xiǎng hē shénme nǎichá? Jiā bīng ma?",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD97706) // Vàng hổ phách retro
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "💬 Xin chào! Bạn muốn uống trà sữa gì? Thêm đá không?",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF4B5563)
                        )

                        // Con trỏ RPG nhấp nháy chờ tương tác
                        Text(
                            text = "▼",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            color = PixelColors.PixelBlue
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 2. Danh sách chủ đề tình huống phản xạ (Arcade Scenario Chips)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                PixelScenarioChip(
                    modifier = Modifier.weight(1f),
                    icon = "🧋",
                    title = "Trà sữa",
                    subtitle = "Gọi món"
                )
                PixelScenarioChip(
                    modifier = Modifier.weight(1f),
                    icon = "🚕",
                    title = "Đi Taxi",
                    subtitle = "Chỉ đường"
                )
                PixelScenarioChip(
                    modifier = Modifier.weight(1f),
                    icon = "🍜",
                    title = "Nhà hàng",
                    subtitle = "Thanh toán"
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 3. Chân thẻ: Chỉ báo hỗ trợ + Nút Bắt đầu Thoại
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(Color(0xFF10B981), com.example.luminalearn.presentation.vocabulary.component.PixelStepCornerShape(1.dp))
                    )
                    Text(
                        text = "Sửa ngữ pháp & phát âm tức thì",
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF334155)
                    )
                }

                PixelArcadeButton(
                    text = "BẮT ĐẦU THOẠI ▶",
                    color = PixelColors.PixelBlue,
                    onClick = onStartRoleplayClick
                )
            }
        }
    }
}

@Composable
private fun PixelScenarioChip(
    icon: String,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    val chipShape = com.example.luminalearn.presentation.vocabulary.component.PixelStepCornerShape(2.dp)
    Box(
        modifier = modifier
            .clip(chipShape)
            .background(Color.White)
            .border(BorderStroke(1.dp, PixelColors.BorderDark), chipShape)
            .padding(horizontal = 4.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(text = icon, fontSize = 11.sp)
                Text(
                    text = title,
                    color = PixelColors.BorderDark,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Black,
                    maxLines = 1
                )
            }
            Text(
                text = subtitle,
                color = Color(0xFF64748B),
                fontSize = 8.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
        }
    }
}
