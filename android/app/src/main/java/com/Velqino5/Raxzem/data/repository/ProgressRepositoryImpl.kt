package com.Velqino5.Raxzem.data.repository

import com.Velqino5.Raxzem.data.local.CalendarClock
import com.Velqino5.Raxzem.data.local.PiggyPreferences
import com.Velqino5.Raxzem.domain.model.CharmUnlock
import com.Velqino5.Raxzem.domain.model.PlayerProgress
import com.Velqino5.Raxzem.domain.model.RunResult
import com.Velqino5.Raxzem.domain.repository.ProgressRepository

class ProgressRepositoryImpl(private val prefs: PiggyPreferences) : ProgressRepository {

    override fun load(): PlayerProgress = PlayerProgress(
        bestScore = prefs.int(PiggyPreferences.KEY_BEST_SCORE),
        highestStage = prefs.int(PiggyPreferences.KEY_HIGHEST_STAGE),
        charms = decodeCharms(prefs.string(PiggyPreferences.KEY_CHARMS)),
        lifetimeCoins = prefs.int(PiggyPreferences.KEY_LIFETIME_COINS),
        lifetimePoints = prefs.int(PiggyPreferences.KEY_LIFETIME_POINTS),
        runs = prefs.int(PiggyPreferences.KEY_RUNS),
        bestStreak = prefs.int(PiggyPreferences.KEY_BEST_STREAK),
        dayStreak = prefs.int(PiggyPreferences.KEY_DAY_STREAK),
        lastPlayedDate = prefs.string(PiggyPreferences.KEY_LAST_PLAYED),
        keeperName = prefs.string(PiggyPreferences.KEY_KEEPER)
    )

    override fun recordRun(result: RunResult): PlayerProgress {
        val current = load()
        val today = CalendarClock.today()
        val dayStreak = when (current.lastPlayedDate) {
            today -> maxOf(current.dayStreak, 1)
            CalendarClock.yesterday() -> current.dayStreak + 1
            else -> 1
        }
        prefs.putInt(PiggyPreferences.KEY_BEST_SCORE, maxOf(current.bestScore, result.score))
        prefs.putInt(PiggyPreferences.KEY_HIGHEST_STAGE, maxOf(current.highestStage, result.stage))
        prefs.putInt(PiggyPreferences.KEY_LIFETIME_COINS, current.lifetimeCoins + result.coinsSorted)
        prefs.putInt(PiggyPreferences.KEY_LIFETIME_POINTS, current.lifetimePoints + result.score)
        prefs.putInt(PiggyPreferences.KEY_RUNS, current.runs + 1)
        prefs.putInt(PiggyPreferences.KEY_BEST_STREAK, maxOf(current.bestStreak, result.bestStreak))
        prefs.putInt(PiggyPreferences.KEY_DAY_STREAK, dayStreak)
        prefs.putString(PiggyPreferences.KEY_LAST_PLAYED, today)
        return load()
    }

    override fun unlockCharm(charmId: String) {
        val current = decodeCharms(prefs.string(PiggyPreferences.KEY_CHARMS))
        if (current.any { it.id == charmId }) return
        val updated = current + CharmUnlock(charmId, CalendarClock.today())
        prefs.putString(PiggyPreferences.KEY_CHARMS, encodeCharms(updated))
    }

    override fun addPoints(points: Int) {
        prefs.putInt(
            PiggyPreferences.KEY_LIFETIME_POINTS,
            prefs.int(PiggyPreferences.KEY_LIFETIME_POINTS) + points
        )
    }

    override fun setKeeperName(name: String) {
        prefs.putString(PiggyPreferences.KEY_KEEPER, name.take(18))
    }

    override fun reset() {
        prefs.clearKeys(PiggyPreferences.ALL_KEYS)
    }

    private fun decodeCharms(raw: String): List<CharmUnlock> {
        if (raw.isBlank()) return emptyList()
        return raw.split(ENTRY_SEPARATOR).mapNotNull { entry ->
            val parts = entry.split(FIELD_SEPARATOR)
            if (parts.size == 2 && parts[0].isNotBlank()) CharmUnlock(parts[0], parts[1]) else null
        }
    }

    private fun encodeCharms(items: List<CharmUnlock>): String =
        items.joinToString(ENTRY_SEPARATOR) { it.id + FIELD_SEPARATOR + it.date }

    private companion object {
        const val ENTRY_SEPARATOR = "|"
        const val FIELD_SEPARATOR = ":"
    }
}
