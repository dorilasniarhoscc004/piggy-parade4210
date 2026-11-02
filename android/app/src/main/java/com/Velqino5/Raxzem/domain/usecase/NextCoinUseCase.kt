package com.Velqino5.Raxzem.domain.usecase

import com.Velqino5.Raxzem.core.config.GameConfig
import com.Velqino5.Raxzem.domain.model.PotType
import kotlin.random.Random

class NextCoinUseCase(private val random: Random = Random.Default) {

    operator fun invoke(recent: List<PotType>): PotType {
        val tail = recent.takeLast(GameConfig.MAX_SAME_TYPE_RUN)
        val blocked = if (tail.size == GameConfig.MAX_SAME_TYPE_RUN && tail.distinct().size == 1) {
            tail.first()
        } else {
            null
        }
        val options = PotType.entries.filter { it != blocked }
        return options[random.nextInt(options.size)]
    }
}
