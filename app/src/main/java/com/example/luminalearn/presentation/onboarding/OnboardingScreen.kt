package com.example.luminalearn.presentation.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.luminalearn.R
import com.example.luminalearn.core.ui.effect.bounceClick
import com.example.luminalearn.core.ui.effect.breathingGlow
import com.example.luminalearn.presentation.main.AppDestination
import com.example.luminalearn.ui.theme.PlusJakartaSans
import kotlinx.coroutines.launch

private val BrandIndigo = Color(0xFF4F46E5)
private val BrandIndigoLight = Color(0xFF6366F1)
private val BrandIndigoBg = Color(0xFFEEF2FF)
private val BackgroundClean = Color(0xFFF8FAFC)
private val TextMain = Color(0xFF0F172A)
private val TextMuted = Color(0xFF64748B)
private val BorderSubtle = Color(0xFFE2E8F0)
private val AmberGold = Color(0xFFF59E0B)
private val AmberGoldBg = Color(0xFFFEF3C7)

/**
 * Model chứa dữ liệu từng slide giới thiệu tính năng Onboarding.
 */
private data class OnboardingPage(
    val badge: String,
    val title: String,
    val description: String,
    val iconResId: Int,
    val themeColor: Color,
    val themeBgColor: Color
)

private val OnboardingPages = listOf(
    OnboardingPage(
        badge = "TRỢ LÝ THÔNG MINH AI",
        title = "Khởi mở tiềm năng HSK\ncùng trợ lý Lumina AI",
        description = "Gia sư AI đồng hành 24/7, tự động phân tích điểm yếu, giải thích ngữ pháp và hiệu chỉnh phát âm chuẩn xác từng thanh điệu.",
        iconResId = R.drawable.ic_spark_ai,
        themeColor = BrandIndigo,
        themeBgColor = BrandIndigoBg
    ),
    OnboardingPage(
        badge = "KHOA HỌC TRÍ NHỚ SRS",
        title = "Ghi nhớ ngắt quãng SRS\nbiến từ vựng thành phản xạ",
        description = "Áp dụng thuật toán Spaced Repetition khoa học, tự động tính toán thời điểm vàng ôn tập để ghi nhớ từ vựng sâu bền vững.",
        iconResId = R.drawable.ic_flashcard,
        themeColor = AmberGold,
        themeBgColor = AmberGoldBg
    ),
    OnboardingPage(
        badge = "LỘ TRÌNH CHINH PHỤC",
        title = "Bứt phá HSK 1 - 6\nvới kho đề luyện toàn diện",
        description = "Hệ thống bài tập mô phỏng đa giác quan (Nghe - Đọc - Viết), theo dõi biểu đồ tiến độ chuẩn xác giúp bạn tự tin đạt điểm tối đa.",
        iconResId = R.drawable.ic_nav_trophy,
        themeColor = Color(0xFF10B981),
        themeBgColor = Color(0xFFD1FAE5)
    )
)

/**
 * Màn hình Onboarding (Intro Walkthrough) hiển thị trước khi vào màn Đăng nhập:
 * - 3 trang HorizontalPager giới thiệu tính năng ứng dụng.
 * - Fluid Tab Indicator (Pill dynamic animation) mượt mà chuẩn iOS.
 * - Nền cực quang pastel Aurora Mesh Glow đồng bộ không gian.
 */
@Composable
fun OnboardingScreen(navController: NavHostController) {
    val pagerState = rememberPagerState { OnboardingPages.size }
    val scope = rememberCoroutineScope()

    val onNavigateToLogin = {
        navController.navigate(AppDestination.Login.route) {
            popUpTo(AppDestination.Onboarding.route) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFFDF8),
                        Color(0xFFFBFDFF),
                        BackgroundClean
                    )
                )
            )
    ) {
        // Nền cực quang pastel mềm mại phía sau
        OnboardingAuroraGlow(modifier = Modifier.fillMaxWidth())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 22.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // ── 1. HEADER: LOGO & NÚT BỎ QUA (SKIP) ──
            OnboardingTopBar(
                onSkipClick = onNavigateToLogin
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── 2. CAROUSEL NỘI DUNG 3 TRANG ──
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { pageIndex ->
                val pageData = OnboardingPages[pageIndex]
                OnboardingCard(pageData = pageData)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── 3. TAB INDICATOR ĐỘNG CHUẨN IOS (FLUID PILL) ──
            FluidTabIndicator(
                pagerState = pagerState,
                pageCount = OnboardingPages.size,
                onIndicatorClick = { targetPage ->
                    scope.launch {
                        pagerState.animateScrollToPage(targetPage)
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(26.dp))

            // ── 4. NÚT ĐIỀU HƯỚNG CHÂN TRANG (TIẾP TỤC / BẮT ĐẦU NGAY) ──
            OnboardingActionButtons(
                isLastPage = pagerState.currentPage == OnboardingPages.lastIndex,
                onNextClick = {
                    if (pagerState.currentPage < OnboardingPages.lastIndex) {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    } else {
                        onNavigateToLogin()
                    }
                }
            )

            Spacer(modifier = Modifier.height(22.dp))
        }
    }
}

/**
 * Thanh Top Bar gồm Logo ứng dụng và nút "Bỏ qua" tinh tế.
 */
@Composable
private fun OnboardingTopBar(
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Logo Lumina tròn với viền sứ siêu mảnh
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(Color.White)
                .border(BorderStroke(0.5.dp, Color(0xFFE0E7FF).copy(alpha = 0.8f)), CircleShape)
                .breathingGlow(minScale = 0.96f, maxScale = 1.04f, durationMillis = 2400),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher),
                contentDescription = "Lumina Logo",
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
            )
        }

        // Nút "Bỏ qua" capsule phẳng không viền
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White.copy(alpha = 0.85f))
                .border(BorderStroke(0.5.dp, BorderSubtle), RoundedCornerShape(16.dp))
                .bounceClick()
                .clickable(onClick = onSkipClick)
                .padding(horizontal = 14.dp, vertical = 7.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Bỏ qua",
                fontFamily = PlusJakartaSans,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextMuted
            )
        }
    }
}

