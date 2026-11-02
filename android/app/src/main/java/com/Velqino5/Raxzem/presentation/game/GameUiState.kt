package com.Velqino5.Raxzem.presentation.game

import com.Velqino5.Raxzem.core.config.GameConfig
import com.Velqino5.Raxzem.domain.model.CoinOutcome
import com.Velqino5.Raxzem.domain.model.PotType
import com.Velqino5.Raxzem.domain.model.RunResult

enum class GamePhase { READY, DROPPING, RESOLVING, STAGE_CLEARED, FINISHED }

data class GameUiState(
    val phase: GamePhase = GamePhase.READY,
    val stage: Int = 1,
    val coinNumber: Int = 1,
    val coinsInStage: Int = 0,
    val coinsSorted: Int = 0,
    val coinType: PotType? = null,
    val coinStep: Int = 0,
    val score: Int = 0,
    val streak: Int = 0,
    val bestStreak: Int = 0,
    val best: Int = 0,
    val lives: Int = GameConfig.START_LIVES,
    val outcome: CoinOutcome? = null,
    val correctPot: PotType? = null,
    val tappedPot: PotType? = null,
    val lastGain: Int = 0,
    val charmId: String? = null,
    val paused: Boolean = false,
    val reducedMotion: Boolean = false
) {
    val inputEnabled: Boolean get() = phase == GamePhase.DROPPING && !paused

    val dropVisible: Boolean
        get() = coinType != null && (phase == GamePhase.DROPPING || phase == GamePhase.RESOLVING)
}

data class GameFinished(val result: RunResult, val newBest: Boolean)
