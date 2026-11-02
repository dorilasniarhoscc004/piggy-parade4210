package com.Velqino5.Raxzem.domain.model

object GoalIds {
    const val COINS = "sort_coins"
    const val STAGES = "clear_stages"
    const val STREAK = "reach_streak"
}

data class DailyGoal(
    val id: String,
    val target: Int,
    val progress: Int,
    val reward: Int,
    val claimed: Boolean
) {
    val completed: Boolean get() = progress >= target
    val ratio: Float get() = if (target <= 0) 0f else (progress.toFloat() / target).coerceIn(0f, 1f)
}
