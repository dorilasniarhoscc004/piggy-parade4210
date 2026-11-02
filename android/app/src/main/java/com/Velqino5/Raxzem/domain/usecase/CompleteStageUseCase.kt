package com.Velqino5.Raxzem.domain.usecase

import com.Velqino5.Raxzem.core.config.GameConfig
import com.Velqino5.Raxzem.domain.model.StageReward

class CompleteStageUseCase {

    operator fun invoke(charmId: String, flawless: Boolean, alreadyUnlocked: List<String>): StageReward {
        val bonus = if (flawless) GameConfig.PERFECT_STAGE_BONUS else 0
        val reward = if (alreadyUnlocked.contains(charmId)) null else charmId
        return StageReward(bonus, reward)
    }
}
