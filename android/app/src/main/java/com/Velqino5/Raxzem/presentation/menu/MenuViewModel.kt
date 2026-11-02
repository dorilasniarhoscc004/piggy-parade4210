package com.Velqino5.Raxzem.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Velqino5.Raxzem.domain.repository.DailyGoalsRepository
import com.Velqino5.Raxzem.domain.repository.ProgressRepository
import com.Velqino5.Raxzem.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MenuViewModel(
    private val progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository,
    private val dailyGoalsRepository: DailyGoalsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = _state.asStateFlow()

    init {
        reload()
    }

    fun refresh() {
        reload()
    }

    fun openPanel(panel: PanelId) {
        _state.update { it.copy(openPanel = panel) }
    }

    fun closePanel() {
        _state.update { it.copy(openPanel = null, confirmReset = false) }
    }

    fun claimGoal(goalId: String) {
        viewModelScope.launch {
            val goals = dailyGoalsRepository.load()
            val target = goals.firstOrNull { it.id == goalId } ?: return@launch
            if (!target.completed || target.claimed) return@launch
            val updated = goals.map { goal ->
                if (goal.id == goalId) goal.copy(claimed = true) else goal
            }
            dailyGoalsRepository.save(updated)
            progressRepository.addPoints(target.reward)
            reload()
        }
    }

    fun toggleSound() {
        viewModelScope.launch {
            val current = settingsRepository.load()
            settingsRepository.save(current.copy(sound = !current.sound))
            reload()
        }
    }

    fun toggleHaptics() {
        viewModelScope.launch {
            val current = settingsRepository.load()
            settingsRepository.save(current.copy(haptics = !current.haptics))
            reload()
        }
    }

    fun toggleReducedMotion() {
        viewModelScope.launch {
            val current = settingsRepository.load()
            settingsRepository.save(current.copy(reducedMotion = !current.reducedMotion))
            reload()
        }
    }

    fun setKeeperName(name: String) {
        viewModelScope.launch {
            progressRepository.setKeeperName(name)
            _state.update { it.copy(keeperName = name.take(18)) }
        }
    }

    fun askReset() {
        _state.update { it.copy(confirmReset = true) }
    }

    fun dismissReset() {
        _state.update { it.copy(confirmReset = false) }
    }

    fun confirmReset() {
        viewModelScope.launch {
            progressRepository.reset()
            dailyGoalsRepository.reset()
            _state.update { it.copy(confirmReset = false) }
            reload()
        }
    }

    private fun reload() {
        viewModelScope.launch {
            val progress = progressRepository.load()
            val goals = dailyGoalsRepository.load()
            val settings = settingsRepository.load()
            _state.update { current ->
                current.copy(
                    best = progress.bestScore,
                    highestStage = progress.highestStage,
                    charms = progress.charms,
                    goals = goals,
                    settings = settings,
                    keeperName = progress.keeperName,
                    lifetimeCoins = progress.lifetimeCoins,
                    lifetimePoints = progress.lifetimePoints,
                    runs = progress.runs,
                    bestStreak = progress.bestStreak,
                    dayStreak = progress.dayStreak
                )
            }
        }
    }
}
