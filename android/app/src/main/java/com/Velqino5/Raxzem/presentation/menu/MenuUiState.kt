package com.Velqino5.Raxzem.presentation.menu

import com.Velqino5.Raxzem.domain.model.AppSettings
import com.Velqino5.Raxzem.domain.model.CharmUnlock
import com.Velqino5.Raxzem.domain.model.DailyGoal

enum class PanelId { PARADE_MAP, DAILY_POTS, CHARM_SHELF, HOW_IT_WORKS, PROFILE }

data class MenuUiState(
    val best: Int = 0,
    val highestStage: Int = 0,
    val charms: List<CharmUnlock> = emptyList(),
    val goals: List<DailyGoal> = emptyList(),
    val settings: AppSettings = AppSettings(),
    val keeperName: String = "",
    val lifetimeCoins: Int = 0,
    val lifetimePoints: Int = 0,
    val runs: Int = 0,
    val bestStreak: Int = 0,
    val dayStreak: Int = 0,
    val openPanel: PanelId? = null,
    val confirmReset: Boolean = false
) {
    val charmCount: Int get() = charms.size

    val unclaimedGoal: Boolean get() = goals.any { it.completed && !it.claimed }
}
