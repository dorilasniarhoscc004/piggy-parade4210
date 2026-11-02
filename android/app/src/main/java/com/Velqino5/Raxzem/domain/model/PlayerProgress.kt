package com.Velqino5.Raxzem.domain.model

data class PlayerProgress(
    val bestScore: Int = 0,
    val highestStage: Int = 0,
    val charms: List<CharmUnlock> = emptyList(),
    val lifetimeCoins: Int = 0,
    val lifetimePoints: Int = 0,
    val runs: Int = 0,
    val bestStreak: Int = 0,
    val dayStreak: Int = 0,
    val lastPlayedDate: String = "",
    val keeperName: String = ""
)
