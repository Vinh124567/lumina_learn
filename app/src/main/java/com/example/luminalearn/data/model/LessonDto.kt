package com.example.luminalearn.data.model

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import com.example.luminalearn.presentation.lesson.component.ChineseLessonData
import com.example.luminalearn.presentation.lesson.component.SlideType
import com.example.luminalearn.presentation.lesson.component.ToneCardData
import com.example.luminalearn.presentation.lesson.component.ToneRuleItem
import com.example.luminalearn.presentation.main.component.LessonCardData
import com.google.gson.annotations.SerializedName

data class ToneRuleDto(
    @SerializedName("number") val number: Int = 1,
    @SerializedName("text") val text: String = ""
)

data class SlideDto(
    @SerializedName("slideIndex") val slideIndex: Int = 1,
    @SerializedName("category") val category: String = "",
    @SerializedName("cardType") val cardType: String = "",
    @SerializedName("slideType") val slideType: String = "CONCEPT",
    @SerializedName("subTitle") val subTitle: String = "",
    @SerializedName("title") val title: String = "",
    @SerializedName("pinyinVariants") val pinyinVariants: List<String> = emptyList(),
    @SerializedName("hanziVariants") val hanziVariants: List<String> = emptyList(),
    @SerializedName("hanViet") val hanViet: String = "",
    @SerializedName("meaning") val meaning: String = "",
    @SerializedName("explanationText") val explanationText: String = "",
    @SerializedName("explanationSubtitle") val explanationSubtitle: String = "",
    @SerializedName("explanationContent") val explanationContent: String = "",
    @SerializedName("boxColor") val boxColor: String = "",
    @SerializedName("toneRules") val toneRules: List<ToneRuleDto> = emptyList(),
    @SerializedName("audioUrl") val audioUrl: String = "",
    @SerializedName("question") val question: String = "",
    @SerializedName("options") val options: List<String> = emptyList(),
    @SerializedName("correctAnswerIndex") val correctAnswerIndex: Int = -1,
    @SerializedName("quizExplanation") val quizExplanation: String = "",
    @SerializedName("currentIndex") val currentIndex: Int = 1,
    @SerializedName("totalCount") val totalCount: Int = 4
)

data class GrammarExampleDto(
    @SerializedName("hanzi") val hanzi: String = "",
    @SerializedName("pinyinOriginal") val pinyinOriginal: String = "",
    @SerializedName("pinyinActual") val pinyinActual: String = "",
    @SerializedName("meaning") val meaning: String = "",
    @SerializedName("tip") val tip: String? = null,
    @SerializedName("warning") val warning: String? = null,
    @SerializedName("audioText") val audioText: String? = null
)

data class GrammarStructureDto(
    @SerializedName("structureOrder") val structureOrder: Int = 1,
    @SerializedName("title") val title: String = "",
    @SerializedName("formula") val formula: String = "",
    @SerializedName("explanation") val explanation: String = "",
    @SerializedName("examples") val examples: List<GrammarExampleDto> = emptyList(),
    @SerializedName("examTip") val examTip: String? = null
)

data class DialogueLineDto(
    @SerializedName("speakerRole") val speakerRole: String = "A",
    @SerializedName("speakerName") val speakerName: String = "",
    @SerializedName("chinese") val chinese: String = "",
    @SerializedName("pinyin") val pinyin: String = "",
    @SerializedName("vietnamese") val vietnamese: String = "",
    @SerializedName("badgeColor") val badgeColor: String = "#5538EE"
)

data class LessonVocabDto(
    @SerializedName("hanzi") val hanzi: String = "",
    @SerializedName("pinyin") val pinyin: String = "",
    @SerializedName("hanViet") val hanViet: String = "",
    @SerializedName("meaning") val meaning: String = "",
    @SerializedName("partOfSpeech") val partOfSpeech: String = "",
    @SerializedName("exampleHanzi") val exampleHanzi: String = "",
    @SerializedName("examplePinyin") val examplePinyin: String = "",
    @SerializedName("exampleMeaning") val exampleMeaning: String = ""
)

data class LessonDto(
    @SerializedName("id") val id: String,
    @SerializedName("category") val category: String,
    @SerializedName("categoryBgColor") val categoryBgColor: String = "#FCE7F3",
    @SerializedName("categoryTextColor") val categoryTextColor: String = "#9333EA",
    @SerializedName("level") val level: String = "Cơ bản",
    @SerializedName("pinyinHanziTitle") val pinyinHanziTitle: String = "",
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String = "",
    @SerializedName("durationMins") val durationMins: Int = 0,
    @SerializedName("sparks") val sparks: Int = 0,
    @SerializedName("isCompleted") val isCompleted: Boolean = false,
    @SerializedName("totalSlides") val totalSlides: Int = 0,
    @SerializedName("objectives") val objectives: List<String> = emptyList(),
    @SerializedName("grammarStructures") val grammarStructures: List<GrammarStructureDto> = emptyList(),
    @SerializedName("dialogueContext") val dialogueContext: String = "",
    @SerializedName("dialogues") val dialogues: List<DialogueLineDto> = emptyList(),
    @SerializedName("coreVocabularies") val coreVocabularies: List<LessonVocabDto> = emptyList(),
    @SerializedName("slides") val slides: List<SlideDto> = emptyList()
)

