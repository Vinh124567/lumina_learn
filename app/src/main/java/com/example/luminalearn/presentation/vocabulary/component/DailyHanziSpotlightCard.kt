package com.example.luminalearn.presentation.vocabulary.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.ui.theme.PlusJakartaSans
import kotlinx.coroutines.launch
import java.util.Calendar

private val BrandIndigo = Color(0xFF6366F1)
private val TextMain = Color(0xFF0F172A)
private val TextMuted = Color(0xFF64748B)
private val BorderSubtle = Color(0xFFE2E8F0)

data class HanziWordDetail(
    val word: String,
    val pinyin: String,
    val meaning: String
)

data class HanziSentenceDetail(
    val chinese: String,
    val pinyin: String,
    val vietnamese: String
)

data class HanziSpotlightData(
    val hanzi: String,
    val pinyin: String,
    val hanViet: String,
    val radical: String,
    val strokeCount: Int = 6,
    val components: List<String> = emptyList(),
    val story: String,
    val words: String,
    val commonWords: List<HanziWordDetail> = emptyList(),
    val exampleSentence: HanziSentenceDetail? = null
)

val DAILY_HANZI_COLLECTION = listOf(
    HanziSpotlightData(
        hanzi = "安",
        pinyin = "ān",
        hanViet = "An",
        radical = "Bộ Miên (宀)",
        strokeCount = 6,
        components = listOf("宀 (Mái nhà)", "女 (Người phụ nữ)"),
        story = "Gồm mái nhà (宀) che chở cho người phụ nữ (女). Phụ nữ yên ấm dưới mái nhà thì gia đạo bình an, an khang, không lo sóng gió bên ngoài.",
        words = "安全 (ānquán - An toàn) • 安心 (ānxīn - An tâm)",
        commonWords = listOf(
            HanziWordDetail("安全", "ānquán", "An toàn"),
            HanziWordDetail("安心", "ānxīn", "An tâm"),
            HanziWordDetail("平安", "píng'ān", "Bình an")
        ),
        exampleSentence = HanziSentenceDetail(
            chinese = "祝你一路平安，事事顺心。",
            pinyin = "Zhù nǐ yílù píng'ān, shìshì shùnxīn.",
            vietnamese = "Chúc bạn lên đường bình an, mọi sự thuận buồm xuôi gió."
        )
    ),
    HanziSpotlightData(
        hanzi = "明",
        pinyin = "míng",
        hanViet = "Minh",
        radical = "Bộ Nhật (日)",
        strokeCount = 8,
        components = listOf("日 (Mặt trời)", "月 (Mặt trăng)"),
        story = "Kết hợp giữa mặt trời (日) rực rỡ và mặt trăng (月) vằng vặc. Hai nguồn sáng vĩ đại nhất của đất trời hòa làm một, tạo nên sự quang minh, tỏ tường và thấu suốt.",
        words = "明白 (míngbai - Hiểu rõ) • 明天 (míngtiān - Ngày mai)",
        commonWords = listOf(
            HanziWordDetail("明白", "míngbai", "Hiểu rõ"),
            HanziWordDetail("明天", "míngtiān", "Ngày mai"),
            HanziWordDetail("光明", "guāngmíng", "Quang minh, tươi sáng")
        ),
        exampleSentence = HanziSentenceDetail(
            chinese = "我终于听明白了老师的意思。",
            pinyin = "Wǒ zhōngyú tīng míngbai le lǎoshī de yìsi.",
            vietnamese = "Cuối cùng em cũng đã hiểu rõ ý của thầy giáo."
        )
    ),
    HanziSpotlightData(
        hanzi = "和",
        pinyin = "hé",
        hanViet = "Hòa",
        radical = "Bộ Khẩu (口)",
        strokeCount = 8,
        components = listOf("禾 (Cây lúa)", "口 (Miệng ăn)"),
        story = "Gồm bông lúa trĩu hạt (禾) và miệng ăn (口). Mọi người đều có đủ cơm ăn áo mặc thì xã hội thái bình, lòng người hòa thuận, kiến tạo thái bình thịnh trị.",
        words = "和平 (hépíng - Hòa bình) • 和气 (héqi - Hòa nhã)",
        commonWords = listOf(
            HanziWordDetail("和平", "hépíng", "Hòa bình"),
            HanziWordDetail("和气", "héqi", "Hòa nhã"),
            HanziWordDetail("和谐", "héxié", "Hài hòa")
        ),
        exampleSentence = HanziSentenceDetail(
            chinese = "人与自然应当和谐相处。",
            pinyin = "Rén yǔ zìrán yīngdāng héxié xiāngchǔ.",
            vietnamese = "Con người và thiên nhiên nên chung sống hài hòa cùng nhau."
        )
    ),
    HanziSpotlightData(
        hanzi = "信",
        pinyin = "xìn",
        hanViet = "Tín",
        radical = "Bộ Nhân (亻)",
        strokeCount = 9,
        components = listOf("亻 (Người quân tử)", "言 (Lời nói)"),
        story = "Ghép từ người (亻) và lời nói (言). Người quân tử lời nói ra phải giữ trọn, nói là làm, tạo nên chữ 'Tín' và lòng tin cậy vững chãi giữa người với người.",
        words = "相信 (xiāngxìn - Tin tưởng) • 信心 (xìnxīn - Tự tin)",
        commonWords = listOf(
            HanziWordDetail("相信", "xiāngxìn", "Tin tưởng"),
            HanziWordDetail("信心", "xìnxīn", "Tự tin"),
            HanziWordDetail("信用", "xìnyòng", "Uy tín")
        ),
        exampleSentence = HanziSentenceDetail(
            chinese = "做人一定要守信用。",
            pinyin = "Zuòrén yídìng yào shǒu xìnyòng.",
            vietnamese = "Làm người nhất định phải giữ chữ tín."
        )
    ),
    HanziSpotlightData(
        hanzi = "爱",
        pinyin = "ài",
        hanViet = "Ái",
        radical = "Bộ Trảo (爫)",
        strokeCount = 10,
        components = listOf("爫 (Nâng niu)", "冖 (Bao bọc)", "友 (Tình thân)"),
        story = "Tình cảm bao bọc, sự che chở nâng niu bằng cả tấm lòng chân thành. Yêu thương là trao đi sự thấu hiểu và gắn kết bền chặt như người tri kỷ.",
        words = "爱情 (àiqíng - Tình yêu) • 爱好 (àihào - Sở thích)",
        commonWords = listOf(
            HanziWordDetail("爱情", "àiqíng", "Tình yêu"),
            HanziWordDetail("爱好", "àihào", "Sở thích"),
            HanziWordDetail("关爱", "guān'ài", "Quan tâm yêu thương")
        ),
        exampleSentence = HanziSentenceDetail(
            chinese = "爱能克服生活中的一切困难。",
            pinyin = "Ài néng kèfú shēnghuó zhōng de yíqiè kùnnan.",
            vietnamese = "Tình yêu thương có thể vượt qua mọi khó khăn trong cuộc sống."
        )
    ),
    HanziSpotlightData(
        hanzi = "学",
        pinyin = "xué",
        hanViet = "Học",
        radical = "Bộ Tử (子)",
        strokeCount = 8,
        components = listOf("⺍ (Nét tư duy)", "冖 (Mái trường)", "子 (Đứa trẻ)"),
        story = "Mái trường che chở trên đầu, đứa trẻ (子) mở rộng tầm mắt tiếp thu tinh hoa tri thức của bậc tiền nhân để rèn luyện nhân cách và tài năng.",
        words = "学习 (xuéxí - Học tập) • 学校 (xuéxiào - Trường học)",
        commonWords = listOf(
            HanziWordDetail("学习", "xuéxí", "Học tập"),
            HanziWordDetail("学校", "xuéxiào", "Trường học"),
            HanziWordDetail("学生", "xuésheng", "Học sinh")
        ),
        exampleSentence = HanziSentenceDetail(
            chinese = "好好学习，天天向上。",
            pinyin = "Hǎohǎo xuéxí, tiāntiān xiàngshàng.",
            vietnamese = "Học tập chăm chỉ, mỗi ngày một tiến bộ."
        )
    ),
    HanziSpotlightData(
        hanzi = "福",
        pinyin = "fú",
        hanViet = "Phúc",
        radical = "Bộ Thị (礻)",
        strokeCount = 13,
        components = listOf("礻 (Thần linh ban ơn)", "一口田 (Một gia đình ấm no)"),
        story = "Thần linh ban phúc (礻), con người có một mảnh ruộng màu mỡ (田) và một gia đình quây quần ấm no (口), ấy là phúc đức trọn vẹn của đời người.",
        words = "幸福 (xìngfú - Hạnh phúc) • 福气 (fúqi - Phúc khí)",
        commonWords = listOf(
            HanziWordDetail("幸福", "xìngfú", "Hạnh phúc"),
            HanziWordDetail("福气", "fúqi", "Phúc khí"),
            HanziWordDetail("祝福", "zhùfú", "Chúc phúc")
        ),
        exampleSentence = HanziSentenceDetail(
            chinese = "祝你全家幸福美满，万事如意。",
            pinyin = "Zhù nǐ quánjiā xìngfú měimǎn, wànshì rúyì.",
            vietnamese = "Chúc toàn thể gia đình bạn hạnh phúc mỹ mãn, vạn sự như ý."
        )
    ),
    HanziSpotlightData(
        hanzi = "家",
        pinyin = "jiā",
        hanViet = "Gia",
        radical = "Bộ Miên (宀)",
        strokeCount = 10,
        components = listOf("宀 (Mái nhà)", "豕 (Gia súc trù phú)"),
        story = "Dưới mái nhà (宀) nuôi gia súc no đủ (豕 - con lợn biểu trưng cho sự trù phú thời cổ). Mái ấm gia đình sum vầy no đủ là cội nguồn của mọi hạnh phúc.",
        words = "家庭 (jiātíng - Gia đình) • 大家 (dàjiā - Mọi người)",
        commonWords = listOf(
            HanziWordDetail("家庭", "jiātíng", "Gia đình"),
            HanziWordDetail("大家", "dàjiā", "Mọi người"),
            HanziWordDetail("回家", "huíjiā", "Về nhà")
        ),
        exampleSentence = HanziSentenceDetail(
            chinese = "家是永远温暖的心灵港湾。",
            pinyin = "Jiā shì yǒngyuǎn wēnnuǎn de xīnlíng gǎngwān.",
            vietnamese = "Gia đình mãi mãi là bến đỗ ấm áp của tâm hồn."
        )
    ),
    HanziSpotlightData(
        hanzi = "忍",
        pinyin = "rěn",
        hanViet = "Nhẫn",
        radical = "Bộ Tâm (心)",
        strokeCount = 7,
        components = listOf("刃 (Lưỡi đao sắc bén)", "心 (Trái tim)"),
        story = "Lưỡi đao sắc bén (刃) đặt ngay trên trái tim (心). Ý chí kiên định kiềm chế cảm xúc nhất thời, bền gan nhẫn nại thì ắt vượt qua nghịch cảnh và nên việc lớn.",
        words = "忍耐 (rěnnài - Nhẫn nại) • 容忍 (róngrěn - Khoan dung)",
        commonWords = listOf(
            HanziWordDetail("忍耐", "rěnnài", "Nhẫn nại"),
            HanziWordDetail("容忍", "róngrěn", "Khoan dung"),
            HanziWordDetail("忍心", "rěnxīn", "Nỡ lòng")
        ),
        exampleSentence = HanziSentenceDetail(
            chinese = "小不忍则乱大谋。",
            pinyin = "Xiǎo bù rěn zé luàn dà móu.",
            vietnamese = "Việc nhỏ không nhẫn nhịn thì làm hỏng mưu sự lớn."
        )
    ),
    HanziSpotlightData(
        hanzi = "美",
        pinyin = "měi",
        hanViet = "Mỹ",
        radical = "Bộ Dương (羊)",
        strokeCount = 9,
        components = listOf("羊 (Con cừu)", "大 (To lớn mập mạp)"),
        story = "Con cừu (羊) to lớn tốt tươi (大). Người xưa lấy sự tươi tốt, sung túc của vật nuôi làm biểu trưng cho cái Đẹp thanh tao, toàn mỹ và sự hài hòa thánh thiện.",
        words = "美丽 (měilì - Xinh đẹp) • 美好 (měihǎo - Tốt đẹp)",
        commonWords = listOf(
            HanziWordDetail("美丽", "měilì", "Xinh đẹp"),
            HanziWordDetail("美好", "měihǎo", "Tốt đẹp"),
            HanziWordDetail("美食", "měishí", "Món ăn ngon")
        ),
        exampleSentence = HanziSentenceDetail(
            chinese = "祝你拥有一个美好的未来。",
            pinyin = "Zhù nǐ yōngyǒu yí gè měihǎo de wèilái.",
            vietnamese = "Chúc bạn có một tương lai thật tươi sáng và tốt đẹp."
        )
    )
)

