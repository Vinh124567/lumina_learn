package com.example.luminalearn.presentation.spark_ai_lab.component

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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.presentation.vocabulary.component.PixelArcadeButton
import com.example.luminalearn.presentation.vocabulary.component.PixelBadge
import com.example.luminalearn.presentation.vocabulary.component.PixelCardBox
import com.example.luminalearn.presentation.vocabulary.component.PixelColors
import com.example.luminalearn.presentation.vocabulary.component.PixelStepCornerShape

data class RoleplayScenarioData(
    val id: String,
    val iconEmoji: String,
    val title: String,
    val pinyinTitle: String,
    val levelCategory: String, // "hsk12", "hsk23", "hsk34"
    val levelLabel: String,
    val levelColor: Color,
    val description: String,
    val aiRole: String,
    val userRole: String,
    val bonusSparks: Int = 30
)

val DEFAULT_SCENARIOS = listOf(
    RoleplayScenarioData(
        id = "heytea",
        iconEmoji = "🧋",
        title = "Gọi trà sữa HeyTea",
        pinyinTitle = "点喜茶 (Diǎn Xǐchá)",
        levelCategory = "hsk12",
        levelLabel = "HSK 1–2 Cơ bản",
        levelColor = Color(0xFF22C55E),
        description = "Luyện chọn mức đường (半糖), đá (少冰) và gọi thêm topping trân châu bản xứ.",
        aiRole = "Nhân viên HeyTea",
        userRole = "Khách hàng"
    ),
    RoleplayScenarioData(
        id = "hotpot",
        iconEmoji = "🍲",
        title = "Gọi món lẩu Tứ Xuyên",
        pinyinTitle = "吃四川火锅 (Chī Sìchuān Huǒguō)",
        levelCategory = "hsk23",
        levelLabel = "HSK 2–3 Giao tiếp",
        levelColor = Color(0xFF3B82F6),
        description = "Chọn vị nước lẩu uyên ương (鸳鸯锅), gọi món nhúng thịt bò và pha sốt mè chuẩn vị.",
        aiRole = "Phục vụ bàn",
        userRole = "Thực khách"
    ),
    RoleplayScenarioData(
        id = "taxi",
        iconEmoji = "🚕",
        title = "Bắt taxi tại Bắc Kinh",
        pinyinTitle = "在打车 (Zài Dǎchē)",
        levelCategory = "hsk23",
        levelLabel = "HSK 2–3 Giao tiếp",
        levelColor = Color(0xFF3B82F6),
        description = "Chỉ dẫn tài xế tới điểm hẹn, hỏi giá cước và xin xuất hóa đơn (发票).",
        aiRole = "Tài xế bản xứ",
        userRole = "Hành khách"
    ),
    RoleplayScenarioData(
        id = "shopping",
        iconEmoji = "🛍️",
        title = "Trả giá mua sắm chợ đêm",
        pinyinTitle = "夜市砍价 (Yèshì Kǎnjià)",
        levelCategory = "hsk34",
        levelLabel = "HSK 3–4 Nâng cao",
        levelColor = Color(0xFFF97316),
        description = "Mặc cả khéo léo khi mua đồ lưu niệm, áo quần và thanh toán qua WeChat Pay.",
        aiRole = "Chủ sạp hàng",
        userRole = "Người mua"
    )
)

@Composable
fun RoleplayScenarioCard(
    scenario: RoleplayScenarioData,
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val headerColor = when (scenario.levelCategory) {
        "hsk12" -> PixelColors.PixelGreen
        "hsk23" -> PixelColors.PixelBlue
        else -> PixelColors.PixelOrange
    }

    PixelCardBox(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = Color(0xFFFFFBEB), // Kem phiêu lưu RPG ấm áp
        headerTitle = "${scenario.iconEmoji} ${scenario.title.uppercase()}",
        headerBadge = scenario.levelLabel,
        headerColor = headerColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            // 1. Tiêu đề Hán tự & Phiên âm Pinyin lớn, sắc nét
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = scenario.pinyinTitle,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = PixelColors.BorderDark,
                    letterSpacing = 0.3.sp
                )

                // Huy hiệu cấp độ HSK mini
                PixelBadge(
                    text = when (scenario.levelCategory) {
                        "hsk12" -> "HSK 1-2"
                        "hsk23" -> "HSK 2-3"
                        else -> "HSK 3-4"
                    },
                    backgroundColor = headerColor,
                    textColor = Color.White
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // 2. Khung Mô tả Nhiệm vụ Giao tiếp
            val descShape = PixelStepCornerShape(2.dp)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(descShape)
                    .background(Color.White)
                    .border(BorderStroke(1.dp, Color(0xFFFDE68A)), descShape)
                    .padding(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        text = "🎯 NHIỆM VỤ:",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFB45309),
                        letterSpacing = 0.3.sp
                    )
                    Text(
                        text = scenario.description,
                        fontSize = 11.sp,
                        color = Color(0xFF78350F),
                        lineHeight = 15.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 3. Phân vai Hội thoại Đối kháng (Roleplay Co-op Duel Grid)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Vai AI
                val roleAiShape = PixelStepCornerShape(1.5.dp)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(roleAiShape)
                        .background(Color(0xFFEDE9FE))
                        .border(BorderStroke(1.dp, Color(0xFFDDD6FE)), roleAiShape)
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "🤖 AI: ",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF6D28D9)
                        )
                        Text(
                            text = scenario.aiRole,
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4C1D95),
                            maxLines = 1
                        )
                    }
                }

                // Vai Bạn
                val roleUserShape = PixelStepCornerShape(1.5.dp)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(roleUserShape)
                        .background(Color(0xFFFEF3C7))
                        .border(BorderStroke(1.dp, Color(0xFFFDE68A)), roleUserShape)
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "🧑 BẠN: ",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFB45309)
                        )
                        Text(
                            text = scenario.userRole,
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF78350F),
                            maxLines = 1
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 4. Chân thẻ: Chiến lợi phẩm + Nút Arcade Bắt đầu
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Phần thưởng Sparks
                val rewardShape = PixelStepCornerShape(1.5.dp)
                Box(
                    modifier = Modifier
                        .clip(rewardShape)
                        .background(Color(0xFFFEF08A))
                        .border(BorderStroke(1.dp, Color(0xFFCA8A04)), rewardShape)
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text(text = "⚡", fontSize = 9.sp)
                        Text(
                            text = "+${scenario.bonusSparks} SPARKS",
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF713F12)
                        )
                    }
                }

                // Nút hành động Arcade nảy 3D
                PixelArcadeButton(
                    text = "VÀO VAI ▶",
                    color = PixelColors.PixelBlue,
                    onClick = onStartClick
                )
            }
        }
    }
}
