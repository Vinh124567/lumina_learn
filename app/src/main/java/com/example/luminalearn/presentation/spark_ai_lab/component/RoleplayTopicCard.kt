package com.example.luminalearn.presentation.spark_ai_lab.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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

/**
 * Model dữ liệu cho Thẻ Chủ Đề Luyện Thoại (Topic Category).
 */
data class RoleplayTopicData(
    val id: String,
    val iconEmoji: String,
    val title: String,
    val chineseTitle: String,
    val pinyinTitle: String,
    val description: String,
    val levelBadge: String,
    val headerColor: Color,
    val cardBgColor: Color,
    val scenarios: List<RoleplayScenarioData>
)

val DEFAULT_TOPICS = listOf(
    RoleplayTopicData(
        id = "food_drink",
        iconEmoji = "🧋",
        title = "Ẩm thực & Quán xá",
        chineseTitle = "餐饮美食",
        pinyinTitle = "Cānyǐn Měishí",
        description = "Gọi trà sữa HeyTea, lẩu Tứ Xuyên, gọi món & xin nước sốt",
        levelBadge = "2 Kịch bản • HSK 1–3",
        headerColor = PixelColors.PixelYellow,
        cardBgColor = Color(0xFFFFFBEB),
        scenarios = listOf(
            DEFAULT_SCENARIOS[0], // heytea
            DEFAULT_SCENARIOS[1]  // hotpot
        )
    ),
    RoleplayTopicData(
        id = "transport_travel",
        iconEmoji = "🚕",
        title = "Di chuyển & Du lịch",
        chineseTitle = "交通出行",
        pinyinTitle = "Jiāotōng Chūxíng",
        description = "Bắt taxi Bắc Kinh, chỉ đường, hỏi giá cước & hóa đơn",
        levelBadge = "1 Kịch bản • HSK 2–3",
        headerColor = PixelColors.PixelBlue,
        cardBgColor = PixelColors.CardBlue,
        scenarios = listOf(
            DEFAULT_SCENARIOS[2] // taxi
        )
    ),
    RoleplayTopicData(
        id = "shopping_life",
        iconEmoji = "🛍️",
        title = "Mua sắm & Chợ đêm",
        chineseTitle = "夜市购物",
        pinyinTitle = "Yèshì Gòuwù",
        description = "Trả giá mua sắm chợ đêm, mặc cả & quét mã WeChat Pay",
        levelBadge = "1 Kịch bản • HSK 3–4",
        headerColor = PixelColors.PixelOrange,
        cardBgColor = Color(0xFFFFEDD5),
        scenarios = listOf(
            DEFAULT_SCENARIOS[3] // shopping
        )
    )
)

/**
 * Thẻ Card Ngang Chủ Đề Luyện Nói AI (Horizontal Topic Category Card):
 * - Bố cục ngang tinh giản, thoáng đãng, không bị lồng hộp rườm rà.
 * - Icon Emoji lớn bên trái, nội dung ở giữa và nút khám phá mở danh sách kịch bản.
 */
@Composable
fun RoleplayTopicCard(
    topic: RoleplayTopicData,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    PixelCardBox(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        backgroundColor = topic.cardBgColor,
        headerTitle = "${topic.iconEmoji} ${topic.title.uppercase()}",
        headerBadge = topic.levelBadge,
        headerColor = topic.headerColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Khung Icon Emoji lớn dập nổi
            val iconShape = PixelStepCornerShape(2.dp)
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(iconShape)
                    .background(Color.White)
                    .border(BorderStroke(1.2.dp, PixelColors.BorderDark), iconShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = topic.iconEmoji,
                    fontSize = 24.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Thông tin chủ đề
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = topic.chineseTitle,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = PixelColors.BorderDark
                    )
                    Text(
                        text = "(${topic.pinyinTitle})",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B)
                    )
                }

                Text(
                    text = topic.description,
                    fontSize = 11.sp,
                    color = Color(0xFF475569),
                    lineHeight = 15.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Nút bấm khám phá
            PixelArcadeButton(
                text = "XEM ▶",
                color = topic.headerColor,
                onClick = onClick
            )
        }
    }
}
