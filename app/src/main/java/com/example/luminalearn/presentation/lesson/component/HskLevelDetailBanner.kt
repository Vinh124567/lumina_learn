package com.example.luminalearn.presentation.lesson.component

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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
            passScoreDesc = "Điểm chuẩn: 60% tổng",
            goodScore = "180+ / 200đ",
            goodScoreDesc = "Đạt học bổng & xin việc",
            vocabCount = "150 từ",
            vocabDesc = "Gồm cả chữ Hán cốt lõi",
            examDuration = "35 phút",
            examDurationDesc = "Nghe & Đọc (40 câu)",
            sections = listOf(
                ExamSectionItem("Phần Nghe", "20 câu (15 phút, đọc 2 lần)", "100đ"),
                ExamSectionItem("Phần Đọc", "20 câu (17 phút)", "100đ")
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
            passScoreDesc = "Điểm chuẩn: 60% tổng",
            goodScore = "180+ / 200đ",
            goodScoreDesc = "Đạt học bổng & miễn ngoại ngữ",
            vocabCount = "300 từ",
            vocabDesc = "150 từ mới + tích lũy HSK 1",
            examDuration = "50 phút",
            examDurationDesc = "Nghe & Đọc (60 câu)",
            sections = listOf(
                ExamSectionItem("Phần Nghe", "35 câu (25 phút, đọc 2 lần)", "100đ"),
                ExamSectionItem("Phần Đọc", "25 câu (22 phút)", "100đ")
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
            maxScore = "/ 300đ",
            passScoreDesc = "Điểm chuẩn: 60% tổng",
            goodScore = "240+ / 300đ",
            goodScoreDesc = "Đạt học bổng 1 năm tiếng & xin việc",
            vocabCount = "600 từ",
            vocabDesc = "300 từ mới + tích lũy HSK 1-2",
            examDuration = "85 phút",
            examDurationDesc = "Nghe, Đọc & Viết",
            sections = listOf(
                ExamSectionItem("Phần Nghe", "40 câu (35 phút)", "100đ"),
                ExamSectionItem("Phần Đọc", "30 câu (30 phút)", "100đ"),
                ExamSectionItem("Phần Viết", "10 câu (15 phút)", "100đ")
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
            maxScore = "/ 300đ",
            passScoreDesc = "Điểm chuẩn: 60% tổng",
            goodScore = "240+ / 300đ",
            goodScoreDesc = "Đạt học bổng chính phủ CSC / CIS",
            vocabCount = "1200 từ",
            vocabDesc = "600 từ mới chuyên sâu",
            examDuration = "100 phút",
            examDurationDesc = "Nghe, Đọc & Viết",
            sections = listOf(
                ExamSectionItem("Phần Nghe", "45 câu (30 phút, nghe 1 lần)", "100đ"),
                ExamSectionItem("Phần Đọc", "40 câu (40 phút)", "100đ"),
                ExamSectionItem("Phần Viết", "15 câu (25 phút)", "100đ")
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
            maxScore = "/ 300đ",
            passScoreDesc = "Điểm chuẩn: 60% tổng",
            goodScore = "250+ / 300đ",
            goodScoreDesc = "Đạt học bổng & xin việc",
            vocabCount = "2500 từ",
            vocabDesc = "Gồm cả chữ Hán cốt lõi",
            examDuration = "120 phút",
            examDurationDesc = "Nghe, Đọc & Tự luận",
            sections = listOf(
                ExamSectionItem("Phần Nghe", "45 câu (30 phút, nghe 1 lần)", "100đ"),
                ExamSectionItem("Phần Đọc", "45 câu (45 phút)", "100đ"),
                ExamSectionItem("Phần Viết", "10 câu + 2 đoạn văn (40 phút)", "100đ")
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
            maxScore = "/ 300đ",
            passScoreDesc = "Điểm chuẩn: 60% tổng",
            goodScore = "250+ / 300đ",
            goodScoreDesc = "Biên phiên dịch viên cao cấp",
            vocabCount = "5000+ từ",
            vocabDesc = "Hệ thống từ vựng toàn diện",
            examDuration = "135 phút",
            examDurationDesc = "Nghe, Đọc & Tự luận",
            sections = listOf(
                ExamSectionItem("Phần Nghe", "50 câu (35 phút, nghe 1 lần)", "100đ"),
                ExamSectionItem("Phần Đọc", "50 câu (50 phút)", "100đ"),
                ExamSectionItem("Phần Viết", "Tóm tắt 1000 chữ (45 phút)", "100đ")
            ),
            strategy = "Luyện trí nhớ ngắn hạn và kỹ năng tóm tắt trong 10 phút. Tinh chỉnh nhận diện lỗi sai ngữ pháp nâng cao."
        )
        else -> null
    }
}

@Composable
fun HskLevelDetailBanner(
    level: String,
    completedCount: Int,
    totalCount: Int,
    onViewVocabClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val detail = getHskStandardDetail(level) ?: return

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = Color(0x141E293B),
                ambientColor = Color(0x08000000)
            ),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFEFF6FF))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // 1. Header: Icon Squircle HSK + Tiêu đề + 2 Tags + Mô tả + Nút full-width
            BannerHeader(
                detail = detail,
                onViewVocabClick = { onViewVocabClick(detail.levelId) }
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 2. Lưới 2x2: 4 thẻ số liệu thống kê & thang điểm
            BannerMetricsGrid(detail = detail)

            Spacer(modifier = Modifier.height(14.dp))

            // 3. Hai khối: Cấu trúc thi và Chiến thuật tối ưu
            BannerBottomDetails(
                detail = detail,
                completedCount = completedCount,
                totalCount = totalCount
            )
        }
    }
}

