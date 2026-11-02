package com.Velqino5.Raxzem.presentation.gameover

import com.Velqino5.Raxzem.core.config.GameConfig

data class GameOverUiState(
    val score: Int = 0,
    val stage: Int = 1,
    val coinsSorted: Int = 0,
    val bestStreak: Int = 0,
    val best: Int = 0,
    val newBest: Boolean = false,
    val routeCleared: Boolean = false,
    val charmId: String? = null,
    val charmNumber: Int = 0,
    val charmTotal: Int = GameConfig.STAGES
)
