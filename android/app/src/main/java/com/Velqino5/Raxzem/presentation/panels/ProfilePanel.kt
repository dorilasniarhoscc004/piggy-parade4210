package com.Velqino5.Raxzem.presentation.panels

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.Velqino5.Raxzem.R
import com.Velqino5.Raxzem.core.theme.PiggyAccent
import com.Velqino5.Raxzem.core.theme.PiggyBorder
import com.Velqino5.Raxzem.core.theme.PiggyDanger
import com.Velqino5.Raxzem.core.theme.PiggyLeaf
import com.Velqino5.Raxzem.core.theme.PiggyOnPrimary
import com.Velqino5.Raxzem.core.theme.PiggyPrimary
import com.Velqino5.Raxzem.core.theme.PiggySurfaceRaised
import com.Velqino5.Raxzem.core.theme.PiggyTextPrimary
import com.Velqino5.Raxzem.core.theme.PiggyTextSecondary
import com.Velqino5.Raxzem.core.ui.GlassPanelSurface
import com.Velqino5.Raxzem.core.ui.PanelScaffold
import com.Velqino5.Raxzem.core.ui.SectionHeader
import com.Velqino5.Raxzem.core.ui.StatCard
import com.Velqino5.Raxzem.presentation.menu.MenuUiState

@Composable
fun ProfilePanel(
    state: MenuUiState,
    onToggleSound: () -> Unit,
    onToggleHaptics: () -> Unit,
    onToggleReducedMotion: () -> Unit,
    onKeeperName: (String) -> Unit,
    onAskReset: () -> Unit,
    onClose: () -> Unit
) {
    val defaultName = stringResource(R.string.default_keeper)
    val onLabel = stringResource(R.string.state_on)
    val offLabel = stringResource(R.string.state_off)
    PanelScaffold(
        title = stringResource(R.string.panel_profile),
        onClose = onClose
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            GlassPanelSurface(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(
                            if (state.charmCount > 0) R.drawable.sprite_5 else R.drawable.logo
                        ),
                        contentDescription = stringResource(R.string.cd_charm),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    OutlinedTextField(
                        value = state.keeperName.ifBlank { defaultName },
                        onValueChange = onKeeperName,
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        label = {
                            Text(
                                text = stringResource(R.string.keeper_name),
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PiggyAccent,
                            unfocusedBorderColor = PiggyBorder.copy(alpha = 0.4f),
                            focusedTextColor = PiggyTextPrimary,
                            unfocusedTextColor = PiggyTextPrimary,
                            focusedLabelColor = PiggyAccent,
                            unfocusedLabelColor = PiggyTextSecondary,
                            cursorColor = PiggyAccent
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            SectionHeader(title = stringResource(R.string.section_lifetime))

            LifetimeStats(state = state)

            SectionHeader(title = stringResource(R.string.section_settings))

            GlassPanelSurface(modifier = Modifier.fillMaxWidth()) {
                SettingRow(
                    label = stringResource(R.string.setting_sound),
                    checked = state.settings.sound,
                    onLabel = onLabel,
                    offLabel = offLabel,
                    onToggle = onToggleSound
                )
                SettingRow(
                    label = stringResource(R.string.setting_haptics),
                    checked = state.settings.haptics,
                    onLabel = onLabel,
                    offLabel = offLabel,
                    onToggle = onToggleHaptics
                )
                SettingRow(
                    label = stringResource(R.string.setting_reduced_motion),
                    checked = state.settings.reducedMotion,
                    onLabel = onLabel,
                    offLabel = offLabel,
                    onToggle = onToggleReducedMotion
                )
            }

            TextButton(
                onClick = onAskReset,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp)
            ) {
                Text(
                    text = stringResource(R.string.reset_progress),
                    style = MaterialTheme.typography.labelLarge,
                    color = PiggyDanger
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun LifetimeStats(state: MenuUiState) {
    val showCoins = state.lifetimeCoins > 0
    val showRuns = state.runs > 0
    val showStreak = state.bestStreak > 0
    val showDays = state.dayStreak > 0
    val visible = listOf(showCoins, showRuns, showStreak, showDays).count { it }
    if (visible < 2) {
        Text(
            text = stringResource(R.string.menu_hint),
            style = MaterialTheme.typography.bodyMedium,
            color = PiggyTextSecondary
        )
        return
    }
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        if (showCoins) {
            StatCard(
                label = stringResource(R.string.stat_coins),
                value = state.lifetimeCoins.toString(),
                accent = PiggyAccent,
                modifier = Modifier.weight(1f)
            )
        }
        if (showRuns) {
            StatCard(
                label = stringResource(R.string.stat_runs),
                value = state.runs.toString(),
                accent = PiggyPrimary,
                modifier = Modifier.weight(1f)
            )
        }
        if (showStreak) {
            StatCard(
                label = stringResource(R.string.stat_streak),
                value = state.bestStreak.toString(),
                accent = PiggyLeaf,
                modifier = Modifier.weight(1f)
            )
        }
        if (showDays) {
            StatCard(
                label = stringResource(R.string.stat_days),
                value = state.dayStreak.toString(),
                accent = PiggyTextSecondary,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun SettingRow(
    label: String,
    checked: Boolean,
    onLabel: String,
    offLabel: String,
    onToggle: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = PiggyTextPrimary
        )
        Switch(
            checked = checked,
            onCheckedChange = { onToggle() },
            colors = SwitchDefaults.colors(
                checkedThumbColor = PiggyOnPrimary,
                checkedTrackColor = PiggyAccent,
                uncheckedThumbColor = PiggyTextSecondary,
                uncheckedTrackColor = PiggySurfaceRaised
            ),
            modifier = Modifier.semantics {
                stateDescription = if (checked) onLabel else offLabel
            }
        )
    }
}
