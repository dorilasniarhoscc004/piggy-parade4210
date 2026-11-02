package com.Velqino5.Raxzem.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.Velqino5.Raxzem.presentation.app.AppViewModel
import com.Velqino5.Raxzem.presentation.game.GameViewModel
import com.Velqino5.Raxzem.presentation.gameover.GameOverViewModel
import com.Velqino5.Raxzem.presentation.menu.MenuViewModel
import com.Velqino5.Raxzem.presentation.splash.SplashViewModel

object AppViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val created: ViewModel = when {
            modelClass.isAssignableFrom(AppViewModel::class.java) -> AppViewModel()
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> SplashViewModel()
            modelClass.isAssignableFrom(MenuViewModel::class.java) -> MenuViewModel(
                ServiceLocator.progressRepository,
                ServiceLocator.settingsRepository,
                ServiceLocator.dailyGoalsRepository
            )
            modelClass.isAssignableFrom(GameViewModel::class.java) -> GameViewModel(
                ServiceLocator.progressRepository,
                ServiceLocator.settingsRepository,
                ServiceLocator.dailyGoalsRepository,
                ServiceLocator.nextCoin,
                ServiceLocator.resolveCoin,
                ServiceLocator.completeStage,
                ServiceLocator.updateDailyGoals
            )
            modelClass.isAssignableFrom(GameOverViewModel::class.java) -> GameOverViewModel(
                ServiceLocator.progressRepository
            )
            else -> throw IllegalArgumentException(modelClass.name)
        }
        return created as T
    }
}
