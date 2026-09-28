package com.example.luminalearn.presentation.lesson.component

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R

data class ExamSectionItem(
    val title: String,
    val detail: String,
    val score: String
)

data class HskStandardDetail(
    val levelId: String,
    val levelDigit: String,
    val title: String,
    val cefrTag: String,
    val extraTag: String,
    val extraTagBgColor: Color = Color(0xFFCCFBF1),
    val extraTagTextColor: Color = Color(0xFF0F766E),
    val description: String,
    val passScore: String,
    val maxScore: String,
    val passScoreDesc: String,
    val goodScore: String,
    val goodScoreDesc: String,
    val vocabCount: String,
    val vocabDesc: String,
    val examDuration: String,
    val examDurationDesc: String,
    val sections: List<ExamSectionItem>,
    val strategy: String
)

private const val SECTION_LISTENING = "Phần Nghe"
private const val SECTION_READING = "Phần Đọc"
private const val SECTION_WRITING = "Phần Viết"
private const val PASS_SCORE_DESC_60 = "Điểm chuẩn: 60% tổng"
private const val MAX_SCORE_300 = "/ 300đ"

fun getHskStandardDetail(level: String): HskStandardDetail? {
    return when (level.uppercase().trim()) {
        "HSK 1", "HSK1" -> HskStandardDetail(
            levelId = "HSK 1",
            levelDigit = "1",
            title = "Tiêu chuẩn Thang điểm HSK Cấp 1",
            cefrTag = "CEFR A1 (Căn bản)",
            extraTag = "Đầy đủ Pinyin trên toàn bộ đề thi",
            extraTagBgColor = Color(0xFFCCFBF1),
            extraTagTextColor = Color(0xFF0F766E),
            description = "Có thể hiểu và sử dụng các từ ngữ, câu thoại tiếng Trung cực kỳ đơn giản; làm quen cách chào hỏi, số đếm và đại từ.",
            passScore = "120",
            maxScore = "/ 200đ",
            passScoreDesc = PASS_SCORE_DESC_60,
            goodScore = "180+ / 200đ",
            goodScoreDesc = "Đạt học bổng & xin việc",
            vocabCount = "150 từ",
            vocabDesc = "Gồm cả chữ Hán cốt lõi",
            examDuration = "35 phút",
            examDurationDesc = "Nghe & Đọc (40 câu)",
            sections = listOf(
                ExamSectionItem(SECTION_LISTENING, "20 câu (15 phút, đọc 2 lần)", "100đ"),
                ExamSectionItem(SECTION_READING, "20 câu (17 phút)", "100đ")
            ),
            strategy = "Bài nghe đọc 2 lần với tốc độ rất chậm. Hãy tập trung bắt từ khóa danh từ chỉ người, địa điểm và số đếm."
        )
        "HSK 2", "HSK2" -> HskStandardDetail(
            levelId = "HSK 2",
            levelDigit = "2",
            title = "Tiêu chuẩn Thang điểm HSK Cấp 2",
            cefrTag = "CEFR A2 (Sơ cấp)",
            extraTag = "Đầy đủ Pinyin trên toàn bộ đề thi",
            extraTagBgColor = Color(0xFFCCFBF1),
            extraTagTextColor = Color(0xFF0F766E),
            description = "Giao tiếp cơ bản trong đời sống hàng ngày, hiểu các thông tin cá nhân và gia đình, mua sắm và môi trường quen thuộc.",
            passScore = "120",
            maxScore = "/ 200đ",
            passScoreDesc = PASS_SCORE_DESC_60,
            goodScore = "180+ / 200đ",
            goodScoreDesc = "Đạt học bổng & miễn ngoại ngữ",
            vocabCount = "300 từ",
            vocabDesc = "150 từ mới + tích lũy HSK 1",
            examDuration = "50 phút",
            examDurationDesc = "Nghe & Đọc (60 câu)",
            sections = listOf(
                ExamSectionItem(SECTION_LISTENING, "35 câu (25 phút, đọc 2 lần)", "100đ"),
                ExamSectionItem(SECTION_READING, "25 câu (22 phút)", "100đ")
            ),
            strategy = "Chú ý ngữ pháp liên từ nối câu và các phó từ chỉ mức độ (很, 非常, 太). Đề vẫn có pinyin nên tận dụng tối đa."
        )
        "HSK 3", "HSK3" -> HskStandardDetail(
            levelId = "HSK 3",
            levelDigit = "3",
            title = "Tiêu chuẩn Thang điểm HSK Cấp 3",
            cefrTag = "CEFR B1 (Trung cấp)",
            extraTag = "Không còn Pinyin trong đề thi",
            extraTagBgColor = Color(0xFFFEF3C7),
            extraTagTextColor = Color(0xFFB45309),
            description = "Có thể giao tiếp bằng tiếng Trung trong cuộc sống, học tập và công việc; xử lý phần lớn các tình huống khi đi du lịch.",
            passScore = "180",
            maxScore = MAX_SCORE_300,
            passScoreDesc = PASS_SCORE_DESC_60,
            goodScore = "240+ / 300đ",
            goodScoreDesc = "Đạt học bổng 1 năm tiếng & xin việc",
            vocabCount = "600 từ",
            vocabDesc = "300 từ mới + tích lũy HSK 1-2",
            examDuration = "85 phút",
            examDurationDesc = "Nghe, Đọc & Viết",
            sections = listOf(
                ExamSectionItem(SECTION_LISTENING, "40 câu (35 phút)", "100đ"),
                ExamSectionItem(SECTION_READING, "30 câu (30 phút)", "100đ"),
                ExamSectionItem(SECTION_WRITING, "10 câu (15 phút)", "100đ")
            ),
            strategy = "Bước chuyển mình quan trọng không còn Pinyin. Cần luyện nhận diện mặt chữ Hán và ngữ pháp câu chữ 把, 被."
        )
        "HSK 4", "HSK4" -> HskStandardDetail(
            levelId = "HSK 4",
            levelDigit = "4",
            title = "Tiêu chuẩn Thang điểm HSK Cấp 4",
            cefrTag = "CEFR B2 (Trung cao cấp)",
            extraTag = "Đủ chuẩn du học Đại học TQ",
            extraTagBgColor = Color(0xFFE0F2FE),
            extraTagTextColor = Color(0xFF0369A1),
            description = "Có thể thảo luận về các chủ đề chuyên sâu, giao lưu lưu loát với người bản xứ và đọc hiểu văn bản phổ thông.",
            passScore = "180",
            maxScore = MAX_SCORE_300,
            passScoreDesc = PASS_SCORE_DESC_60,
            goodScore = "240+ / 300đ",
            goodScoreDesc = "Đạt học bổng chính phủ CSC / CIS",
            vocabCount = "1200 từ",
            vocabDesc = "600 từ mới chuyên sâu",
            examDuration = "100 phút",
            examDurationDesc = "Nghe, Đọc & Viết",
            sections = listOf(
                ExamSectionItem(SECTION_LISTENING, "45 câu (30 phút, nghe 1 lần)", "100đ"),
                ExamSectionItem(SECTION_READING, "40 câu (40 phút)", "100đ"),
                ExamSectionItem(SECTION_WRITING, "15 câu (25 phút)", "100đ")
            ),
            strategy = "Phần nghe chỉ nghe 1 lần duy nhất. Cần đọc lướt trước đáp án và quản lý thời gian phần Đọc chặt chẽ."
        )
        "HSK 5", "HSK5" -> HskStandardDetail(
            levelId = "HSK 5",
            levelDigit = "5",
            title = "Tiêu chuẩn Thang điểm HSK Cấp 5",
            cefrTag = "CEFR C1 (Cao cấp)",
            extraTag = "100% Chữ Hán — Viết đoạn 80 chữ",
            extraTagBgColor = Color(0xFFFFEDD5),
            extraTagTextColor = Color(0xFFC2410C),
            description = "Có thể đọc báo chí, tạp chí tiếng Trung, thưởng thức phim ảnh không phụ đề, thuyết trình chuyên sâu và đàm phán hợp đồng kinh tế.",
            passScore = "180",
            maxScore = MAX_SCORE_300,
            passScoreDesc = PASS_SCORE_DESC_60,
            goodScore = "250+ / 300đ",
            goodScoreDesc = "Đạt học bổng & xin việc",
            vocabCount = "2500 từ",
            vocabDesc = "Gồm cả chữ Hán cốt lõi",
            examDuration = "120 phút",
            examDurationDesc = "Nghe, Đọc & Tự luận",
            sections = listOf(
                ExamSectionItem(SECTION_LISTENING, "45 câu (30 phút, nghe 1 lần)", "100đ"),
                ExamSectionItem(SECTION_READING, "45 câu (45 phút)", "100đ"),
                ExamSectionItem(SECTION_WRITING, "10 câu + 2 đoạn văn (40 phút)", "100đ")
            ),
            strategy = "Phần nghe chỉ được nghe 1 LẦN DUY NHẤT! Phải đọc lướt 4 đáp án trước khi băng chạy. Tận dụng tối đa từ Hán - Việt đồng âm nghĩa."
        )
        "HSK 6", "HSK6" -> HskStandardDetail(
            levelId = "HSK 6",
            levelDigit = "6",
            title = "Tiêu chuẩn Thang điểm HSK Cấp 6",
            cefrTag = "CEFR C2 (Bậc thầy)",
            extraTag = "Mức độ tinh thông cao nhất",
            extraTagBgColor = Color(0xFFEDE9FE),
            extraTagTextColor = Color(0xFF6D28D9),
            description = "Dễ dàng hiểu và biểu đạt suy nghĩ bằng tiếng Trung cả dạng nói lẫn viết một cách tự nhiên như người bản ngữ.",
            passScore = "180",
            maxScore = MAX_SCORE_300,
            passScoreDesc = PASS_SCORE_DESC_60,
            goodScore = "250+ / 300đ",
            goodScoreDesc = "Biên phiên dịch viên cao cấp",
            vocabCount = "5000+ từ",
            vocabDesc = "Hệ thống từ vựng toàn diện",
            examDuration = "135 phút",
            examDurationDesc = "Nghe, Đọc & Tự luận",
            sections = listOf(
                ExamSectionItem(SECTION_LISTENING, "50 câu (35 phút, nghe 1 lần)", "100đ"),
                ExamSectionItem(SECTION_READING, "50 câu (50 phút)", "100đ"),
                ExamSectionItem(SECTION_WRITING, "Tóm tắt 1000 chữ (45 phút)", "100đ")
            ),
            strategy = "Luyện trí nhớ ngắn hạn và kỹ năng tóm tắt trong 10 phút. Tinh chỉnh nhận diện lỗi sai ngữ pháp nâng cao."
        )
        else -> null
    }
}

