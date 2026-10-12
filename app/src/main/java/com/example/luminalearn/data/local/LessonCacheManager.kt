package com.example.luminalearn.data.local

import android.content.Context
import com.example.luminalearn.data.model.GrammarStructureDto
import com.example.luminalearn.data.model.JourneyUnitDto
import com.example.luminalearn.data.model.LessonDto
import com.example.luminalearn.data.model.VocabularyDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

/**
 * Trình quản lý bộ nhớ đệm Offline-First (Offline Cache Manager).
 * Lưu trữ danh sách bài học, chi tiết bài học, ngữ pháp, từ vựng và chặng Unit dưới dạng JSON trong Internal Storage.
 * Giúp app hoạt động mượt mà ngay cả khi không có mạng Internet hoặc khi máy chủ bận.
 */
object LessonCacheManager {

    private val gson = Gson()
    private var appContext: Context? = null

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    private fun getFile(filename: String): File? {
        val ctx = appContext ?: return null
        return File(ctx.filesDir, filename)
    }

    fun cacheLessons(lessons: List<LessonDto>) {
        try {
            val file = getFile("cached_lessons.json") ?: return
            file.writeText(gson.toJson(lessons))
        } catch (_: Exception) {}
    }

    fun getCachedLessons(): List<LessonDto>? {
        return try {
            val file = getFile("cached_lessons.json") ?: return null
            if (!file.exists()) return null
            val json = file.readText()
            if (json.isBlank()) return null
            val type = object : TypeToken<List<LessonDto>>() {}.type
            gson.fromJson(json, type)
        } catch (_: Exception) {
            null
        }
    }

    fun cacheRecommendedLessons(lessons: List<LessonDto>) {
        try {
            val file = getFile("cached_recommended_lessons.json") ?: return
            file.writeText(gson.toJson(lessons))
        } catch (_: Exception) {}
    }

    fun getCachedRecommendedLessons(): List<LessonDto>? {
        return try {
            val file = getFile("cached_recommended_lessons.json") ?: return null
            if (!file.exists()) return null
            val json = file.readText()
            if (json.isBlank()) return null
            val type = object : TypeToken<List<LessonDto>>() {}.type
            gson.fromJson(json, type)
        } catch (_: Exception) {
            null
        }
    }

    fun cacheLessonDetail(lesson: LessonDto) {
        try {
            val file = getFile("cached_lesson_${lesson.id}.json") ?: return
            file.writeText(gson.toJson(lesson))
        } catch (_: Exception) {}
    }

    fun getCachedLessonDetail(id: String): LessonDto? {
        return try {
            val file = getFile("cached_lesson_${id}.json") ?: return null
            if (!file.exists()) return null
            val json = file.readText()
            if (json.isBlank()) return null
            gson.fromJson(json, LessonDto::class.java)
        } catch (_: Exception) {
            null
        }
    }

    fun cacheUnits(units: List<JourneyUnitDto>) {
        try {
            val file = getFile("cached_units.json") ?: return
            file.writeText(gson.toJson(units))
        } catch (_: Exception) {}
    }

    fun getCachedUnits(): List<JourneyUnitDto>? {
        return try {
            val file = getFile("cached_units.json") ?: return null
            if (!file.exists()) return null
            val json = file.readText()
            if (json.isBlank()) return null
            val type = object : TypeToken<List<JourneyUnitDto>>() {}.type
            gson.fromJson(json, type)
        } catch (_: Exception) {
            null
        }
    }

    fun cacheGrammars(grammars: List<GrammarStructureDto>) {
        try {
            val file = getFile("cached_grammars.json") ?: return
            file.writeText(gson.toJson(grammars))
        } catch (_: Exception) {}
    }

    fun getCachedGrammars(): List<GrammarStructureDto>? {
        return try {
            val file = getFile("cached_grammars.json") ?: return null
            if (!file.exists()) return null
            val json = file.readText()
            if (json.isBlank()) return null
            val type = object : TypeToken<List<GrammarStructureDto>>() {}.type
            gson.fromJson(json, type)
        } catch (_: Exception) {
            null
        }
    }

    fun cacheVocabularies(vocabs: List<VocabularyDto>) {
        try {
            val file = getFile("cached_vocabularies.json") ?: return
            file.writeText(gson.toJson(vocabs))
        } catch (_: Exception) {}
    }

    fun getCachedVocabularies(): List<VocabularyDto>? {
        return try {
            val file = getFile("cached_vocabularies.json") ?: return null
            if (!file.exists()) return null
            val json = file.readText()
            if (json.isBlank()) return null
            val type = object : TypeToken<List<VocabularyDto>>() {}.type
            gson.fromJson(json, type)
        } catch (_: Exception) {
            null
        }
    }
}
