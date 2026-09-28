package com.example.luminalearn.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.luminalearn.presentation.vocabulary.model.SrsRating
import com.example.luminalearn.presentation.vocabulary.model.SrsScheduler
import com.example.luminalearn.presentation.vocabulary.model.SrsWordState

class SrsManager(context: Context) {

    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "lumina_srs_prefs"
        private const val KEY_PREFIX_REP = "rep_"
        private const val KEY_PREFIX_INTERVAL = "interval_"
        private const val KEY_PREFIX_EASE = "ease_"
        private const val KEY_PREFIX_NEXT = "next_"
        private const val KEY_PREFIX_LAST = "last_"
    }

    fun getSrsState(wordId: String): SrsWordState {
        val rep = prefs.getInt("$KEY_PREFIX_REP$wordId", 0)
        val interval = prefs.getInt("$KEY_PREFIX_INTERVAL$wordId", 0)
        val ease = prefs.getFloat("$KEY_PREFIX_EASE$wordId", 2.5f)
        val next = prefs.getLong("$KEY_PREFIX_NEXT$wordId", 0L)
        val last = prefs.getLong("$KEY_PREFIX_LAST$wordId", 0L)
        return SrsWordState(
            wordId = wordId,
            repetition = rep,
            intervalDays = interval,
            easeFactor = ease,
            nextReviewTimeMillis = next,
            lastReviewTimeMillis = last
        )
    }

    fun saveSrsState(state: SrsWordState) {
        prefs.edit {
            putInt("$KEY_PREFIX_REP${state.wordId}", state.repetition)
            putInt("$KEY_PREFIX_INTERVAL${state.wordId}", state.intervalDays)
            putFloat("$KEY_PREFIX_EASE${state.wordId}", state.easeFactor)
            putLong("$KEY_PREFIX_NEXT${state.wordId}", state.nextReviewTimeMillis)
            putLong("$KEY_PREFIX_LAST${state.wordId}", state.lastReviewTimeMillis)
        }
    }

    fun rateWord(wordId: String, rating: SrsRating): SrsWordState {
        val current = getSrsState(wordId)
        val next = SrsScheduler.calculateNextState(current, rating)
        saveSrsState(next)
        return next
    }

    fun syncWithBackend(
        wordId: String,
        beRep: Int,
        beInterval: Int,
        beEase: Float,
        beNextReview: Long,
        beLastReview: Long
    ): SrsWordState {
        val local = getSrsState(wordId)
        if (beLastReview > local.lastReviewTimeMillis) {
            val synced = SrsWordState(
                wordId = wordId,
                repetition = beRep,
                intervalDays = beInterval,
                easeFactor = beEase,
                nextReviewTimeMillis = beNextReview,
                lastReviewTimeMillis = beLastReview
            )
            saveSrsState(synced)
            return synced
        }
        return local
    }
}
