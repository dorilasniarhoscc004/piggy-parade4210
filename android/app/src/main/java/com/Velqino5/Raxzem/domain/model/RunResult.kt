package com.Velqino5.Raxzem.domain.model

data class RunResult(
    val score: Int,
    val stage: Int,
    val stagesCleared: Int,
    val coinsSorted: Int,
    val bestStreak: Int,
    val charmId: String?,
    val routeCleared: Boolean
)
