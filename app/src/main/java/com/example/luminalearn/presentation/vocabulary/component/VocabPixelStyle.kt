package com.example.luminalearn.presentation.vocabulary.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminalearn.core.ui.effect.bounceClick

// ── BẬC THANG PIXEL 90° CHUẨN JRPG 16-BIT RETRO ──
class PixelStepCornerShape(private val step: Dp = 3.5.dp) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val s = with(density) { step.toPx() }
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(s, 0f)
            lineTo(w - s, 0f)
            lineTo(w - s, s)
            lineTo(w, s)
            lineTo(w, h - s)
            lineTo(w - s, h - s)
            lineTo(w - s, h)
            lineTo(s, h)
            lineTo(s, h - s)
            lineTo(0f, h - s)
            lineTo(0f, s)
            lineTo(s, s)
            close()
        }
        return Outline.Generic(path)
    }
}

// ── BẢNG MÀU JRPG RETRO SANG TRỌNG (STARDEW / POKÉMON / OCTOPATH) ──
object PixelColors {
    val BorderDark = Color(0xFF1E1B18)       // Nâu gỗ mun / Viền đen cổ điển JRPG
    val BorderGold = Color(0xFFD97706)       // Chỉ vàng Gold hoàng gia
    val BorderGoldLight = Color(0xFFFDE68A)  // Ánh vàng phản quang
    val ShadowDark = Color(0xFF334155)       // Gờ kim loại xám bạc Slate (không đen xì)
    val ShadowSilver = Color(0xFF64748B)     // Bạc kim loại phản quang (Metallic Silver)
    val SilverRivet = Color(0xFFE2E8F0)      // Màu bạc của đinh tán ốc vít
    val SilverRivetSlot = Color(0xFF475569)  // Màu rãnh chữ thập ốc vít
    val CardBg = Color(0xFFFFFDF8)           // Giấy da ấm JRPG Parchment
    val RetroCream = Color(0xFFFFFBEB)       // Nền giấy da / GameBoy Color
    
    // Màu ngọc quý phong cách RPG
    val PixelYellow = Color(0xFFF59E0B)      // Vàng hoàng gia Gold
    val PixelYellowDark = Color(0xFFB45309)  // Vàng sẫm
    val PixelGreen = Color(0xFF16A34A)       // Xanh Ngọc Lục Bảo (Emerald)
    val PixelGreenDark = Color(0xFF14532D)
    val PixelRed = Color(0xFFDC2626)         // Đỏ Hồng Ngọc (Ruby HP)
    val PixelRedDark = Color(0xFF7F1D1D)
    val PixelPurple = Color(0xFF7C3AED)      // Tím Thạch Anh (Amethyst Mana)
    val PixelPurpleDark = Color(0xFF4C1D95)
    val PixelBlue = Color(0xFF0284C7)        // Xanh Sapphire Cyan
    val PixelBlueDark = Color(0xFF075985)
    val PixelOrange = Color(0xFFEA580C)      // Cam Hổ Phách (Amber)
    val PixelPink = Color(0xFFDB2777)        // Hồng Ngọc Bích
    val MutedText = Color(0xFF475569)        // Chữ phụ slate

    // Aliases linh hoạt
    val NeonYellow = PixelYellow
    val NeonGreen = PixelGreen
    val NeonOrange = PixelOrange
    val NeonCyan = PixelBlue
    val CardGreen = Color(0xFFDCFCE7)
    val CardOrange = Color(0xFFFEF3C7)
    val CardYellow = Color(0xFFFEF9C3)
    val CardPurple = Color(0xFFF3E8FF)
    val CardBlue = Color(0xFFE0F2FE)

