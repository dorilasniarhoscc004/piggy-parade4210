package com.Velqino5.Raxzem.presentation.gameover

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Velqino5.Raxzem.R
import com.Velqino5.Raxzem.core.config.GameConfig
import com.Velqino5.Raxzem.core.di.AppViewModelFactory
import com.Velqino5.Raxzem.core.navigation.Screen
import com.Velqino5.Raxzem.core.theme.PiggyAccent
import com.Velqino5.Raxzem.core.theme.PiggyLeaf
import com.Velqino5.Raxzem.core.theme.PiggyOnPrimary
import com.Velqino5.Raxzem.core.theme.PiggyPrimary
import com.Velqino5.Raxzem.core.theme.PiggyTextPrimary
import com.Velqino5.Raxzem.core.theme.PiggyTextSecondary
import com.Velqino5.Raxzem.core.theme.ResultOverlay
import com.Velqino5.Raxzem.core.ui.GlassPanelSurface
import com.Velqino5.Raxzem.core.ui.PrimaryButton
import com.Velqino5.Raxzem.core.ui.ScreenBackground
import com.Velqino5.Raxzem.core.ui.SecondaryButton
import com.Velqino5.Raxzem.core.ui.StatCard
import com.Velqino5.Raxzem.core.ui.charmName

@Composable
fun GameOverRoute(
    destination: Screen.GameOver,
    onAgain: () -> Unit,
    onMenu: () -> Unit
) {
    val gameOverViewModel: GameOverViewModel = viewModel(factory = AppViewModelFactory)
    val state by gameOverViewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(destination) {
        gameOverViewModel.present(destination)
    }
    GameOverScreen(state = state, onAgain = onAgain, onMenu = onMenu)
}

@Composable
fun GameOverScreen(
    state: GameOverUiState,
    onAgain: () -> Unit,
    onMenu: () -> Unit
) {
    var entered by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        entered = true
    }
    val countUp by animateIntAsState(
        targetValue = if (entered) state.score else 0,
        animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing)
    )
    val charmScale by animateFloatAsState(
        targetValue = if (entered) 1f else 0.8f,
        animationSpec = tween(durationMillis = 480, easing = FastOutSlowInEasing)
    )

    ScreenBackground(overlay = ResultOverlay) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(320.dp)
                .background(
                    Brush.radialGradient(
                        listOf(PiggyAccent.copy(alpha = 0.18f), Color.Transparent)
                    ),
                    CircleShape
                )
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.weight(0.16f))

            Text(
                text = if (state.routeCleared) {
                    stringResource(R.string.stage_cleared)
                } else {
                    stringResource(R.string.parade_over)
                },
                style = MaterialTheme.typography.displaySmall,
                color = PiggyTextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (state.charmId != null) {
                    stringResource(R.string.charm_unlocked)
                } else {
                    stringResource(R.string.coins_sorted, state.coinsSorted)
                },
                style = MaterialTheme.typography.bodyLarge,
                color = PiggyTextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (state.newBest) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = PiggyAccent
                ) {
                    Text(
                        text = stringResource(R.string.new_best),
                        style = MaterialTheme.typography.labelSmall,
                        color = PiggyOnPrimary,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            GlassPanelSurface(
                shape = RoundedCornerShape(22.dp),
                borderColor = PiggyAccent.copy(alpha = 0.35f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    StatCard(
                        label = stringResource(R.string.stat_score),
                        value = countUp.toString(),
                        accent = PiggyAccent,
                        announce = true,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        label = stringResource(R.string.stat_stage),
                        value = stringResource(
                            R.string.value_fraction,
                            state.stage,
                            GameConfig.STAGES
                        ),
                        accent = PiggyPrimary,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    StatCard(
                        label = stringResource(R.string.stat_best),
                        value = state.best.toString(),
                        accent = PiggyLeaf,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        label = stringResource(R.string.stat_coins),
                        value = state.coinsSorted.toString(),
                        accent = PiggyTextSecondary,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            if (state.charmId != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Image(
                    painter = painterResource(R.drawable.sprite_5),
                    contentDescription = stringResource(R.string.cd_charm),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(86.dp)
                        .scale(charmScale)
                        .alpha(if (entered) 1f else 0f)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = charmName(state.charmId),
                    style = MaterialTheme.typography.titleMedium,
                    color = PiggyAccent,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(
                        R.string.charm_on_shelf,
                        state.charmNumber,
                        state.charmTotal
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = PiggyTextSecondary,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.weight(0.3f))

            PrimaryButton(
                text = stringResource(R.string.sort_again),
                onClick = onAgain
            )

            Spacer(modifier = Modifier.height(12.dp))

            SecondaryButton(
                text = stringResource(R.string.menu),
                iconPainter = painterResource(R.drawable.icon_home),
                onClick = onMenu
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
