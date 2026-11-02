package com.Velqino5.Raxzem.presentation.menu

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Velqino5.Raxzem.R
import com.Velqino5.Raxzem.core.config.GameConfig
import com.Velqino5.Raxzem.core.di.AppViewModelFactory
import com.Velqino5.Raxzem.core.theme.MenuOverlay
import com.Velqino5.Raxzem.core.theme.PiggyAccent
import com.Velqino5.Raxzem.core.theme.PiggyBorder
import com.Velqino5.Raxzem.core.theme.PiggyLeaf
import com.Velqino5.Raxzem.core.theme.PiggyPrimary
import com.Velqino5.Raxzem.core.theme.PiggySurface
import com.Velqino5.Raxzem.core.theme.PiggyTextMuted
import com.Velqino5.Raxzem.core.theme.PiggyTextPrimary
import com.Velqino5.Raxzem.core.theme.PiggyTextSecondary
import com.Velqino5.Raxzem.core.ui.PrimaryButton
import com.Velqino5.Raxzem.core.ui.ScreenBackground
import com.Velqino5.Raxzem.core.ui.StatCard
import com.Velqino5.Raxzem.core.ui.TileButton
import com.Velqino5.Raxzem.presentation.dialog.ConfirmResetDialog
import com.Velqino5.Raxzem.presentation.panels.CharmShelfPanel
import com.Velqino5.Raxzem.presentation.panels.DailyPotsPanel
import com.Velqino5.Raxzem.presentation.panels.HowItWorksPanel
import com.Velqino5.Raxzem.presentation.panels.ParadeMapPanel
import com.Velqino5.Raxzem.presentation.panels.ProfilePanel

@Composable
fun MenuRoute(onPlay: () -> Unit) {
    val menuViewModel: MenuViewModel = viewModel(factory = AppViewModelFactory)
    val state by menuViewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        menuViewModel.refresh()
    }
    MenuScreen(
        state = state,
        onPlay = onPlay,
        onOpenPanel = menuViewModel::openPanel,
        onClosePanel = menuViewModel::closePanel,
        onClaimGoal = menuViewModel::claimGoal,
        onToggleSound = menuViewModel::toggleSound,
        onToggleHaptics = menuViewModel::toggleHaptics,
        onToggleReducedMotion = menuViewModel::toggleReducedMotion,
        onKeeperName = menuViewModel::setKeeperName,
        onAskReset = menuViewModel::askReset,
        onDismissReset = menuViewModel::dismissReset,
        onConfirmReset = menuViewModel::confirmReset
    )
}

@Composable
fun MenuScreen(
    state: MenuUiState,
    onPlay: () -> Unit,
    onOpenPanel: (PanelId) -> Unit,
    onClosePanel: () -> Unit,
    onClaimGoal: (String) -> Unit,
    onToggleSound: () -> Unit,
    onToggleHaptics: () -> Unit,
    onToggleReducedMotion: () -> Unit,
    onKeeperName: (String) -> Unit,
    onAskReset: () -> Unit,
    onDismissReset: () -> Unit,
    onConfirmReset: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        MenuContent(
            state = state,
            onPlay = onPlay,
            onOpenPanel = onOpenPanel
        )
        when (state.openPanel) {
            PanelId.HOW_IT_WORKS -> HowItWorksPanel(onClose = onClosePanel)
            PanelId.PARADE_MAP -> ParadeMapPanel(
                highestStage = state.highestStage,
                onClose = onClosePanel
            )
            PanelId.DAILY_POTS -> DailyPotsPanel(
                goals = state.goals,
                onClaim = onClaimGoal,
                onClose = onClosePanel
            )
            PanelId.CHARM_SHELF -> CharmShelfPanel(
                charms = state.charms,
                onClose = onClosePanel
            )
            PanelId.PROFILE -> ProfilePanel(
                state = state,
                onToggleSound = onToggleSound,
                onToggleHaptics = onToggleHaptics,
                onToggleReducedMotion = onToggleReducedMotion,
                onKeeperName = onKeeperName,
                onAskReset = onAskReset,
                onClose = onClosePanel
            )
            null -> Unit
        }
        if (state.confirmReset) {
            ConfirmResetDialog(
                onConfirm = onConfirmReset,
                onDismiss = onDismissReset
            )
        }
    }
}