    // Arcade & Console UI Aliases
    val ArcadeAmber = PixelYellow
    val ArcadeGreen = PixelGreen
    val ArcadeRed = PixelRed
    val ArcadePurple = PixelPurple
    val ArcadeBlue = PixelBlue
    val SurfaceDark = Color(0xFF1E1B18)
    val SurfaceLight = Color(0xFFFFFDF8)
    val TextDark = Color(0xFF1E1B18)
    val TextLight = Color(0xFFFFFFFF)
    val TextMuted = MutedText
}

enum class PixelCornerStyle {
    None,
    Rivets,         // Ốc vít kim loại (cho cartridge/rương đồ)
    GoldenBrackets, // Góc bọc chỉ vàng hoàng gia JRPG (tinh tế, thanh lịch)
    Gems,           // Viên ngọc RPG 16-bit lấp lánh
    Sparkles        // Ngôi sao 4 cánh pixel ✦
}

/**
 * Thẻ Hộp thoại JRPG Retro Hoàng gia (JRPG Dialogue / Cartridge Box):
 * - Góc bậc thang Pixel 90° chân thực (PixelStepCornerShape).
 * - Viền đôi nghệ thuật JRPG (Double Inset Border) dải chỉ vàng.
 * - Gờ bóng 3D kim loại ánh bạc Slate, không đen xì.
 * - Hỗ trợ đa dạng hoa văn góc: None, GoldenBrackets, Gems, Sparkles, Rivets (không bị lạm dụng đinh tán).
 */
