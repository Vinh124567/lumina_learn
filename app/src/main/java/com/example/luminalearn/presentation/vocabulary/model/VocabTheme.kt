package com.example.luminalearn.presentation.vocabulary.model

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Bảng màu dùng chung cho toàn bộ module Từ vựng (tránh duplicate definitions)
 */
object VocabColors {
    val BrandPrimary  = Color(0xFF5C50F6)
    val BrandLight    = Color(0xFFEDE9FE)
    val BrandDark     = Color(0xFF4F46E5)
    val TextDark      = Color(0xFF0F172A)
    val TextSecondary = Color(0xFF475569)
    val TextMuted     = Color(0xFF64748B)
    val BorderLight   = Color(0xFFE2E8F0)
    val ScreenBg     = Color(0xFFF8FAFC)
    val SuccessGreen  = Color(0xFF16A34A)
    val SuccessBg     = Color(0xFFECFDF5)
    val ErrorRed      = Color(0xFFEF4444)
    val ErrorBg       = Color(0xFFFEF2F2)
    val HanVietAmber  = Color(0xFFB45309)
    val HanVietBg     = Color(0xFFFEF3C7)
}

/**
 * Các hình dạng (Shape/Radius) dùng chung cho UI Từ vựng
 */
object VocabShapes {
    val Card      = RoundedCornerShape(18.dp)
    val BigCard   = RoundedCornerShape(24.dp)
    val Tab       = RoundedCornerShape(14.dp)
    val Tag       = RoundedCornerShape(6.dp)
    val Badge     = RoundedCornerShape(5.dp)
    val Hanzi     = RoundedCornerShape(13.dp)
    val ModeTab   = RoundedCornerShape(9.dp)
    val Checkbox  = RoundedCornerShape(7.dp)
    val Option    = RoundedCornerShape(16.dp)
    val Example   = RoundedCornerShape(11.dp)
}
