package com.Velqino5.Raxzem.core.navigation

sealed interface Screen {

    data object Splash : Screen

    data object Menu : Screen

    data class Game(val runId: Int) : Screen

    data class GameOver(
        val score: Int,
        val stage: Int,
        val coinsSorted: Int,
        val bestStreak: Int,
        val charmId: String?,
        val routeCleared: Boolean,
        val newBest: Boolean
    ) : Screen
}
