package com.Velqino5.Raxzem.presentation.game

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Velqino5.Raxzem.R
import com.Velqino5.Raxzem.core.config.GameConfig
import com.Velqino5.Raxzem.core.di.AppViewModelFactory
import com.Velqino5.Raxzem.core.theme.GameOverlay
import com.Velqino5.Raxzem.core.theme.PiggyAccent
import com.Velqino5.Raxzem.core.theme.PiggyBgTop
import com.Velqino5.Raxzem.core.theme.PiggyBorder
import com.Velqino5.Raxzem.core.theme.PiggyDanger
import com.Velqino5.Raxzem.core.theme.PiggyLeaf
import com.Velqino5.Raxzem.core.theme.PiggyPrimary
import com.Velqino5.Raxzem.core.theme.PiggySurfaceRaised
import com.Velqino5.Raxzem.core.theme.PiggyTextMuted
import com.Velqino5.Raxzem.core.theme.PiggyTextPrimary
import com.Velqino5.Raxzem.core.theme.PiggyTextSecondary
import com.Velqino5.Raxzem.core.ui.ProgressTrack
import com.Velqino5.Raxzem.core.ui.PotTile
import com.Velqino5.Raxzem.core.ui.ScreenBackground
import com.Velqino5.Raxzem.core.ui.StatCard
import com.Velqino5.Raxzem.core.ui.potAccent
import com.Velqino5.Raxzem.core.ui.potArt
import com.Velqino5.Raxzem.core.ui.potDescription
import com.Velqino5.Raxzem.core.ui.potLabel
import com.Velqino5.Raxzem.domain.model.CoinOutcome
import com.Velqino5.Raxzem.domain.model.PotType
import com.Velqino5.Raxzem.presentation.dialog.PausedDialog

@Composable
fun GameRoute(runId: Int, onGameOver: (GameFinished) -> Unit) {
    val gameViewModel: GameViewModel = viewModel(factory = AppViewModelFactory)
    val state by gameViewModel.state.collectAsStateWithLifecycle()
    DisposableEffect(runId) {
        gameViewModel.enterRun(runId)
        onDispose { gameViewModel.leaveRun() }
    }
    LaunchedEffect(Unit) {
        gameViewModel.events.collect { outcome -> onGameOver(outcome) }
    }
    GameScreen(
        state = state,
        onPotTap = gameViewModel::onPotTap,
        onPause = gameViewModel::pause,
        onResume = gameViewModel::resume,
        onQuit = gameViewModel::quitRound
    )
}

@Composable
fun GameScreen(
    state: GameUiState,
    onPotTap: (PotType) -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onQuit: () -> Unit
) {
    val stepAnim = if (state.reducedMotion) {
        GameConfig.STEP_ANIM_REDUCED_MS
    } else {
        GameConfig.STEP_ANIM_MS
    }
    val missFlash by animateFloatAsState(
        targetValue = if (state.phase == GamePhase.RESOLVING &&
            state.outcome != null &&
            state.outcome != CoinOutcome.CORRECT
        ) {
            0.3f
        } else {
            0f
        },
        animationSpec = tween(durationMillis = 220)
    )

    ScreenBackground(overlay = GameOverlay) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PiggyDanger.copy(alpha = missFlash))
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            GameHeader(state = state, onPause = onPause)

            DropLane(
                state = state,
                stepAnim = stepAnim,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            GameStatStrip(
                state = state,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            PotDeck(
                state = state,
                onPotTap = onPotTap,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            StageProgress(
                state = state,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
        if (state.paused) {
            PausedDialog(onResume = onResume, onQuit = onQuit)
        }
    }
}

@Composable
private fun GameHeader(state: GameUiState, onPause: () -> Unit) {
    val livesLabel = stringResource(R.string.cd_lives)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(PiggyBgTop.copy(alpha = 0.55f))
    ) {
        IconButton(
            onClick = onPause,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 10.dp)
                .size(48.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.icon_pause),
                contentDescription = stringResource(R.string.cd_pause),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(28.dp)
            )
        }
        Text(
            text = stringResource(
                R.string.game_header,
                state.stage,
                state.coinNumber,
                GameConfig.COINS_PER_STAGE
            ),
            style = MaterialTheme.typography.titleMedium,
            color = PiggyTextPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center)
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp)
                .semantics {
                    contentDescription = livesLabel
                    liveRegion = LiveRegionMode.Polite
                }
        ) {
            repeat(GameConfig.START_LIVES) { index ->
                Image(
                    painter = painterResource(R.drawable.sprite_5),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(20.dp)
                        .alpha(if (index < state.lives) 1f else 0.22f)
                )
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(1.dp)
                .background(PiggyBorder.copy(alpha = 0.22f))
        )
    }
}

