package com.Velqino5.Raxzem.domain.repository

import com.Velqino5.Raxzem.domain.model.AppSettings

interface SettingsRepository {
    fun load(): AppSettings
    fun save(settings: AppSettings)
}
