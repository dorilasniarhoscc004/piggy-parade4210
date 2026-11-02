package com.Velqino5.Raxzem.domain.repository

import com.Velqino5.Raxzem.domain.model.DailyGoal

interface DailyGoalsRepository {
    fun load(): List<DailyGoal>
    fun save(goals: List<DailyGoal>)
    fun reset()
}
