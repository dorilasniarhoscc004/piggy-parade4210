package com.Velqino5.Raxzem.domain.usecase

import com.Velqino5.Raxzem.core.config.GameConfig
import com.Velqino5.Raxzem.domain.model.CoinOutcome
import com.Velqino5.Raxzem.domain.model.CoinResolution
import com.Velqino5.Raxzem.domain.model.PotType

class ResolveCoinUseCase {

    operator fun invoke(coin: PotType, tapped: PotType?, streak: Int, lives: Int): CoinResolution {
        if (tapped == coin) {
            val gain = GameConfig.BASE_POINTS +
                minOf(streak, GameConfig.STREAK_CAP) * GameConfig.STREAK_STEP
            return CoinResolution(CoinOutcome.CORRECT, gain, streak + 1, lives)
        }
        val outcome = if (tapped == null) CoinOutcome.MISSED else CoinOutcome.WRONG
        return CoinResolution(outcome, 0, 0, (lives - 1).coerceAtLeast(0))
    }
}
