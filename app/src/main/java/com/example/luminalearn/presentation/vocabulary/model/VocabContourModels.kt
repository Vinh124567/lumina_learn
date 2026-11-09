package com.example.luminalearn.presentation.vocabulary.model

/**
 * Chế độ lọc hiển thị của màn hình biểu đồ cao độ & sóng âm
 */
enum class ContourViewMode {
    ALL, PITCH_ONLY, WAVE_ONLY
}

/**
 * Tab điều hướng phụ trong màn hình Luyện thanh điệu & Cao độ
 */
enum class VocabPitchSubTab {
    PITCH_CONTOUR,   // Biểu đồ Pitch Contour
    DIAGNOSTIC,      // Chuẩn đoán âm
    TONE_PRACTICE    // Luyện thanh điệu
}

/**
 * Thông tin chi tiết về từng âm tiết và thanh điệu
 */
data class SyllableToneInfo(
    val hanzi: String,
    val pinyin: String,
    val toneName: String,
    val toneCode: String,
    val toneNumber: Int,
    val score: Int
)

data class ToneDetail(
    val name: String,
    val code: String,
    val number: Int
)

/**
 * Phân tích pinyin thành các âm tiết tách rời (ngay cả khi dataset viết liền như "xiànglái")
 */
fun parseSyllables(hanzi: String, pinyin: String): List<SyllableToneInfo> {
    val cleanPinyin = pinyin.trim()
    val hanziChars = hanzi.toCharArray().map { it.toString() }

    val spaceTokens = cleanPinyin.split("\\s+".toRegex()).filter { it.isNotBlank() }
    val pinyinSyllables: List<String> = if (spaceTokens.size == hanziChars.size) {
        spaceTokens
    } else if (hanziChars.size == 2 && spaceTokens.size == 1) {
        splitTwoSyllables(cleanPinyin)
    } else {
        spaceTokens
    }

    return hanziChars.mapIndexed { index, h ->
        val py = pinyinSyllables.getOrNull(index) ?: if (index == 0) cleanPinyin else ""
        val (toneName, toneCode, toneNum) = detectTone(py)
        SyllableToneInfo(
            hanzi = h,
            pinyin = py,
            toneName = toneName,
            toneCode = toneCode,
            toneNumber = toneNum,
            score = if (index == 0) 96 else 94
        )
    }
}

private fun splitTwoSyllables(pinyin: String): List<String> {
    val toneVowels = "āáǎàēéěèīíǐìōóǒòūúǔùǖǘǚǜ"
    val vowelIndices = mutableListOf<Int>()
    for (i in pinyin.indices) {
        if (pinyin[i] in toneVowels) {
            vowelIndices.add(i)
        }
    }

    if (vowelIndices.size >= 2) {
        val splitPoint = (vowelIndices[0] + vowelIndices[1]) / 2 + 1
        return listOf(pinyin.substring(0, splitPoint), pinyin.substring(splitPoint))
    }

    val mid = pinyin.length / 2
    return listOf(pinyin.substring(0, mid), pinyin.substring(mid))
}

fun detectTone(pinyinSyllable: String): ToneDetail {
    return when {
        pinyinSyllable.any { it in "āēīōūǖ" } -> ToneDetail("Thanh 1", "55", 1)
        pinyinSyllable.any { it in "áéíóúǘ" } -> ToneDetail("Thanh 2", "35", 2)
        pinyinSyllable.any { it in "ǎěǐǒǔǚ" } -> ToneDetail("Thanh 3", "214", 3)
        pinyinSyllable.any { it in "àèìòùǜ" } -> ToneDetail("Thanh 4", "51", 4)
        else -> ToneDetail("Khinh thanh", "2", 5)
    }
}