@Composable
private fun MenuContent(
    state: MenuUiState,
    onPlay: () -> Unit,
    onOpenPanel: (PanelId) -> Unit
) {
    var entered by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        entered = true
    }
    val sheetShift by animateDpAsState(
        targetValue = if (entered) 0.dp else 64.dp,
        animationSpec = tween(durationMillis = 420, easing = FastOutSlowInEasing)
    )
    val fade by animateFloatAsState(
        targetValue = if (entered) 1f else 0f,
        animationSpec = tween(durationMillis = 420, easing = FastOutSlowInEasing)
    )
    val ctaScale by animateFloatAsState(
        targetValue = if (entered) 1f else 0.9f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    val heroLift by animateDpAsState(
        targetValue = if (entered) 0.dp else 10.dp,
        animationSpec = tween(durationMillis = 820, easing = FastOutSlowInEasing)
    )

    ScreenBackground(overlay = MenuOverlay) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                IconButton(
                    onClick = { onOpenPanel(PanelId.PROFILE) },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 12.dp, top = 8.dp)
                        .size(48.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_settings),
                        contentDescription = stringResource(R.string.cd_settings),
                        tint = PiggyAccent,
                        modifier = Modifier.size(26.dp)
                    )
                }
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = stringResource(R.string.cd_logo),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 6.dp)
                        .size(126.dp)
                        .alpha(fade)
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .size(260.dp)
                        .background(
                            Brush.radialGradient(
                                listOf(PiggyAccent.copy(alpha = 0.22f), Color.Transparent)
                            ),
                            CircleShape
                        )
                )
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 6.dp)
                        .alpha(fade)
                ) {
                    Image(
                        painter = painterResource(R.drawable.sprite_2),
                        contentDescription = stringResource(R.string.cd_pot_clay),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(104.dp)
                            .offset(y = 18.dp)
                            .alpha(0.9f)
                    )
                    Image(
                        painter = painterResource(R.drawable.sprite_1),
                        contentDescription = stringResource(R.string.cd_pot_gold),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(170.dp)
                            .offset(y = heroLift)
                    )
                    Image(
                        painter = painterResource(R.drawable.sprite_3),
                        contentDescription = stringResource(R.string.cd_pot_leaf),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(104.dp)
                            .offset(y = 18.dp)
                            .alpha(0.9f)
                    )
                }
            }

            MenuSheet(
                state = state,
                fade = fade,
                ctaScale = ctaScale,
                shift = sheetShift,
                onPlay = onPlay,
                onOpenPanel = onOpenPanel
            )
        }
    }
}

@Composable
private fun MenuSheet(
    state: MenuUiState,
    fade: Float,
    ctaScale: Float,
    shift: Dp,
    onPlay: () -> Unit,
    onOpenPanel: (PanelId) -> Unit
) {
    val shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    Surface(
        shape = shape,
        color = PiggySurface.copy(alpha = 0.96f),
        border = BorderStroke(1.dp, PiggyBorder.copy(alpha = 0.35f)),
        shadowElevation = 18.dp,
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = shift)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(PiggyTextPrimary.copy(alpha = 0.07f), Color.Transparent)
                    )
                )
                .padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(width = 44.dp, height = 5.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(PiggyAccent.copy(alpha = 0.5f))
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = stringResource(R.string.app_title),
                style = MaterialTheme.typography.headlineMedium,
                color = PiggyTextPrimary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = stringResource(R.string.menu_tagline),
                style = MaterialTheme.typography.bodyMedium,
                color = PiggyTextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(fade)
            ) {
                TileButton(
                    label = stringResource(R.string.tile_parade_map),
                    icon = painterResource(R.drawable.ic_map),
                    modifier = Modifier.weight(1f),
                    onClick = { onOpenPanel(PanelId.PARADE_MAP) }
                )
                TileButton(
                    label = stringResource(R.string.tile_daily_pots),
                    icon = painterResource(R.drawable.ic_calendar),
                    badge = state.unclaimedGoal,
                    modifier = Modifier.weight(1f),
                    onClick = { onOpenPanel(PanelId.DAILY_POTS) }
                )
                TileButton(
                    label = stringResource(R.string.tile_charm_shelf),
                    icon = painterResource(R.drawable.ic_star),
                    modifier = Modifier.weight(1f),
                    onClick = { onOpenPanel(PanelId.CHARM_SHELF) }
                )
                TileButton(
                    label = stringResource(R.string.tile_how_it_works),
                    icon = painterResource(R.drawable.ic_info),
                    modifier = Modifier.weight(1f),
                    onClick = { onOpenPanel(PanelId.HOW_IT_WORKS) }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = stringResource(R.string.menu_hint),
                style = MaterialTheme.typography.bodyMedium,
                color = PiggyTextMuted,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            PrimaryButton(
                text = stringResource(R.string.play),
                icon = painterResource(R.drawable.ic_play),
                onClick = onPlay,
                modifier = Modifier.scale(ctaScale)
            )

            Spacer(modifier = Modifier.height(16.dp))

            MenuStatStrip(state = state)

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.disclaimer),
                style = MaterialTheme.typography.labelSmall,
                color = PiggyTextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun MenuStatStrip(state: MenuUiState) {
    val showBest = state.best > 0
    val showStage = state.highestStage > 0
    val showCharms = state.charmCount > 0
    val visible = listOf(showBest, showStage, showCharms).count { it }
    if (visible < 2) {
        Spacer(modifier = Modifier.height(66.dp))
        return
    }
    val stageValue = stringResource(R.string.value_fraction, state.highestStage, GameConfig.STAGES)
    val charmValue = stringResource(R.string.value_fraction, state.charmCount, GameConfig.STAGES)
    val bestValue = state.best.toString()
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        if (showBest) {
            StatCard(
                label = stringResource(R.string.stat_best),
                value = bestValue,
                accent = PiggyAccent,
                modifier = Modifier.weight(1f)
            )
        }
        if (showStage) {
            StatCard(
                label = stringResource(R.string.stat_stage),
                value = stageValue,
                accent = PiggyPrimary,
                modifier = Modifier.weight(1f)
            )
        }
        if (showCharms) {
            StatCard(
                label = stringResource(R.string.stat_charms),
                value = charmValue,
                accent = PiggyLeaf,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