@Composable
private fun BannerHeader(
    detail: HskStandardDetail,
    onViewVocabClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Hàng 1: Icon Squircle HSK + Cột Tiêu đề & 2 Tags
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            // Icon squircle tím đậm
            Box(
                modifier = Modifier
                    .size(width = 46.dp, height = 48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF581C87)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "HSK",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 12.sp
                    )
                    Text(
                        text = detail.levelDigit,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Tiêu đề & Tags
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = detail.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF0F172A)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Tag 1: CEFR (Tím)
                Surface(
                    shape = RoundedCornerShape(5.dp),
                    color = Color(0xFFF3E8FF)
                ) {
                    Text(
                        text = detail.cefrTag,
                        color = Color(0xFF7E22CE),
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                if (detail.extraTag.isNotBlank()) {
                    Spacer(modifier = Modifier.height(3.dp))
                    // Tag 2: Đặc điểm đề thi
                    Surface(
                        shape = RoundedCornerShape(5.dp),
                        color = detail.extraTagBgColor
                    ) {
                        Text(
                            text = detail.extraTag,
                            color = detail.extraTagTextColor,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Đoạn văn bản mô tả năng lực
        Text(
            text = detail.description,
            fontSize = 12.sp,
            color = Color(0xFF64748B),
            lineHeight = 17.5.sp,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Nút Xem từ vựng: FULL-WIDTH
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(38.dp)
                .clickable(onClick = onViewVocabClick),
            shape = RoundedCornerShape(10.dp),
            color = Color(0xFFF5F3FF),
            border = BorderStroke(1.dp, Color(0xFFDDD6FE))
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_nav_vocabulary),
                    contentDescription = null,
                    tint = Color(0xFF4F46E5),
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Xem từ vựng ${detail.levelId} →",
                    color = Color(0xFF4F46E5),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun BannerMetricsGrid(detail: HskStandardDetail) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Hàng 1: Thang điểm & Mục tiêu giỏi
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MetricInfoCard(
                label = "THANG ĐIỂM & ĐIỂM ĐỖ",
                primaryValue = detail.passScore,
                secondaryValue = detail.maxScore,
                primaryColor = Color(0xFF10B981),
                description = detail.passScoreDesc,
                descriptionColor = Color(0xFF059669),
                modifier = Modifier.weight(1f)
            )

            MetricInfoCard(
                label = "MỤC TIÊU ĐIỂM GIỎI",
                primaryValue = detail.goodScore,
                secondaryValue = null,
                primaryColor = Color(0xFF4F46E5),
                description = detail.goodScoreDesc,
                descriptionColor = Color(0xFF64748B),
                modifier = Modifier.weight(1f)
            )
        }

        // Hàng 2: Vốn từ vựng & Thời lượng bài thi
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MetricInfoCard(
                label = "VỐN TỪ VỰNG HSK",
                primaryValue = detail.vocabCount,
                secondaryValue = null,
                primaryColor = Color(0xFF0F172A),
                description = detail.vocabDesc,
                descriptionColor = Color(0xFF64748B),
                modifier = Modifier.weight(1f)
            )

            MetricInfoCard(
                label = "THỜI LƯỢNG BÀI THI",
                primaryValue = detail.examDuration,
                secondaryValue = null,
                primaryColor = Color(0xFF0F172A),
                description = detail.examDurationDesc,
                descriptionColor = Color(0xFF64748B),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun MetricInfoCard(
    label: String,
    primaryValue: String,
    secondaryValue: String?,
    primaryColor: Color,
    description: String,
    descriptionColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFF8FAFC))
            .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(14.dp))
            .padding(horizontal = 10.dp, vertical = 9.dp)
    ) {
        Column {
            Text(
                text = label,
                fontSize = 9.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF64748B),
                letterSpacing = 0.2.sp,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = primaryValue,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = primaryColor
                )
                if (secondaryValue != null) {
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = secondaryValue,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF64748B)
                    )
                }
            }
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = description,
                fontSize = 10.5.sp,
                color = descriptionColor,
                lineHeight = 14.sp,
                maxLines = 2
            )
        }
    }
}

@Composable
private fun BannerBottomDetails(
    detail: HskStandardDetail,
    completedCount: Int,
    totalCount: Int
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Khối 1: Cấu trúc các phần thi chính thức
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFFF8FAFC))
                .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(14.dp))
                .padding(12.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_slides),
                        contentDescription = null,
                        tint = Color(0xFF6366F1),
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Cấu trúc các phần thi chính thức:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF334155)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                detail.sections.forEach { sec ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = sec.title,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF0F172A)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${sec.detail} • ",
                                fontSize = 11.5.sp,
                                color = Color(0xFF64748B)
                            )
                            Text(
                                text = sec.score,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                        }
                    }
                }
            }
        }

        // Khối 2: Chiến thuật tối ưu điểm số
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFFFFFBEB))
                .border(1.dp, Color(0xFFFEF3C7), RoundedCornerShape(14.dp))
                .padding(12.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_sparkle),
                        contentDescription = null,
                        tint = Color(0xFFD97706),
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Chiến thuật tối ưu điểm số ${detail.levelId}:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF92400E)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = detail.strategy,
                    fontSize = 11.5.sp,
                    color = Color(0xFF78350F),
                    lineHeight = 16.5.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_check_circle),
                        contentDescription = null,
                        tint = Color(0xFF059669),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Tiến độ bài học cấp độ này: $completedCount/$totalCount bài hoàn thành",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF059669)
                    )
                }
            }
        }
    }
}