@Composable
fun HskLevelDetailBanner(
    level: String,
    onViewVocabClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val detail = getHskStandardDetail(level) ?: return

    androidx.compose.material3.Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color.White),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF5C50F6))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = detail.levelId,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFEDE9FE))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = detail.cefrTag,
                            color = Color(0xFF5C50F6),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFF1F5F9),
                    modifier = Modifier.clickable { onViewVocabClick(detail.levelId) }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_nav_vocabulary),
                            contentDescription = null,
                            tint = Color(0xFF5C50F6),
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Từ vựng →",
                            color = Color(0xFF5C50F6),
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Description
            Text(
                text = detail.description,
                fontSize = 12.5.sp,
                color = Color(0xFF64748B),
                lineHeight = 17.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CompactMetricChip(
                    label = "Điểm đỗ",
                    value = "${detail.passScore}đ",
                    modifier = Modifier.weight(1f)
                )
                CompactMetricChip(
                    label = "Vốn từ",
                    value = detail.vocabCount,
                    modifier = Modifier.weight(1f)
                )
                CompactMetricChip(
                    label = "Thời lượng",
                    value = detail.examDuration,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun CompactMetricChip(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF8FAFC))
            .padding(vertical = 8.dp, horizontal = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = value,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                fontSize = 10.5.sp,
                color = Color(0xFF64748B)
            )
        }
    }
}
