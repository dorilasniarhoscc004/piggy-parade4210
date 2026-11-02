package com.Velqino5.Raxzem.data.repository

import com.Velqino5.Raxzem.data.local.CalendarClock
import com.Velqino5.Raxzem.data.local.PiggyPreferences
import com.Velqino5.Raxzem.data.sample.SampleData
import com.Velqino5.Raxzem.domain.model.DailyGoal
import com.Velqino5.Raxzem.domain.repository.DailyGoalsRepository

class DailyGoalsRepositoryImpl(private val prefs: PiggyPreferences) : DailyGoalsRepository {

    override fun load(): List<DailyGoal> {
        val today = CalendarClock.today()
        if (prefs.string(PiggyPreferences.KEY_GOALS_DATE) != today) {
            val fresh = SampleData.goalTemplates
            prefs.putString(PiggyPreferences.KEY_GOALS_DATE, today)
            prefs.putString(PiggyPreferences.KEY_GOALS, encode(fresh))
            return fresh
        }
        val stored = decode(prefs.string(PiggyPreferences.KEY_GOALS))
        return if (stored.isEmpty()) SampleData.goalTemplates else stored
    }

    override fun save(goals: List<DailyGoal>) {
        prefs.putString(PiggyPreferences.KEY_GOALS_DATE, CalendarClock.today())
        prefs.putString(PiggyPreferences.KEY_GOALS, encode(goals))
    }

    override fun reset() {
        prefs.putString(PiggyPreferences.KEY_GOALS_DATE, "")
        prefs.putString(PiggyPreferences.KEY_GOALS, "")
    }

    private fun encode(goals: List<DailyGoal>): String = goals.joinToString(ENTRY) { goal ->
        listOf(goal.id, goal.target, goal.progress, goal.reward, goal.claimed).joinToString(FIELD)
    }

    private fun decode(raw: String): List<DailyGoal> {
        if (raw.isBlank()) return emptyList()
        return raw.split(ENTRY).mapNotNull { entry ->
            val parts = entry.split(FIELD)
            if (parts.size != 5) return@mapNotNull null
            val template = SampleData.goalTemplates.firstOrNull { it.id == parts[0] }
                ?: return@mapNotNull null
            template.copy(
                target = parts[1].toIntOrNull() ?: template.target,
                progress = parts[2].toIntOrNull() ?: 0,
                reward = parts[3].toIntOrNull() ?: template.reward,
                claimed = parts[4].toBooleanStrictOrNull() ?: false
            )
        }
    }

    private companion object {
        const val ENTRY = "|"
        const val FIELD = ";"
    }
}