@Composable
fun PixelCardBox(
    modifier: Modifier = Modifier,
    backgroundColor: Color = PixelColors.CardBg,
    borderColor: Color = PixelColors.BorderDark,
    innerBorderColor: Color = PixelColors.BorderGold,
    shadowColor: Color = PixelColors.ShadowDark,
    borderWidth: Dp = 1.8.dp,
    shadowOffset: Dp = 2.5.dp,
    cornerRadius: Dp = 3.5.dp,
    stepSize: Dp = cornerRadius.coerceIn(2.5.dp, 4.dp),
    shape: Shape = PixelStepCornerShape(stepSize),
    headerTitle: String? = null,
    headerBadge: String? = null,
    headerColor: Color? = null,
    showGrille: Boolean = false,
    showRivets: Boolean = false,
    cornerStyle: PixelCornerStyle = when {
        showRivets -> PixelCornerStyle.Rivets
        headerTitle != null -> PixelCornerStyle.Rivets // Card lớn có header tự động có đinh ở góc
        else -> PixelCornerStyle.None // Card nhỏ giữ sạch sẽ, không lạm dụng đinh
    },
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val boxModifier = if (onClick != null) {
        modifier.bounceClick(scaleDown = 0.97f, onClick = onClick)
    } else {
        modifier
    }

    Box(modifier = boxModifier) {
        // 1. Lớp gờ máy 3D kim loại ánh bạc phía sau (Silver Slate Shadow)
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = shadowOffset, y = shadowOffset)
                .clip(shape)
                .background(shadowColor)
                .border(BorderStroke(borderWidth, borderColor), shape)
        ) {
            // Đường phản quang kim loại ánh bạc ở mép ngoài
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .padding(end = 1.dp, bottom = 1.dp)
                    .border(
                        BorderStroke(1.dp, Color(0xFF94A3B8).copy(alpha = 0.45f)),
                        shape
                    )
            )
        }

        // 2. Lớp mặt trước (Card Surface) có viền đôi JRPG
        Box(
            modifier = Modifier
                .clip(shape)
                .background(backgroundColor)
                .border(BorderStroke(borderWidth, borderColor), shape)
        ) {
            // Đường chỉ viền đôi bên trong (Inset Border) tạo phong cách hộp thoại JRPG sang trọng
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .padding(2.5.dp)
                    .border(
                        BorderStroke(1.dp, innerBorderColor.copy(alpha = 0.65f)),
                        PixelStepCornerShape((stepSize - 1.dp).coerceAtLeast(1.dp))
                    )
            )

            if (headerTitle != null) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Header Banner Ruy băng vương giả JRPG
                    val actualHeaderColor = headerColor ?: PixelColors.PixelYellow
                    val headerHorizPadding = if (cornerStyle == PixelCornerStyle.Rivets) 18.dp else 12.dp
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(actualHeaderColor)
                            .padding(horizontal = headerHorizPadding, vertical = 7.dp)
                    ) {
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
                                    text = headerTitle,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    color = PixelColors.BorderDark,
                                    letterSpacing = 0.5.sp
                                )

                                if (headerBadge != null) {
                                    PixelBadge(
                                        text = headerBadge,
                                        backgroundColor = Color.White,
                                        textColor = PixelColors.BorderDark
                                    )
                                }
                            }

                            if (showGrille) {
                                Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                                    repeat(3) {
                                        Box(
                                            modifier = Modifier
                                                .width(10.dp)
                                                .height(2.5.dp)
                                                .background(PixelColors.BorderDark.copy(alpha = 0.35f))
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Đường ngăn cách chỉ vàng giữa Header Banner và thân thẻ
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(borderWidth)
                            .background(borderColor)
                    )

                    // Thân thẻ chính
                    Box(modifier = Modifier.fillMaxWidth()) {
                        content()

                        // Hoa văn trang trí ở 2 góc dưới thẻ
                        when (cornerStyle) {
                            PixelCornerStyle.Rivets -> {
                                PixelRivet(modifier = Modifier.align(Alignment.BottomStart).padding(6.dp))
                                PixelRivet(modifier = Modifier.align(Alignment.BottomEnd).padding(6.dp))
                            }
                            PixelCornerStyle.GoldenBrackets -> {
                                PixelCornerBracket(modifier = Modifier.align(Alignment.BottomStart).padding(5.dp))
                                PixelCornerBracket(modifier = Modifier.align(Alignment.BottomEnd).padding(5.dp))
                            }
                            PixelCornerStyle.Gems -> {
                                PixelGem(modifier = Modifier.align(Alignment.BottomStart).padding(6.dp))
                                PixelGem(modifier = Modifier.align(Alignment.BottomEnd).padding(6.dp))
                            }
                            PixelCornerStyle.Sparkles -> {
                                PixelSparkle(modifier = Modifier.align(Alignment.BottomStart).padding(6.dp))
                                PixelSparkle(modifier = Modifier.align(Alignment.BottomEnd).padding(6.dp))
                            }
                            PixelCornerStyle.None -> {}
                        }
                    }
                }

                // 2 Đinh tán ở 2 góc trên của Header cho Card lớn
                if (cornerStyle == PixelCornerStyle.Rivets) {
                    PixelRivet(modifier = Modifier.align(Alignment.TopStart).padding(6.dp))
                    PixelRivet(modifier = Modifier.align(Alignment.TopEnd).padding(6.dp))
                }
            } else {
                // Thẻ không header
                Box {
                    content()

                    // Hoa văn trang trí ở 4 góc thẻ
                    when (cornerStyle) {
                        PixelCornerStyle.Rivets -> {
                            PixelRivet(modifier = Modifier.align(Alignment.TopStart).padding(6.dp))
                            PixelRivet(modifier = Modifier.align(Alignment.TopEnd).padding(6.dp))
                            PixelRivet(modifier = Modifier.align(Alignment.BottomStart).padding(6.dp))
                            PixelRivet(modifier = Modifier.align(Alignment.BottomEnd).padding(6.dp))
                        }
                        PixelCornerStyle.GoldenBrackets -> {
                            PixelCornerBracket(modifier = Modifier.align(Alignment.TopStart).padding(5.dp))
                            PixelCornerBracket(modifier = Modifier.align(Alignment.TopEnd).padding(5.dp))
                            PixelCornerBracket(modifier = Modifier.align(Alignment.BottomStart).padding(5.dp))
                            PixelCornerBracket(modifier = Modifier.align(Alignment.BottomEnd).padding(5.dp))
                        }
                        PixelCornerStyle.Gems -> {
                            PixelGem(modifier = Modifier.align(Alignment.TopStart).padding(6.dp))
                            PixelGem(modifier = Modifier.align(Alignment.TopEnd).padding(6.dp))
                            PixelGem(modifier = Modifier.align(Alignment.BottomStart).padding(6.dp))
                            PixelGem(modifier = Modifier.align(Alignment.BottomEnd).padding(6.dp))
                        }
                        PixelCornerStyle.Sparkles -> {
                            PixelSparkle(modifier = Modifier.align(Alignment.TopStart).padding(6.dp))
                            PixelSparkle(modifier = Modifier.align(Alignment.TopEnd).padding(6.dp))
                            PixelSparkle(modifier = Modifier.align(Alignment.BottomStart).padding(6.dp))
                            PixelSparkle(modifier = Modifier.align(Alignment.BottomEnd).padding(6.dp))
                        }
                        PixelCornerStyle.None -> {}
                    }
                }
            }
        }
    }
}

