package com.Velqino5.Raxzem.data.repository

import com.Velqino5.Raxzem.data.local.PiggyPreferences
import com.Velqino5.Raxzem.domain.model.AppSettings
import com.Velqino5.Raxzem.domain.repository.SettingsRepository

class SettingsRepositoryImpl(private val prefs: PiggyPreferences) : SettingsRepository {

    override fun load(): AppSettings = AppSettings(
        sound = prefs.bool(PiggyPreferences.KEY_SOUND, true),
        haptics = prefs.bool(PiggyPreferences.KEY_HAPTICS, true),
        reducedMotion = prefs.bool(PiggyPreferences.KEY_REDUCED_MOTION, false)
    )

    override fun save(settings: AppSettings) {
        prefs.putBool(PiggyPreferences.KEY_SOUND, settings.sound)
        prefs.putBool(PiggyPreferences.KEY_HAPTICS, settings.haptics)
        prefs.putBool(PiggyPreferences.KEY_REDUCED_MOTION, settings.reducedMotion)
    }
}