fun LessonDto.toChineseLessonData(): ChineseLessonData {
    val bgColor = try {
        Color(categoryBgColor.toColorInt())
    } catch (_: Exception) {
        Color(0xFFF3E8FF)
    }

    val textColor = try {
        Color(categoryTextColor.toColorInt())
    } catch (_: Exception) {
        Color(0xFF7E22CE)
    }

    val mappedGrammar = grammarStructures.map { s ->
        com.example.luminalearn.presentation.lesson.component.GrammarStructureData(
            structureOrder = s.structureOrder,
            title = s.title,
            formula = s.formula,
            explanation = s.explanation,
            examples = s.examples.map { e ->
                com.example.luminalearn.presentation.lesson.component.GrammarExampleData(
                    hanzi = e.hanzi,
                    pinyinOriginal = e.pinyinOriginal,
                    pinyinActual = e.pinyinActual,
                    meaning = e.meaning,
                    tip = e.tip,
                    warning = e.warning,
                    audioText = e.audioText
                )
            },
            examTip = s.examTip
        )
    }

    val mappedDialogues = dialogues.map { d ->
        val c = try {
            Color(d.badgeColor.toColorInt())
        } catch (_: Exception) {
            Color(0xFF5538EE)
        }
        com.example.luminalearn.presentation.lesson.component.LessonDialogueData(
            speakerRole = d.speakerRole,
            speakerName = d.speakerName,
            chinese = d.chinese,
            pinyin = d.pinyin,
            vietnamese = d.vietnamese,
            badgeColor = c
        )
    }

    val mappedVocab = coreVocabularies.map { v ->
        com.example.luminalearn.presentation.lesson.component.LessonCoreVocabData(
            hanzi = v.hanzi,
            pinyin = v.pinyin,
            hanViet = v.hanViet,
            meaning = v.meaning,
            partOfSpeech = v.partOfSpeech,
            exampleHanzi = v.exampleHanzi,
            examplePinyin = v.examplePinyin,
            exampleMeaning = v.exampleMeaning
        )
    }

    return ChineseLessonData(
        id = id,
        category = category,
        level = level,
        pinyinHanziTitle = pinyinHanziTitle,
        title = title,
        description = description,
        durationMins = durationMins,
        sparks = sparks,
        isCompleted = isCompleted,
        totalSlides = totalSlides,
        slides = slides.map { it.toToneCardData() },
        categoryBgColor = bgColor,
        categoryTextColor = textColor,
        objectives = objectives,
        grammarStructures = mappedGrammar,
        dialogueContext = dialogueContext,
        dialogues = mappedDialogues,
        coreVocabularies = mappedVocab
    )
}

fun LessonDto.toLessonCardData(): LessonCardData {
    val bgColor = try {
        Color(categoryBgColor.toColorInt())
    } catch (_: Exception) {
        Color(0xFFFCE7F3)
    }

    val textColor = try {
        Color(categoryTextColor.toColorInt())
    } catch (_: Exception) {
        Color(0xFF9333EA)
    }

    return LessonCardData(
        category = category,
        categoryBgColor = bgColor,
        categoryTextColor = textColor,
        durationMins = durationMins,
        sparks = sparks,
        title = title,
        description = description,
        slideCount = totalSlides
    )
}

fun SlideDto.toToneCardData(): ToneCardData {
    return ToneCardData(
        slideIndex = slideIndex,
        category = category,
        cardType = cardType,
        slideType = SlideType.fromString(slideType),
        subTitle = subTitle,
        title = title,
        pinyinVariants = pinyinVariants,
        hanziVariants = hanziVariants,
        hanViet = hanViet,
        meaning = meaning,
        explanationText = explanationText,
        explanationSubtitle = explanationSubtitle,
        explanationContent = explanationContent,
        boxColor = boxColor,
        toneRules = toneRules.map { ToneRuleItem(number = it.number, text = it.text) },
        audioUrl = audioUrl,
        isPlaying = false,
        question = question,
        options = options,
        correctAnswerIndex = correctAnswerIndex,
        quizExplanation = quizExplanation,
        currentIndex = currentIndex,
        totalCount = totalCount
    )
}