/**
 * Góc bọc kim loại chỉ vàng hoàng gia JRPG (Golden Corner Bracket).
 * Nhẹ nhàng, sang trọng và không bị thô cứng như đinh tán.
 */
@Composable
fun PixelCornerBracket(
    modifier: Modifier = Modifier,
    color: Color = PixelColors.BorderGold,
    size: Dp = 5.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .border(BorderStroke(1.2.dp, color), PixelStepCornerShape(1.dp))
    )
}

/**
 * Viên đá quý RPG 16-bit lấp lánh (Pixel Gem: Ruby, Emerald, Sapphire, Topaz).
 */
@Composable
fun PixelGem(
    modifier: Modifier = Modifier,
    gemColor: Color = Color(0xFFE11D48), // Ruby
    size: Dp = 6.dp
) {
    val gemShape = PixelStepCornerShape(1.dp)
    Box(
        modifier = modifier
            .size(size)
            .clip(gemShape)
            .background(gemColor)
            .border(BorderStroke(0.8.dp, PixelColors.BorderDark), gemShape)
    ) {
        // Điểm sáng Highlight trắng pixel
        Box(
            modifier = Modifier
                .size(1.8.dp)
                .align(Alignment.TopStart)
                .offset(x = 0.8.dp, y = 0.8.dp)
                .background(Color.White)
        )
    }
}

/**
 * Ngôi sao 4 cánh pixel ✦ (Pixel Star Sparkle) biểu tượng ma thuật RPG.
 */
@Composable
fun PixelSparkle(
    modifier: Modifier = Modifier,
    color: Color = PixelColors.BorderGold,
    size: Dp = 6.dp
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(1.5.dp)
                .height(size)
                .background(color)
        )
        Box(
            modifier = Modifier
                .width(size)
                .height(1.5.dp)
                .background(color)
        )
        Box(
            modifier = Modifier
                .size(1.8.dp)
                .background(Color.White)
        )
    }
}

/**
 * Đinh tán ốc vít kim loại Pixel 6.5x6.5 dập nổi (Retro Metal Screw Rivet).
 * Khung viền sắc nét, mặt bạc kim loại kèm rãnh chữ thập (+) 16-bit arcade.
 */
@Composable
fun PixelRivet(
    modifier: Modifier = Modifier,
    size: Dp = 6.5.dp,
    screwColor: Color = PixelColors.SilverRivet,
    slotColor: Color = PixelColors.SilverRivetSlot
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(1.5.dp))
            .background(screwColor)
            .border(BorderStroke(1.dp, PixelColors.BorderDark), RoundedCornerShape(1.5.dp)),
        contentAlignment = Alignment.Center
    ) {
        // Rãnh ốc vít Pixel chữ thập (+)
        Box(
            modifier = Modifier
                .width(3.5.dp)
                .height(1.dp)
                .background(slotColor)
        )
        Box(
            modifier = Modifier
                .width(1.dp)
                .height(3.5.dp)
                .background(slotColor)
        )
    }
}

