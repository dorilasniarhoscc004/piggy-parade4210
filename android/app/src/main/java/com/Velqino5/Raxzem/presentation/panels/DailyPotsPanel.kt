package com.Velqino5.Raxzem.presentation.panels

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.Velqino5.Raxzem.R
import com.Velqino5.Raxzem.core.theme.PiggyAccent
import com.Velqino5.Raxzem.core.theme.PiggyLeaf
import com.Velqino5.Raxzem.core.theme.PiggyTextMuted
import com.Velqino5.Raxzem.core.theme.PiggyTextPrimary
import com.Velqino5.Raxzem.core.theme.PiggyTextSecondary
import com.Velqino5.Raxzem.core.ui.GlassPanelSurface
import com.Velqino5.Raxzem.core.ui.PanelScaffold
import com.Velqino5.Raxzem.core.ui.ProgressTrack
import com.Velqino5.Raxzem.core.ui.SecondaryButton
import com.Velqino5.Raxzem.core.ui.SectionHeader
import com.Velqino5.Raxzem.core.ui.goalTitle
import com.Velqino5.Raxzem.domain.model.DailyGoal

@Composable
fun DailyPotsPanel(
    goals: List<DailyGoal>,
    onClaim: (String) -> Unit,
    onClose: () -> Unit
) {
    PanelScaffold(
        title = stringResource(R.string.panel_daily_pots),
        onClose = onClose
    ) {
        SectionHeader(
            title = stringResource(R.string.goals_reset),
            trailing = stringResource(
                R.string.value_fraction,
                goals.count { it.claimed },
                goals.size.coerceAtLeast(1)
            )
        )
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            goals.forEach { goal ->
                GoalCard(goal = goal, onClaim = onClaim)
            }
        }
    }
}

@Composable
private fun GoalCard(goal: DailyGoal, onClaim: (String) -> Unit) {
    val claimedLabel = stringResource(R.string.claimed)
    GlassPanelSurface(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = goalTitle(goal),
                    style = MaterialTheme.typography.titleMedium,
                    color = PiggyTextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.goal_reward, goal.reward),
                    style = MaterialTheme.typography.labelSmall,
                    color = PiggyTextMuted
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = stringResource(R.string.value_fraction, goal.progress, goal.target),
                style = MaterialTheme.typography.titleMedium,
                color = if (goal.completed) PiggyLeaf else PiggyTextSecondary
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        ProgressTrack(
            filled = (goal.ratio * 10).toInt(),
            total = 10,
            accent = if (goal.completed) PiggyLeaf else PiggyAccent
        )
        Spacer(modifier = Modifier.height(12.dp))
        SecondaryButton(
            text = if (goal.claimed) claimedLabel else stringResource(R.string.claim),
            enabled = goal.completed && !goal.claimed,
            height = 48.dp,
            onClick = { onClaim(goal.id) },
            modifier = Modifier.semantics {
                stateDescription = claimedLabel
            }
        )
    }
}
