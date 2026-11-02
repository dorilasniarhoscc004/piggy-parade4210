package com.Velqino5.Raxzem.presentation.panels

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.Velqino5.Raxzem.R
import com.Velqino5.Raxzem.core.config.GameConfig
import com.Velqino5.Raxzem.core.theme.PiggyTextMuted
import com.Velqino5.Raxzem.core.theme.PiggyTextPrimary
import com.Velqino5.Raxzem.core.ui.GlassPanelSurface
import com.Velqino5.Raxzem.core.ui.PanelScaffold
import com.Velqino5.Raxzem.core.ui.SectionHeader
import com.Velqino5.Raxzem.core.ui.charmName
import com.Velqino5.Raxzem.core.ui.charmTint
import com.Velqino5.Raxzem.data.sample.SampleData
import com.Velqino5.Raxzem.domain.model.Charm
import com.Velqino5.Raxzem.domain.model.CharmUnlock

@Composable
fun CharmShelfPanel(charms: List<CharmUnlock>, onClose: () -> Unit) {
    PanelScaffold(
        title = stringResource(R.string.panel_charm_shelf),
        onClose = onClose
    ) {
        SectionHeader(
            title = stringResource(R.string.charms_counter, charms.size, GameConfig.STAGES)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(SampleData.charms, key = { charm -> charm.id }) { charm ->
                CharmCard(
                    charm = charm,
                    unlock = charms.firstOrNull { it.id == charm.id }
                )
            }
        }
    }
}

@Composable
private fun CharmCard(charm: Charm, unlock: CharmUnlock?) {
    val unlocked = unlock != null
    val lockedLabel = stringResource(R.string.state_charm_locked)
    val unlockedLabel = stringResource(R.string.state_charm_unlocked)
    val lockedHint = stringResource(R.string.charm_locked_hint, charm.stageIndex)
    GlassPanelSurface(
        borderColor = charmTint(charm.stageIndex).copy(alpha = if (unlocked) 0.5f else 0.15f),
        modifier = Modifier
            .fillMaxWidth()
            .semantics {
                stateDescription = if (unlocked) unlockedLabel else lockedLabel
            }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(R.drawable.sprite_5),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(52.dp)
                    .alpha(if (unlocked) 1f else 0.3f)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = charmName(charm.id),
                style = MaterialTheme.typography.labelSmall,
                color = if (unlocked) PiggyTextPrimary else PiggyTextMuted,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = unlock?.date ?: lockedHint,
                style = MaterialTheme.typography.labelSmall,
                color = PiggyTextMuted,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}
