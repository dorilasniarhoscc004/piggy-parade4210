package com.Velqino5.Raxzem.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.Velqino5.Raxzem.R
import com.Velqino5.Raxzem.core.theme.PiggyAccent
import com.Velqino5.Raxzem.core.theme.PiggyLeaf
import com.Velqino5.Raxzem.core.theme.PiggyPrimary
import com.Velqino5.Raxzem.domain.model.DailyGoal
import com.Velqino5.Raxzem.domain.model.GoalIds
import com.Velqino5.Raxzem.domain.model.PotType

fun potAccent(type: PotType): Color = when (type) {
    PotType.GOLD -> PiggyAccent
    PotType.CLAY -> PiggyPrimary
    PotType.LEAF -> PiggyLeaf
}

@Composable
fun potArt(type: PotType): Painter = when (type) {
    PotType.GOLD -> painterResource(R.drawable.sprite_1)
    PotType.CLAY -> painterResource(R.drawable.sprite_2)
    PotType.LEAF -> painterResource(R.drawable.sprite_3)
}

@Composable
fun potLabel(type: PotType): String = when (type) {
    PotType.GOLD -> stringResource(R.string.pot_gold)
    PotType.CLAY -> stringResource(R.string.pot_clay)
    PotType.LEAF -> stringResource(R.string.pot_leaf)
}

@Composable
fun potDescription(type: PotType): String = when (type) {
    PotType.GOLD -> stringResource(R.string.cd_pot_gold)
    PotType.CLAY -> stringResource(R.string.cd_pot_clay)
    PotType.LEAF -> stringResource(R.string.cd_pot_leaf)
}

@Composable
fun charmName(id: String): String = when (id) {
    "brass_bell" -> stringResource(R.string.charm_brass_bell)
    "ribbon_knot" -> stringResource(R.string.charm_ribbon_knot)
    "acorn_pin" -> stringResource(R.string.charm_acorn_pin)
    "candle_cup" -> stringResource(R.string.charm_candle_cup)
    "honey_drop" -> stringResource(R.string.charm_honey_drop)
    "paper_lantern" -> stringResource(R.string.charm_paper_lantern)
    "clover_tag" -> stringResource(R.string.charm_clover_tag)
    "copper_key" -> stringResource(R.string.charm_copper_key)
    "sugar_star" -> stringResource(R.string.charm_sugar_star)
    "wood_whistle" -> stringResource(R.string.charm_wood_whistle)
    "amber_bead" -> stringResource(R.string.charm_amber_bead)
    else -> stringResource(R.string.charm_gold_crown)
}

@Composable
fun goalTitle(goal: DailyGoal): String = when (goal.id) {
    GoalIds.COINS -> stringResource(R.string.goal_coins, goal.target)
    GoalIds.STAGES -> stringResource(R.string.goal_stages, goal.target)
    else -> stringResource(R.string.goal_streak, goal.target)
}

fun charmTint(index: Int): Color = when (index % 3) {
    0 -> PiggyAccent
    1 -> PiggyPrimary
    else -> PiggyLeaf
}