/**
 * Nút bấm JRPG 3 tầng dập nổi (3-Layer Embossed Button):
 * - Bậc thang pixel (PixelStepCornerShape 2.5dp).
 * - Gờ Bevel sáng phản quang ở nóc phím.
 * - Gờ bóng tối dập nổi ở đáy phím tạo độ nảy cơ học.
 */
@Composable
fun PixelArcadeButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = PixelColors.PixelYellow,
    backgroundColor: Color = color,
    textColor: Color = PixelColors.BorderDark,
    borderColor: Color = PixelColors.BorderDark,
    shadowColor: Color = PixelColors.ShadowDark,
    shadowOffset: Dp = 2.5.dp,
    cornerRadius: Dp = 2.5.dp,
    stepSize: Dp = cornerRadius.coerceIn(2.dp, 3.5.dp),
    fontSize: Float = 12f,
    contentPadding: Dp = 8.dp,
    fillMaxWidth: Boolean = false,
    height: Dp? = null
) {
    val shape = PixelStepCornerShape(stepSize)

    val buttonModifier = modifier
        .then(if (fillMaxWidth) Modifier.fillMaxWidth() else Modifier)
        .then(if (height != null) Modifier.height(height) else Modifier)
        .bounceClick(scaleDown = 0.94f, onClick = onClick)

    Box(
        modifier = buttonModifier
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = shadowOffset, y = shadowOffset)
                .clip(shape)
                .background(shadowColor)
                .border(BorderStroke(1.2.dp, borderColor), shape)
        )

        val innerModifier = Modifier
            .then(if (fillMaxWidth) Modifier.fillMaxWidth() else Modifier)
            .then(if (height != null) Modifier.fillMaxHeight() else Modifier)

        Box(
            modifier = innerModifier
                .clip(shape)
                .background(backgroundColor)
                .border(BorderStroke(1.5.dp, borderColor), shape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .padding(top = 1.dp, start = 1.dp, end = 1.dp)
                    .border(
                        BorderStroke(1.dp, Color.White.copy(alpha = 0.55f)),
                        PixelStepCornerShape((stepSize - 0.5.dp).coerceAtLeast(0.5.dp))
                    )
            )

            Text(
                text = text,
                color = textColor,
                fontSize = fontSize.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.5.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = contentPadding)
            )
        }
    }
}

@Composable
fun PixelBadge(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = PixelColors.PixelYellow,
    backgroundColor: Color = color,
    textColor: Color = PixelColors.BorderDark,
    borderColor: Color = PixelColors.BorderDark,
    stepSize: Dp = 1.5.dp
) {
    val shape = PixelStepCornerShape(stepSize)
    Box(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor)
            .border(BorderStroke(1.2.dp, borderColor), shape)
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        // Bevel highlight nhẹ
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(0.8.dp)
                .border(BorderStroke(0.8.dp, Color.White.copy(alpha = 0.4f)), shape)
        )

        Text(
            text = text,
            color = textColor,
            fontSize = 9.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 0.5.sp
        )
    }
}

/**
 * Thanh máu / năng lượng HP/EXP RPG 16-bit có nhãn nhúng (HP, MP, EXP) và vạch chia ô pixel.
 */