@Composable
private fun DropLane(state: GameUiState, stepAnim: Int, modifier: Modifier) {
    BoxWithConstraints(modifier = modifier) {
        val laneHeight = maxHeight
        val travel = (laneHeight - 196.dp).coerceAtLeast(60.dp)
        val ratio = state.coinStep.toFloat() / GameConfig.DROP_STEPS
        val drop by animateDpAsState(
            targetValue = travel * ratio,
            animationSpec = tween(durationMillis = stepAnim, easing = FastOutSlowInEasing)
        )

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp)
        ) {
            repeat(3) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .fillMaxHeight()
                        .background(PiggyTextPrimary.copy(alpha = 0.08f))
                )
            }
        }

        Image(
            painter = painterResource(R.drawable.sprite_4),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 2.dp)
                .size(46.dp)
                .alpha(0.4f)
        )

        if (state.dropVisible && state.coinType != null) {
            FallingCoin(
                type = state.coinType,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = 58.dp + drop)
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 28.dp)
                .height(1.dp)
                .background(PiggyTextPrimary.copy(alpha = 0.12f))
        )

        if (state.phase == GamePhase.READY) {
            LaneBanner(text = stringResource(R.string.get_ready))
        }
        if (state.phase == GamePhase.STAGE_CLEARED) {
            StageClearedBanner(state = state)
        }
    }
}

@Composable
private fun FallingCoin(type: PotType, modifier: Modifier) {
    val accent = potAccent(type)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(76.dp)
                .clip(CircleShape)
                .background(accent.copy(alpha = 0.16f))
                .border(3.dp, accent, CircleShape)
        ) {
            Image(
                painter = painterResource(R.drawable.sprite_4),
                contentDescription = stringResource(R.string.cd_coin),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(56.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = accent.copy(alpha = 0.18f),
            border = BorderStroke(1.dp, accent.copy(alpha = 0.6f))
        ) {
            Text(
                text = potLabel(type),
                style = MaterialTheme.typography.labelSmall,
                color = accent,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
            )
        }
    }
}

@Composable
private fun LaneBanner(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = PiggySurfaceRaised.copy(alpha = 0.92f),
            border = BorderStroke(1.dp, PiggyAccent.copy(alpha = 0.5f)),
            shadowElevation = 12.dp
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.headlineMedium,
                color = PiggyTextPrimary,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 14.dp)
            )
        }
    }
}

@Composable
private fun StageClearedBanner(state: GameUiState) {
    var shown by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        shown = true
    }
    val scale by animateFloatAsState(
        targetValue = if (shown) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.sprite_5),
            contentDescription = stringResource(R.string.cd_charm),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(96.dp)
                .scale(scale)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(R.string.stage_cleared_banner, state.stage),
            style = MaterialTheme.typography.headlineMedium,
            color = PiggyAccent,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun GameStatStrip(state: GameUiState, modifier: Modifier) {
    val showScore = state.score > 0
    val showStreak = state.streak > 0
    val showBest = state.best > 0
    val visible = listOf(showScore, showStreak, showBest).count { it }
    if (visible < 2) {
        Box(
            modifier = modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = PiggySurfaceRaised.copy(alpha = 0.7f),
                border = BorderStroke(1.dp, PiggyBorder.copy(alpha = 0.22f))
            ) {
                Text(
                    text = stringResource(R.string.menu_hint),
                    style = MaterialTheme.typography.labelSmall,
                    color = PiggyTextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                )
            }
        }
        return
    }
    val animatedScore by animateIntAsState(
        targetValue = state.score,
        animationSpec = tween(durationMillis = 220)
    )
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        if (showScore) {
            StatCard(
                label = stringResource(R.string.stat_score),
                value = animatedScore.toString(),
                accent = PiggyAccent,
                announce = true,
                modifier = Modifier.weight(1f)
            )
        }
        if (showStreak) {
            StatCard(
                label = stringResource(R.string.stat_streak),
                value = state.streak.toString(),
                accent = PiggyLeaf,
                modifier = Modifier.weight(1f)
            )
        }
        if (showBest) {
            StatCard(
                label = stringResource(R.string.stat_best),
                value = state.best.toString(),
                accent = PiggyPrimary,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun PotDeck(state: GameUiState, onPotTap: (PotType) -> Unit, modifier: Modifier) {
    val readyState = stringResource(R.string.state_pot_ready)
    val correctState = stringResource(R.string.state_pot_correct)
    val missedState = stringResource(R.string.state_pot_missed)
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        PotType.entries.forEach { type ->
            val revealed = state.phase == GamePhase.RESOLVING && state.correctPot == type
            val wrongPick = state.phase == GamePhase.RESOLVING &&
                state.tappedPot == type &&
                state.outcome == CoinOutcome.WRONG
            val pop by animateFloatAsState(
                targetValue = if (revealed && state.outcome == CoinOutcome.CORRECT) 1.06f else 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            PotTile(
                art = potArt(type),
                label = potLabel(type),
                accent = if (wrongPick) PiggyDanger else potAccent(type),
                description = potDescription(type),
                state = when {
                    wrongPick -> missedState
                    revealed -> correctState
                    else -> readyState
                },
                artSize = 96.dp,
                enabled = state.inputEnabled,
                highlighted = revealed,
                dimmed = !state.inputEnabled,
                popScale = pop,
                modifier = Modifier
                    .weight(1f)
                    .height(150.dp),
                onClick = { onPotTap(type) }
            )
        }
    }
}

@Composable
private fun StageProgress(state: GameUiState, modifier: Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        ProgressTrack(
            filled = state.coinsInStage,
            total = GameConfig.COINS_PER_STAGE,
            accent = PiggyAccent
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = stringResource(R.string.stage_of, state.stage, GameConfig.STAGES),
            style = MaterialTheme.typography.labelSmall,
            color = PiggyTextMuted,
            textAlign = TextAlign.Center
        )
    }
}
