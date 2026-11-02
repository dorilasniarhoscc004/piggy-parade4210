package com.Velqino5.Raxzem.presentation.panels

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.Velqino5.Raxzem.R
import com.Velqino5.Raxzem.core.config.GameConfig
import com.Velqino5.Raxzem.core.theme.PiggyAccent
import com.Velqino5.Raxzem.core.theme.PiggyOnPrimary
import com.Velqino5.Raxzem.core.theme.PiggyPrimary
import com.Velqino5.Raxzem.core.theme.PiggySurfaceRaised
import com.Velqino5.Raxzem.core.theme.PiggyTextPrimary
import com.Velqino5.Raxzem.core.theme.PiggyTextSecondary
import com.Velqino5.Raxzem.core.ui.GlassPanelSurface
import com.Velqino5.Raxzem.core.ui.PanelScaffold
import com.Velqino5.Raxzem.core.ui.SectionHeader
import com.Velqino5.Raxzem.core.ui.charmName
import com.Velqino5.Raxzem.data.sample.SampleData
import com.Velqino5.Raxzem.domain.model.Stage

@Composable
fun ParadeMapPanel(highestStage: Int, onClose: () -> Unit) {
    PanelScaffold(
        title = stringResource(R.string.panel_parade_map),
        onClose = onClose
    ) {
        SectionHeader(
            title = stringResource(R.string.stat_stage),
            trailing = stringResource(R.string.value_fraction, highestStage, GameConfig.STAGES)
        )
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(SampleData.stages, key = { stage -> stage.index }) { stage ->
                StageNode(stage = stage, highestStage = highestStage)
            }
        }
    }
}

@Composable
private fun StageNode(stage: Stage, highestStage: Int) {
    val cleared = stage.index <= highestStage
    val current = stage.index == highestStage + 1
    val nodeColor = when {
        cleared -> PiggyAccent
        current -> PiggyPrimary
        else -> PiggySurfaceRaised
    }
    val alignEnd = stage.index % 2 == 0
    Row(
        horizontalArrangement = if (alignEnd) Arrangement.End else Arrangement.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (alignEnd) {
            Spacer(modifier = Modifier.width(28.dp))
        }
        GlassPanelSurface(
            modifier = Modifier
                .weight(1f)
                .alpha(if (cleared || current) 1f else 0.45f)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(nodeColor)
                ) {
                    Text(
                        text = stage.index.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        color = if (cleared || current) PiggyOnPrimary else PiggyTextSecondary
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = charmName(stage.charmId),
                        style = MaterialTheme.typography.titleMedium,
                        color = PiggyTextPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = stringResource(R.string.stage_speed, stage.stepMs.toInt()),
                        style = MaterialTheme.typography.labelSmall,
                        color = PiggyTextSecondary
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = when {
                        cleared -> stringResource(R.string.stage_done)
                        current -> stringResource(R.string.stage_current)
                        else -> stringResource(R.string.stage_locked)
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = if (cleared) PiggyAccent else PiggyTextSecondary
                )
            }
        }
        if (!alignEnd) {
            Spacer(modifier = Modifier.width(28.dp))
        }
    }
}
