package com.Velqino5.Raxzem.domain.model

enum class CoinOutcome { CORRECT, WRONG, MISSED }

data class CoinResolution(
    val outcome: CoinOutcome,
    val scoreDelta: Int,
    val streak: Int,
    val lives: Int
)

data class StageReward(val bonus: Int, val charmId: String?)
