package com.Velqino5.Raxzem.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Velqino5.Raxzem.core.di.AppViewModelFactory
import com.Velqino5.Raxzem.core.navigation.Screen
import com.Velqino5.Raxzem.presentation.game.GameRoute
import com.Velqino5.Raxzem.presentation.gameover.GameOverRoute
import com.Velqino5.Raxzem.presentation.menu.MenuRoute
import com.Velqino5.Raxzem.presentation.splash.SplashRoute

@Composable
fun AppRoot() {
    val appViewModel: AppViewModel = viewModel(factory = AppViewModelFactory)
    val screen by appViewModel.screen.collectAsStateWithLifecycle()
    val backStack by appViewModel.backStack.collectAsStateWithLifecycle()

    BackHandler(enabled = backStack.isNotEmpty()) {
        appViewModel.back()
    }

    AnimatedContent(
        targetState = screen,
        transitionSpec = {
            (fadeIn(animationSpec = tween(durationMillis = 220)) +
                slideInVertically(animationSpec = tween(durationMillis = 220)) { full ->
                    full / 12
                }) togetherWith fadeOut(animationSpec = tween(durationMillis = 180))
        },
        modifier = Modifier.fillMaxSize()
    ) { current ->
        when (current) {
            is Screen.Splash -> SplashRoute(
                onFinished = appViewModel::toMenu
            )

            is Screen.Menu -> MenuRoute(
                onPlay = { appViewModel.startGame() }
            )

            is Screen.Game -> GameRoute(
                runId = current.runId,
                onGameOver = { finished ->
                    appViewModel.finishGame(
                        Screen.GameOver(
                            score = finished.result.score,
                            stage = finished.result.stage,
                            coinsSorted = finished.result.coinsSorted,
                            bestStreak = finished.result.bestStreak,
                            charmId = finished.result.charmId,
                            routeCleared = finished.result.routeCleared,
                            newBest = finished.newBest
                        )
                    )
                }
            )

            is Screen.GameOver -> GameOverRoute(
                destination = current,
                onAgain = { appViewModel.startGame(replaceCurrent = true) },
                onMenu = appViewModel::toMenu
            )
        }
    }
}
