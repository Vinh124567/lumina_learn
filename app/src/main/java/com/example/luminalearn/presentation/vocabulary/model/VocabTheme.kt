package com.example.luminalearn.presentation.vocabulary.model

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Bảng màu dùng chung cho toàn bộ module Từ vựng (tránh duplicate definitions)
 */
object VocabColors {
    // ── Primary Brand (Đồng bộ 100% với LuminaLearn Primary #224BDD) ──
    val BrandPrimary  = Color(0xFF224BDD)
    val BrandLight    = Color(0xFFEEF2FF)
    val BrandDark     = Color(0xFF1738B5)

    // ── Neutral & Surface (Đồng bộ với Theme app) ──
    val TextDark      = Color(0xFF151A2F)
    val TextSecondary = Color(0xFF444655)
    val TextMuted     = Color(0xFF64748B)
    val BorderLight   = Color(0xFFE2E8F0)
    val ScreenBg      = Color(0xFFFAFBFF)

    // ── Accent & Semantic ──
    val AccentCoral   = Color(0xFFFD8863)
    val AccentCoralBg = Color(0xFFFFE8DF)
    val SuccessGreen  = Color(0xFF10B981)
    val SuccessBg     = Color(0xFFECFDF5)
    val ErrorRed      = Color(0xFFEF4444)
    val ErrorBg       = Color(0xFFFEF2F2)
    val HanVietAmber  = Color(0xFF475569)
    val HanVietBg     = Color(0xFFF1F5F9)
}

/**
 * Các hình dạng (Shape/Radius) dùng chung cho UI Từ vựng
 */
object VocabShapes {
    val Card      = RoundedCornerShape(22.dp)
    val BigCard   = RoundedCornerShape(24.dp)
    val Tab       = RoundedCornerShape(16.dp)
    val Tag       = RoundedCornerShape(8.dp)
    val Badge     = RoundedCornerShape(8.dp)
    val Hanzi     = RoundedCornerShape(16.dp)
    val ModeTab   = RoundedCornerShape(12.dp)
    val Checkbox  = RoundedCornerShape(8.dp)
    val Option    = RoundedCornerShape(18.dp)
    val Example   = RoundedCornerShape(14.dp)
}
