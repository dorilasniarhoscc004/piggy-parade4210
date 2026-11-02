package com.Velqino5.Raxzem.data.sample

import com.Velqino5.Raxzem.core.config.GameConfig
import com.Velqino5.Raxzem.domain.model.Charm
import com.Velqino5.Raxzem.domain.model.DailyGoal
import com.Velqino5.Raxzem.domain.model.GoalIds
import com.Velqino5.Raxzem.domain.model.Stage

object SampleData {

    val charmIds: List<String> = listOf(
        "brass_bell",
        "ribbon_knot",
        "acorn_pin",
        "candle_cup",
        "honey_drop",
        "paper_lantern",
        "clover_tag",
        "copper_key",
        "sugar_star",
        "wood_whistle",
        "amber_bead",
        "gold_crown"
    )

    val charms: List<Charm> = charmIds.mapIndexed { index, id -> Charm(id, index + 1) }

    val stages: List<Stage> = (1..GameConfig.STAGES).map { index ->
        Stage(
            index = index,
            stepMs = maxOf(
                GameConfig.STEP_MS_MIN,
                GameConfig.STEP_MS_BASE - (index - 1) * GameConfig.STEP_MS_DECAY
            ),
            charmId = charmIds[(index - 1).coerceIn(charmIds.indices)]
        )
    }

    val goalTemplates: List<DailyGoal> = listOf(
        DailyGoal(GoalIds.COINS, target = 40, progress = 0, reward = 100, claimed = false),
        DailyGoal(GoalIds.STAGES, target = 2, progress = 0, reward = 150, claimed = false),
        DailyGoal(GoalIds.STREAK, target = 8, progress = 0, reward = 200, claimed = false)
    )

    fun stepMsFor(stage: Int): Long =
        stages.firstOrNull { it.index == stage }?.stepMs ?: GameConfig.STEP_MS_MIN

    fun charmIdFor(stage: Int): String =
        stages.firstOrNull { it.index == stage }?.charmId ?: charmIds.last()
}
