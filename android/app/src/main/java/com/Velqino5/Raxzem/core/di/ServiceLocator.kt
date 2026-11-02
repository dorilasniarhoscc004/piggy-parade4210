package com.Velqino5.Raxzem.core.di

import android.content.Context
import com.Velqino5.Raxzem.data.local.PiggyPreferences
import com.Velqino5.Raxzem.data.repository.DailyGoalsRepositoryImpl
import com.Velqino5.Raxzem.data.repository.ProgressRepositoryImpl
import com.Velqino5.Raxzem.data.repository.SettingsRepositoryImpl
import com.Velqino5.Raxzem.domain.repository.DailyGoalsRepository
import com.Velqino5.Raxzem.domain.repository.ProgressRepository
import com.Velqino5.Raxzem.domain.repository.SettingsRepository
import com.Velqino5.Raxzem.domain.usecase.CompleteStageUseCase
import com.Velqino5.Raxzem.domain.usecase.NextCoinUseCase
import com.Velqino5.Raxzem.domain.usecase.ResolveCoinUseCase
import com.Velqino5.Raxzem.domain.usecase.UpdateDailyGoalsUseCase

object ServiceLocator {

    private var preferences: PiggyPreferences? = null

    fun init(context: Context) {
        if (preferences == null) {
            preferences = PiggyPreferences(context.applicationContext)
        }
    }

    private fun prefs(): PiggyPreferences = requireNotNull(preferences)

    val progressRepository: ProgressRepository by lazy { ProgressRepositoryImpl(prefs()) }

    val settingsRepository: SettingsRepository by lazy { SettingsRepositoryImpl(prefs()) }

    val dailyGoalsRepository: DailyGoalsRepository by lazy { DailyGoalsRepositoryImpl(prefs()) }

    val nextCoin: NextCoinUseCase by lazy { NextCoinUseCase() }

    val resolveCoin: ResolveCoinUseCase by lazy { ResolveCoinUseCase() }

    val completeStage: CompleteStageUseCase by lazy { CompleteStageUseCase() }

    val updateDailyGoals: UpdateDailyGoalsUseCase by lazy { UpdateDailyGoalsUseCase() }
}