@Composable
fun PixelRpgProgressBar(
    label: String, // ví dụ: "EXP", "HP", "MAP"
    progress: Float, // 0f .. 1f
    modifier: Modifier = Modifier,
    labelColor: Color = PixelColors.PixelYellow,
    barColor: Color = PixelColors.PixelGreen,
    emptyColor: Color = Color(0xFF1E293B),
    borderColor: Color = PixelColors.BorderDark,
    segments: Int = 12
) {
    val activeSegments = (progress.coerceIn(0f, 1f) * segments).toInt()

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Nhãn chỉ số RPG (EXP, HP)
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(3.dp))
                .background(labelColor)
                .border(BorderStroke(1.5.dp, borderColor), RoundedCornerShape(3.dp))
                .padding(horizontal = 4.dp, vertical = 1.dp)
        ) {
            Text(
                text = label,
                color = PixelColors.BorderDark,
                fontSize = 8.5.sp,
                fontWeight = FontWeight.Black
            )
        }

        Spacer(modifier = Modifier.width(4.dp))

        // Thanh vạch chia ô pixel
        Box(
            modifier = Modifier
                .weight(1f)
                .height(14.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(emptyColor)
                .border(BorderStroke(1.5.dp, borderColor), RoundedCornerShape(3.dp))
                .padding(1.5.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                horizontalArrangement = Arrangement.spacedBy(1.5.dp)
            ) {
                for (i in 0 until segments) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(
                                if (i < activeSegments) barColor else Color.Transparent
                            )
                    )
                }
            }
        }
    }
}

/**
 * Thanh tiến độ chia vạch pixel (Segmented HP/EXP Bar) cơ bản.
 */
@Composable
fun PixelSegmentedProgressBar(
    progress: Float, // 0f .. 1f
    modifier: Modifier = Modifier,
    fillColor: Color = PixelColors.PixelGreen,
    emptyColor: Color = Color(0xFFE4E4E7),
    borderColor: Color = PixelColors.BorderDark,
    segments: Int = 10
) {
    val activeSegments = (progress.coerceIn(0f, 1f) * segments).toInt()

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(3.dp))
            .background(emptyColor)
            .border(BorderStroke(1.5.dp, borderColor), RoundedCornerShape(3.dp))
            .padding(2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            for (i in 0 until segments) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(if (i < activeSegments) fillColor else Color.Transparent)
                )
            }
        }
    }
}

/**
 * Overload PixelSegmentedProgressBar nhận current và total trực quan theo số câu / số bài.
 */
@Composable
fun PixelSegmentedProgressBar(
    current: Int,
    total: Int,
    modifier: Modifier = Modifier,
    activeColor: Color = PixelColors.PixelGreen,
    fillColor: Color = activeColor,
    emptyColor: Color = Color(0xFFE4E4E7),
    borderColor: Color = PixelColors.BorderDark,
    height: Dp = 8.dp
) {
    val progress = if (total > 0) (current.toFloat() / total).coerceIn(0f, 1f) else 0f
    PixelSegmentedProgressBar(
        progress = progress,
        modifier = modifier.height(height),
        fillColor = fillColor,
        emptyColor = emptyColor,
        borderColor = borderColor,
        segments = total.coerceIn(4, 20)
    )
}

/**
 * Trái tim Pixel 16-bit RPG (❤️ HP Life Indicator).
 */
@Composable
fun PixelHeartIcon(
    isFilled: Boolean = true,
    size: Dp = 14.dp
) {
    Text(
        text = if (isFilled) "❤️" else "🖤",
        fontSize = (size.value * 0.9f).sp
    )
}

/**
 * Huy hiệu Tiền vàng Đồng xu RPG (🪙 Coin / Sparks Badge).
 */
@Composable
fun PixelCoinBadge(
    amount: Int,
    modifier: Modifier = Modifier,
    stepSize: Dp = 1.5.dp
) {
    val shape = PixelStepCornerShape(stepSize)
    Box(
        modifier = modifier
            .clip(shape)
            .background(PixelColors.PixelYellow)
            .border(BorderStroke(1.2.dp, PixelColors.BorderDark), shape)
            .padding(horizontal = 5.dp, vertical = 2.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "🪙",
                fontSize = 9.sp
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = "+$amount G",
                color = PixelColors.BorderDark,
                fontSize = 9.5.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}

