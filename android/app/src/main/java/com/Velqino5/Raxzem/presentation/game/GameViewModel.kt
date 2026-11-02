package com.Velqino5.Raxzem.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Velqino5.Raxzem.core.config.GameConfig
import com.Velqino5.Raxzem.data.sample.SampleData
import com.Velqino5.Raxzem.domain.model.CoinOutcome
import com.Velqino5.Raxzem.domain.model.PotType
import com.Velqino5.Raxzem.domain.model.RunResult
import com.Velqino5.Raxzem.domain.repository.DailyGoalsRepository
import com.Velqino5.Raxzem.domain.repository.ProgressRepository
import com.Velqino5.Raxzem.domain.repository.SettingsRepository
import com.Velqino5.Raxzem.domain.usecase.CompleteStageUseCase
import com.Velqino5.Raxzem.domain.usecase.NextCoinUseCase
import com.Velqino5.Raxzem.domain.usecase.ResolveCoinUseCase
import com.Velqino5.Raxzem.domain.usecase.UpdateDailyGoalsUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull

class GameViewModel(
    private val progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository,
    private val dailyGoalsRepository: DailyGoalsRepository,
    private val nextCoinUseCase: NextCoinUseCase,
    private val resolveCoinUseCase: ResolveCoinUseCase,
    private val completeStageUseCase: CompleteStageUseCase,
    private val updateDailyGoalsUseCase: UpdateDailyGoalsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private val _events = Channel<GameFinished>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private val taps = Channel<PotType>(Channel.CONFLATED)

    private var loopJob: Job? = null
    private var currentRunId = -1
    private val recentCoins = mutableListOf<PotType>()
    private var unlockedCharms: List<String> = emptyList()
    private var stageFlawless = true
    private var stagesCleared = 0
    private var earnedCharm: String? = null
    private var delivered = false

    fun enterRun(runId: Int) {
        if (runId != currentRunId) {
            currentRunId = runId
            startRun()
            return
        }
        if (_state.value.paused) {
            resume()
            return
        }
        if (loopJob?.isActive != true && _state.value.phase != GamePhase.FINISHED) {
            launchLoop(withReadyBeat = false)
        }
    }

    fun leaveRun() {
        pause()
    }

    fun pause() {
        loopJob?.cancel()
        loopJob = null
        if (_state.value.phase != GamePhase.FINISHED) {
            _state.update { it.copy(paused = true) }
        }
    }

    fun resume() {
        if (!_state.value.paused) return
        _state.update { it.copy(paused = false) }
        launchLoop(withReadyBeat = false)
    }

    fun quitRound() {
        loopJob?.cancel()
        _state.update { it.copy(paused = false) }
        loopJob = viewModelScope.launch { finishRun(routeCleared = false) }
    }

    fun onPotTap(pot: PotType) {
        if (!_state.value.inputEnabled) return
        taps.trySend(pot)
    }

    private fun startRun() {
        loopJob?.cancel()
        loopJob = viewModelScope.launch {
            val progress = progressRepository.load()
            val settings = settingsRepository.load()
            recentCoins.clear()
            unlockedCharms = progress.charms.map { it.id }
            stageFlawless = true
            stagesCleared = 0
            earnedCharm = null
            delivered = false
            _state.value = GameUiState(
                best = progress.bestScore,
                reducedMotion = settings.reducedMotion
            )
            delay(GameConfig.READY_DELAY_MS)
            coinCycle()
        }
    }

    private fun launchLoop(withReadyBeat: Boolean) {
        loopJob?.cancel()
        loopJob = viewModelScope.launch {
            if (withReadyBeat) {
                _state.update { it.copy(phase = GamePhase.READY) }
                delay(GameConfig.READY_DELAY_MS)
            }
            coinCycle()
        }
    }

    private suspend fun coinCycle() {
        while (currentCoroutineContext().isActive) {
            val snapshot = _state.value
            if (snapshot.lives <= 0) {
                finishRun(routeCleared = false)
                return
            }
            if (snapshot.stage > GameConfig.STAGES) {
                finishRun(routeCleared = true)
                return
            }

            val coin = snapshot.coinType ?: nextCoin()
            val stepMs = SampleData.stepMsFor(snapshot.stage)
            _state.update {
                it.copy(
                    phase = GamePhase.DROPPING,
                    coinType = coin,
                    coinStep = 0,
                    outcome = null,
                    correctPot = null,
                    tappedPot = null,
                    lastGain = 0
                )
            }
            while (taps.tryReceive().isSuccess) {
                continue
            }

            var tapped: PotType? = null
            coroutineScope {
                val stepper = launch {
                    for (step in 1..GameConfig.DROP_STEPS) {
                        delay(stepMs)
                        _state.update { it.copy(coinStep = step) }
                    }
                }
                tapped = withTimeoutOrNull(stepMs * GameConfig.DROP_STEPS) { taps.receive() }
                stepper.cancel()
            }

            val before = _state.value
            val resolution = resolveCoinUseCase(coin, tapped, before.streak, before.lives)
            if (resolution.outcome != CoinOutcome.CORRECT) {
                stageFlawless = false
            }
            val sorted = if (resolution.outcome == CoinOutcome.CORRECT) 1 else 0
            _state.update {
                it.copy(
                    phase = GamePhase.RESOLVING,
                    coinStep = GameConfig.DROP_STEPS,
                    score = it.score + resolution.scoreDelta,
                    streak = resolution.streak,
                    bestStreak = maxOf(it.bestStreak, resolution.streak),
                    lives = resolution.lives,
                    coinsSorted = it.coinsSorted + sorted,
                    outcome = resolution.outcome,
                    correctPot = coin,
                    tappedPot = tapped,
                    lastGain = resolution.scoreDelta
                )
            }
            delay(GameConfig.RESOLVE_MS)

            val resolved = _state.value
            if (resolved.lives <= 0) {
                finishRun(routeCleared = false)
                return
            }
            val coinsInStage = resolved.coinsInStage + 1
            if (coinsInStage >= GameConfig.COINS_PER_STAGE) {
                completeStage(resolved.stage)
                if (resolved.stage + 1 > GameConfig.STAGES) {
                    finishRun(routeCleared = true)
                    return
                }
            } else {
                _state.update {
                    it.copy(
                        coinsInStage = coinsInStage,
                        coinNumber = coinsInStage + 1,
                        coinType = null,
                        coinStep = 0
                    )
                }
            }
        }
    }

    private suspend fun completeStage(stage: Int) {
        val charmId = SampleData.charmIdFor(stage)
        val reward = completeStageUseCase(charmId, stageFlawless, unlockedCharms)
        stagesCleared += 1
        if (reward.charmId != null) {
            progressRepository.unlockCharm(reward.charmId)
            unlockedCharms = unlockedCharms + reward.charmId
            earnedCharm = reward.charmId
        }
        _state.update {
            it.copy(
                phase = GamePhase.STAGE_CLEARED,
                score = it.score + reward.bonus,
                coinsInStage = GameConfig.COINS_PER_STAGE,
                charmId = reward.charmId ?: charmId,
                coinType = null,
                coinStep = 0
            )
        }
        delay(GameConfig.STAGE_BANNER_MS)
        stageFlawless = true
        _state.update {
            it.copy(
                stage = it.stage + 1,
                coinsInStage = 0,
                coinNumber = 1,
                coinType = null,
                coinStep = 0,
                outcome = null,
                correctPot = null,
                tappedPot = null
            )
        }
    }

    private suspend fun finishRun(routeCleared: Boolean) {
        if (delivered) return
        delivered = true
        val snapshot = _state.value
        _state.update { it.copy(phase = GamePhase.FINISHED, coinType = null, paused = false) }
        delay(GameConfig.FINISH_DELAY_MS)
        val finalStage = if (routeCleared) {
            GameConfig.STAGES
        } else {
            snapshot.stage.coerceAtMost(GameConfig.STAGES)
        }
        val result = RunResult(
            score = snapshot.score,
            stage = finalStage,
            stagesCleared = stagesCleared,
            coinsSorted = snapshot.coinsSorted,
            bestStreak = snapshot.bestStreak,
            charmId = earnedCharm,
            routeCleared = routeCleared
        )
        val newBest = snapshot.score > snapshot.best && snapshot.score > 0
        progressRepository.recordRun(result)
        val goals = dailyGoalsRepository.load()
        dailyGoalsRepository.save(updateDailyGoalsUseCase(goals, result))
        _events.send(GameFinished(result, newBest))
    }

    private fun nextCoin(): PotType {
        val coin = nextCoinUseCase(recentCoins)
        recentCoins.add(coin)
        if (recentCoins.size > 4) {
            recentCoins.removeAt(0)
        }
        return coin
    }
}
