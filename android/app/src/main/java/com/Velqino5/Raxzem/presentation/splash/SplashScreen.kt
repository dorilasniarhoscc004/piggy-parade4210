package com.Velqino5.Raxzem.presentation.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Velqino5.Raxzem.R
import com.Velqino5.Raxzem.core.di.AppViewModelFactory
import com.Velqino5.Raxzem.core.theme.PiggyAccent
import com.Velqino5.Raxzem.core.theme.PiggyPrimary
import com.Velqino5.Raxzem.core.theme.PiggyTextMuted
import com.Velqino5.Raxzem.core.theme.PiggyTextPrimary
import com.Velqino5.Raxzem.core.theme.PiggyTextSecondary
import com.Velqino5.Raxzem.core.theme.SplashOverlay
import com.Velqino5.Raxzem.core.ui.ScreenBackground

@Composable
fun SplashRoute(onFinished: () -> Unit) {
    val splashViewModel: SplashViewModel = viewModel(factory = AppViewModelFactory)
    val finished by splashViewModel.finished.collectAsStateWithLifecycle()
    SplashScreen(finished = finished, onFinished = onFinished)
}

@Composable
fun SplashScreen(finished: Boolean, onFinished: () -> Unit) {
    var handedOver by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(finished) {
        if (finished && !handedOver) {
            handedOver = true
            onFinished()
        }
    }

    var entered by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        entered = true
    }
    val entryAlpha by animateFloatAsState(
        targetValue = if (entered) 1f else 0f,
        animationSpec = tween(durationMillis = 520, easing = FastOutSlowInEasing)
    )
    val entryScale by animateFloatAsState(
        targetValue = if (entered) 1f else 0.86f,
        animationSpec = tween(durationMillis = 520, easing = FastOutSlowInEasing)
    )

    val decor = rememberInfiniteTransition()
    val shimmer by decor.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val bob by decor.animateFloat(
        initialValue = 0f,
        targetValue = -8f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val orbit by decor.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    ScreenBackground(overlay = SplashOverlay) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 28.dp)
        ) {
            Spacer(modifier = Modifier.weight(0.26f))

            Box(contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(232.dp)
                        .graphicsLayer { rotationZ = orbit }
                ) {
                    OrbitDot(Modifier.align(Alignment.TopCenter))
                    OrbitDot(Modifier.align(Alignment.BottomStart))
                    OrbitDot(Modifier.align(Alignment.BottomEnd))
                }
                Box(
                    modifier = Modifier
                        .size(210.dp)
                        .background(
                            Brush.radialGradient(
                                listOf(PiggyAccent.copy(alpha = 0.18f), Color.Transparent)
                            ),
                            CircleShape
                        )
                )
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = stringResource(R.string.cd_logo),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(168.dp)
                        .scale(entryScale)
                        .alpha(entryAlpha * shimmer)
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = stringResource(R.string.app_title),
                style = MaterialTheme.typography.displaySmall.copy(
                    shadow = Shadow(
                        color = PiggyPrimary.copy(alpha = 0.7f),
                        offset = Offset(0f, 4f),
                        blurRadius = 18f
                    )
                ),
                color = PiggyTextPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(entryAlpha)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = stringResource(R.string.splash_subtitle),
                style = MaterialTheme.typography.labelSmall,
                color = PiggyTextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(entryAlpha)
            )

            Spacer(modifier = Modifier.height(30.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(18.dp),
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.alpha(0.55f * entryAlpha)
            ) {
                SplashPot(R.drawable.sprite_1, bob)
                SplashPot(R.drawable.sprite_2, bob * 0.6f)
                SplashPot(R.drawable.sprite_3, bob * 0.3f)
            }

            Spacer(modifier = Modifier.weight(0.42f))

            LinearProgressIndicator(
                color = PiggyAccent,
                trackColor = PiggyTextPrimary.copy(alpha = 0.12f),
                modifier = Modifier
                    .width(180.dp)
                    .height(5.dp)
                    .clip(RoundedCornerShape(3.dp))
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.splash_loading),
                style = MaterialTheme.typography.labelSmall,
                color = PiggyTextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(36.dp))
        }
    }
}

@Composable
private fun OrbitDot(modifier: Modifier) {
    Box(
        modifier = modifier
            .size(6.dp)
            .clip(CircleShape)
            .background(PiggyAccent.copy(alpha = 0.8f))
    )
}

@Composable
private fun SplashPot(art: Int, lift: Float) {
    Image(
        painter = painterResource(art),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .size(56.dp)
            .offset(y = lift.dp)
    )
}
