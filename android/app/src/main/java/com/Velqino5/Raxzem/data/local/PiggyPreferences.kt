package com.Velqino5.Raxzem.data.local

import android.content.Context
import android.content.SharedPreferences

class PiggyPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(STORE, Context.MODE_PRIVATE)

    fun int(key: String, fallback: Int = 0): Int = prefs.getInt(key, fallback)

    fun string(key: String, fallback: String = ""): String = prefs.getString(key, fallback) ?: fallback

    fun bool(key: String, fallback: Boolean): Boolean = prefs.getBoolean(key, fallback)

    fun putInt(key: String, value: Int) = prefs.edit().putInt(key, value).apply()

    fun putString(key: String, value: String) = prefs.edit().putString(key, value).apply()

    fun putBool(key: String, value: Boolean) = prefs.edit().putBoolean(key, value).apply()

    fun clearKeys(keys: List<String>) {
        val editor = prefs.edit()
        keys.forEach { editor.remove(it) }
        editor.apply()
    }

    companion object {
        private const val STORE = "piggy_parade_prefs"

        const val KEY_BEST_SCORE = "best_score"
        const val KEY_HIGHEST_STAGE = "highest_stage"
        const val KEY_CHARMS = "charms"
        const val KEY_LIFETIME_COINS = "lifetime_coins"
        const val KEY_LIFETIME_POINTS = "lifetime_points"
        const val KEY_RUNS = "runs"
        const val KEY_BEST_STREAK = "best_streak"
        const val KEY_DAY_STREAK = "day_streak"
        const val KEY_LAST_PLAYED = "last_played"
        const val KEY_KEEPER = "keeper_name"
        const val KEY_SOUND = "sound_cues"
        const val KEY_HAPTICS = "haptics"
        const val KEY_REDUCED_MOTION = "reduced_motion"
        const val KEY_GOALS_DATE = "goals_date"
        const val KEY_GOALS = "goals_state"

        val ALL_KEYS = listOf(
            KEY_BEST_SCORE,
            KEY_HIGHEST_STAGE,
            KEY_CHARMS,
            KEY_LIFETIME_COINS,
            KEY_LIFETIME_POINTS,
            KEY_RUNS,
            KEY_BEST_STREAK,
            KEY_DAY_STREAK,
            KEY_LAST_PLAYED,
            KEY_KEEPER,
            KEY_GOALS_DATE,
            KEY_GOALS
        )
    }
}
