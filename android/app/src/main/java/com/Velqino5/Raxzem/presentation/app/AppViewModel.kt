package com.Velqino5.Raxzem.presentation.app

import androidx.lifecycle.ViewModel
import com.Velqino5.Raxzem.core.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AppViewModel : ViewModel() {

    private val _screen = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = _screen.asStateFlow()

    private val _backStack = MutableStateFlow<List<Screen>>(emptyList())
    val backStack: StateFlow<List<Screen>> = _backStack.asStateFlow()

    private var runCounter = 0

    fun toMenu() {
        _backStack.value = emptyList()
        _screen.value = Screen.Menu
    }

    fun startGame(replaceCurrent: Boolean = false) {
        runCounter += 1
        navigate(Screen.Game(runCounter), replaceCurrent)
    }

    fun finishGame(destination: Screen.GameOver) {
        navigate(destination, replaceCurrent = true)
    }

    fun back() {
        val previous = _backStack.value.lastOrNull() ?: return
        _backStack.update { stack -> stack.dropLast(1) }
        _screen.value = previous
    }

    private fun navigate(target: Screen, replaceCurrent: Boolean) {
        val current = _screen.value
        if (!replaceCurrent && current !is Screen.Splash) {
            _backStack.update { stack -> stack + current }
        }
        _screen.value = target
    }
}
