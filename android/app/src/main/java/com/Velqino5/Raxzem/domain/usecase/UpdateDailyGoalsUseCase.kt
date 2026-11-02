package com.Velqino5.Raxzem.domain.usecase

import com.Velqino5.Raxzem.domain.model.DailyGoal
import com.Velqino5.Raxzem.domain.model.GoalIds
import com.Velqino5.Raxzem.domain.model.RunResult

class UpdateDailyGoalsUseCase {

    operator fun invoke(goals: List<DailyGoal>, result: RunResult): List<DailyGoal> = goals.map { goal ->
        val next = when (goal.id) {
            GoalIds.COINS -> goal.progress + result.coinsSorted
            GoalIds.STAGES -> goal.progress + result.stagesCleared
            GoalIds.STREAK -> maxOf(goal.progress, result.bestStreak)
            else -> goal.progress
        }
        goal.copy(progress = next.coerceAtMost(goal.target))
    }
}
