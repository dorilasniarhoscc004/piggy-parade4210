package com.Velqino5.Raxzem.domain.repository

import com.Velqino5.Raxzem.domain.model.PlayerProgress
import com.Velqino5.Raxzem.domain.model.RunResult

interface ProgressRepository {
    fun load(): PlayerProgress
    fun recordRun(result: RunResult): PlayerProgress
    fun unlockCharm(charmId: String)
    fun addPoints(points: Int)
    fun setKeeperName(name: String)
    fun reset()
}
