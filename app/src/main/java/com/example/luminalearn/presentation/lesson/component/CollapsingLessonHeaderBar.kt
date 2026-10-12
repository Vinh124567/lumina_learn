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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.R
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.ui.theme.PlusJakartaSans

private val PrimaryIndigo = Color(0xFF5C50F6)
private val BrandIndigoLight = Color(0xFF6366F1)
private val TextMain = Color(0xFF0F172A)
private val TextMuted = Color(0xFF64748B)
private val BorderSubtle = Color(0xFFE2E8F0)
private val BackgroundColor = Color(0xFFF6F8FB)

/**
 * Collapsing Header chuẩn hóa theo phong cách của Tab Trang Chủ & Tab Từ Vựng:
 * - Đồng bộ 100% màu nền cùng màu với Body (#F6F8FB), liền mạch tuyệt đối.
 * - Khi ở đỉnh (progress = 0): Chiều cao 106dp, Tiêu đề to 22sp "Lộ Trình Bài Học", Subtitle mô tả.
 * - Khi cuộn (progress -> 1): Co mượt về 56dp, tiêu đề to scale trượt lên TopBar.
 * - Khi cuộn sâu: Dừng đóng và ghim cố định ở độ cao 56dp (Sticky Pinned), che chắn nội dung cuộn bên dưới.
 * - Góc phải: Nút chuyển chế độ xem (Bản đồ 🗺️ / Danh sách 📄) bo góc 12dp với hiệu ứng click nảy cao cấp.
 */
@Composable
fun CollapsingLessonHeaderBar(
    collapseProgress: Float,
    isJourneyView: Boolean,
    onToggleJourneyView: (Boolean) -> Unit,
    isOfflineMode: Boolean = false,
    currentHearts: Int = 5,
    maxHearts: Int = 5,
    onOpenHearts: () -> Unit = {},
    onOpenAnalytics: () -> Unit = {},
    onOpenGrammarVault: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val progress = collapseProgress.coerceIn(0f, 1f)
    val headerHeight = lerp(106.dp, 56.dp, progress)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(headerHeight)
            .background(BackgroundColor)
    ) {
        // ── 1. KHỐI EXPANDED TITLE (Tiêu đề to 22sp + Subtitle) ──
        if (progress < 0.95f) {
            val titleAlpha = (1f - progress * 1.5f).coerceIn(0f, 1f)
            val titleScale = 1f - progress * 0.15f

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .align(Alignment.BottomStart)
                    .padding(bottom = 10.dp)
                    .graphicsLayer {
                        alpha = titleAlpha
                        scaleX = titleScale
                        scaleY = titleScale
                        translationY = -progress * 25f
                    }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Lộ Trình Bài Học",
                        fontFamily = PlusJakartaSans,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextMain,
                        letterSpacing = (-0.4).sp
                    )
                    if (isOfflineMode) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFE0F2FE))
                                .border(BorderStroke(0.5.dp, Color(0xFF38BDF8)), RoundedCornerShape(8.dp))
                                .padding(horizontal = 7.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = androidx.compose.ui.res.stringResource(R.string.offline_badge),
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0369A1)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = if (isOfflineMode) {
                        androidx.compose.ui.res.stringResource(R.string.offline_subtitle)
                    } else {
                        "Chinh phục 6 cấp độ HSK theo lộ trình tương tác chuẩn quốc tế"
                    },
                    fontFamily = PlusJakartaSans,
                    fontSize = 11.5.sp,
                    color = TextMuted
                )
            }
        }

        // ── 2. THANH STICKY ACTION BAR TRÊN ĐỈNH (Cao 56dp) ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .align(Alignment.TopCenter)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Tiêu đề mini (17sp) mờ hiện dần khi progress > 0.35
            val collapsedTitleAlpha = ((progress - 0.35f) / 0.65f).coerceIn(0f, 1f)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.graphicsLayer {
                    alpha = collapsedTitleAlpha
                }
            ) {
                Text(
                    text = "Lộ Trình Bài Học",
                    fontFamily = PlusJakartaSans,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMain,
                    letterSpacing = (-0.3).sp
                )
                if (isOfflineMode) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFE0F2FE))
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = androidx.compose.ui.res.stringResource(R.string.offline_badge_mini),
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0369A1)
                        )
                    }
                }
            }

            // Góc phải: Cụm chuyển đổi chế độ xem + Tim + Phân tích
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Nút Radar Phân tích
                Box(
                    modifier = Modifier
                        .height(36.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFEEF2FF))
                        .border(BorderStroke(0.5.dp, BrandIndigoLight), RoundedCornerShape(12.dp))
                        .bounceClick(scaleDown = 0.92f) { onOpenAnalytics() }
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "📊", fontSize = 13.sp)
                }

                // Nút Cẩm nang Ngữ pháp HSK
                Box(
                    modifier = Modifier
                        .height(36.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF3E8FF))
                        .border(BorderStroke(0.5.dp, Color(0xFFA855F7)), RoundedCornerShape(12.dp))
                        .bounceClick(scaleDown = 0.92f) { onOpenGrammarVault() }
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "📖", fontSize = 13.sp)
                }

                // Badge Trái Tim
                Box(
                    modifier = Modifier
                        .height(36.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFEE2E2))
                        .border(BorderStroke(0.5.dp, Color(0xFFEF4444)), RoundedCornerShape(12.dp))
                        .bounceClick(scaleDown = 0.92f) { onOpenHearts() }
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "❤️", fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "$currentHearts",
                            fontFamily = PlusJakartaSans,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFEF4444)
                        )
                    }
                }

                // Nút Bản đồ
                Box(
                    modifier = Modifier
                        .height(36.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isJourneyView) Color.White else Color(0xFFEEF2F6))
                        .border(
                            BorderStroke(
                                0.5.dp,
                                if (isJourneyView) BrandIndigoLight.copy(alpha = 0.5f) else BorderSubtle
                            ),
                            RoundedCornerShape(12.dp)
                        )
                        .bounceClick(scaleDown = 0.92f) { onToggleJourneyView(true) }
                        .padding(horizontal = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_nav_roadmap),
                            contentDescription = "Bản đồ",
                            tint = if (isJourneyView) BrandIndigoLight else TextMuted,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "Bản đồ",
                            fontFamily = PlusJakartaSans,
                            fontSize = 11.5.sp,
                            fontWeight = if (isJourneyView) FontWeight.Bold else FontWeight.Medium,
                            color = if (isJourneyView) BrandIndigoLight else TextMuted
                        )
                    }
                }

                // Nút Danh sách
                Box(
                    modifier = Modifier
                        .height(36.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (!isJourneyView) Color.White else Color(0xFFEEF2F6))
                        .border(
                            BorderStroke(
                                0.5.dp,
                                if (!isJourneyView) BrandIndigoLight.copy(alpha = 0.5f) else BorderSubtle
                            ),
                            RoundedCornerShape(12.dp)
                        )
                        .bounceClick(scaleDown = 0.92f) { onToggleJourneyView(false) }
                        .padding(horizontal = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_layers),
                            contentDescription = "Danh sách",
                            tint = if (!isJourneyView) BrandIndigoLight else TextMuted,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "Danh sách",
                            fontFamily = PlusJakartaSans,
                            fontSize = 11.5.sp,
                            fontWeight = if (!isJourneyView) FontWeight.Bold else FontWeight.Medium,
                            color = if (!isJourneyView) BrandIndigoLight else TextMuted
                        )
                    }
                }
            }
        }
    }
}