/**
 * Bento Card chứa nội dung minh họa và thông tin của từng trang Onboarding.
 */
@Composable
private fun OnboardingCard(
    pageData: OnboardingPage,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Khối hình minh họa Vector Bento trung tâm
        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(Color.White)
                .border(BorderStroke(0.5.dp, BorderSubtle), RoundedCornerShape(32.dp)),
            contentAlignment = Alignment.Center
        ) {
            // Hào quang mờ sau icon
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(pageData.themeBgColor)
                    .breathingGlow(minScale = 0.92f, maxScale = 1.08f, durationMillis = 2000)
            )

            // Icon đại diện tính năng
            Icon(
                painter = painterResource(id = pageData.iconResId),
                contentDescription = pageData.badge,
                tint = pageData.themeColor,
                modifier = Modifier.size(54.dp)
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Badge danh mục phẳng
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(pageData.themeBgColor)
                .border(BorderStroke(0.5.dp, pageData.themeColor.copy(alpha = 0.25f)), RoundedCornerShape(20.dp))
                .padding(horizontal = 12.dp, vertical = 5.dp)
        ) {
            Text(
                text = pageData.badge,
                fontFamily = PlusJakartaSans,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                color = pageData.themeColor,
                letterSpacing = 0.8.sp
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Tiêu đề trang
        Text(
            text = pageData.title,
            fontFamily = PlusJakartaSans,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextMain,
            textAlign = TextAlign.Center,
            lineHeight = 32.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Mô tả chi tiết
        Text(
            text = pageData.description,
            fontFamily = PlusJakartaSans,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = TextMuted,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
    }
}

/**
 * Tab Indicator động (Fluid Dynamic Pill Indicator) chuẩn phong cách iOS:
 * - Dot active tự động dãn dài thành thanh pill (28dp).
 * - Các dot inactive là hình tròn (8dp).
 * - Chuyển động mượt mà với spring physics.
 */
@Composable
private fun FluidTabIndicator(
    pagerState: PagerState,
    pageCount: Int,
    onIndicatorClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            val isSelected = pagerState.currentPage == index

            // Chiều rộng co giãn động mượt mà bằng Spring physics
            val indicatorWidth by animateDpAsState(
                targetValue = if (isSelected) 28.dp else 8.dp,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMediumLow
                ),
                label = "tab_indicator_width_$index"
            )

            val indicatorColor = if (isSelected) BrandIndigo else Color(0xFFCBD5E1)

            Box(
                modifier = Modifier
                    .height(8.dp)
                    .width(indicatorWidth)
                    .clip(CircleShape)
                    .background(indicatorColor)
                    .clickable { onIndicatorClick(index) }
            )
        }
    }
}

/**
 * Nút bấm tiếp tục hoặc hoàn tất ở chân trang.
 */
@Composable
private fun OnboardingActionButtons(
    isLastPage: Boolean,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onNextClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .bounceClick(),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = BrandIndigo,
            contentColor = Color.White
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        AnimatedContent(
            targetState = isLastPage,
            transitionSpec = {
                fadeIn(tween(220)) togetherWith fadeOut(tween(180))
            },
            label = "button_text_anim"
        ) { lastPage ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (lastPage) "Bắt đầu ngay" else "Tiếp tục",
                    fontFamily = PlusJakartaSans,
                    fontSize = 15.5.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.width(6.dp))

                Icon(
                    painter = painterResource(id = R.drawable.ic_chevron_right),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

/**
 * Quầng sáng cực quang pastel nhẹ nhàng phía sau nền Onboarding (Aurora Mesh Glow).
 */
@Composable
private fun OnboardingAuroraGlow(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "onboarding_aurora")

    val driftX by infiniteTransition.animateFloat(
        initialValue = -18f,
        targetValue = 18f,
        animationSpec = infiniteRepeatable(
            animation = tween(7500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "onboarding_drift_x"
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(420.dp)
            .graphicsLayer { alpha = 0.85f }
    ) {
        val w = size.width

        // Khối cực quang Indigo ở góc trên phải
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFE0E7FF).copy(alpha = 0.70f),
                    Color(0xFFBAE6FD).copy(alpha = 0.35f),
                    Color.Transparent
                ),
                center = Offset(w * 0.85f + driftX, -10f),
                radius = w * 0.65f
            ),
            radius = w * 0.65f,
            center = Offset(w * 0.85f + driftX, -10f)
        )

        // Khối cực quang Champagne ở giữa
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFFEF3C7).copy(alpha = 0.55f),
                    Color(0xFFFDE68A).copy(alpha = 0.22f),
                    Color.Transparent
                ),
                center = Offset(w * 0.40f - driftX, 30f),
                radius = w * 0.55f
            ),
            radius = w * 0.55f,
            center = Offset(w * 0.40f - driftX, 30f)
        )
    }
}
