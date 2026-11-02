package com.Velqino5.Raxzem.presentation.gameover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Velqino5.Raxzem.core.navigation.Screen
import com.Velqino5.Raxzem.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameOverViewModel(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GameOverUiState())
    val state: StateFlow<GameOverUiState> = _state.asStateFlow()

    fun present(destination: Screen.GameOver) {
        viewModelScope.launch {
            val progress = progressRepository.load()
            _state.update {
                it.copy(
                    score = destination.score,
                    stage = destination.stage,
                    coinsSorted = destination.coinsSorted,
                    bestStreak = destination.bestStreak,
                    best = progress.bestScore,
                    newBest = destination.newBest,
                    routeCleared = destination.routeCleared,
                    charmId = destination.charmId,
                    charmNumber = progress.charms.size
                )
            }
        }
    }
}