/**
 * Thẻ Chiết Tự Tâm Điểm Hôm Nay:
 * - Giới thiệu 1 chữ Hán tiêu biểu theo ngày kèm câu chuyện chiết tự ý nghĩa.
 * - Nút xoay tròn ở góc để đổi chữ Hán ngẫu nhiên / tuần tự với hiệu ứng xoay 360 độ.
 * - Chạm vào thẻ mở Dialog Chi Tiết Chiết Tự chuyên sâu.
 */
@Composable
fun DailyHanziSpotlightCard(
    onSpeakWord: (String) -> Unit,
    onOpenDetail: (HanziSpotlightData) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val initialDayIndex = remember {
        val cal = Calendar.getInstance()
        (cal.get(Calendar.DAY_OF_WEEK) - 1).coerceIn(0, DAILY_HANZI_COLLECTION.size - 1)
    }

    var currentIndex by remember { mutableIntStateOf(initialDayIndex) }
    val currentData = DAILY_HANZI_COLLECTION[currentIndex]

    val scope = rememberCoroutineScope()
    val rotationAngle = remember { Animatable(0f) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .bounceClick(scaleDown = 0.985f) {
                onOpenDetail(currentData)
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(0.5.dp, BorderSubtle),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // ── Header: Badge + Bộ thủ + Nút quay tròn đổi chữ ở góc ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFEEF2FF))
                        .border(BorderStroke(0.5.dp, Color(0xFFC7D2FE)), RoundedCornerShape(6.dp))
                        .padding(horizontal = 7.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "✨ CHIẾT TỰ HÔM NAY",
                        fontFamily = PlusJakartaSans,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandIndigo
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = currentData.radical,
                        fontFamily = PlusJakartaSans,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextMuted
                    )

                    // Nút xoay tròn đổi chữ Hán
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF1F5F9))
                            .border(BorderStroke(0.5.dp, BorderSubtle), CircleShape)
                            .bounceClick(scaleDown = 0.85f) {
                                scope.launch {
                                    launch {
                                        rotationAngle.animateTo(
                                            targetValue = rotationAngle.value + 360f,
                                            animationSpec = tween(
                                                durationMillis = 450,
                                                easing = FastOutSlowInEasing
                                            )
                                        )
                                    }
                                    currentIndex = (currentIndex + 1) % DAILY_HANZI_COLLECTION.size
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_refresh),
                            contentDescription = "Đổi chữ chiết tự",
                            tint = BrandIndigo,
                            modifier = Modifier
                                .size(14.dp)
                                .graphicsLayer {
                                    this.rotationZ = rotationAngle.value
                                }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Thân thẻ: AnimatedContent chuyển đổi mượt mà khi đổi chữ ──
            AnimatedContent(
                targetState = currentData,
                transitionSpec = {
                    fadeIn(animationSpec = tween(220)) togetherWith
                        fadeOut(animationSpec = tween(150))
                },
                label = "HanziSpotlightTransition"
            ) { data ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Khối chữ Hán lớn nổi bật
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFF8FAFC))
                            .border(BorderStroke(0.5.dp, BorderSubtle), RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = data.hanzi,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMain
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = data.pinyin,
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BrandIndigo
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = "[${data.hanViet}]",
                                    fontFamily = PlusJakartaSans,
                                    fontSize = 9.5.sp,
                                    color = TextMuted
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Câu chuyện chiết tự sâu sắc
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = data.story,
                            fontFamily = PlusJakartaSans,
                            fontSize = 11.5.sp,
                            color = Color(0xFF334155),
                            lineHeight = 16.5.sp,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = data.words,
                            fontFamily = PlusJakartaSans,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = BrandIndigo,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Nút phát âm loa tròn
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF1F5F9))
                            .bounceClick(scaleDown = 0.88f) {
                                onSpeakWord(data.hanzi)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_speaker),
                            contentDescription = "Phát âm",
                            tint = BrandIndigo,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // ── Gợi ý chạm để xem chi tiết nét & câu ví dụ ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Chạm để xem chi tiết nét & câu ví dụ",
                    fontFamily = PlusJakartaSans,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BrandIndigo
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = "→",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandIndigo
                )
            }
        }
    }
}
