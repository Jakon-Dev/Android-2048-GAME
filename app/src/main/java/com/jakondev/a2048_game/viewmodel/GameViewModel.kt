package com.jakondev.a2048_game.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.random.Random

val COLUMNS = 4
val ROWS = 4

class GameViewModel : ViewModel() {

    // -------------------------
    // ESTADO DEL JUEGO
    // -------------------------
    private val _board = MutableStateFlow(Array(ROWS) { IntArray(COLUMNS) })
    val board: StateFlow<Array<IntArray>> = _board.asStateFlow()

    private val _prevBoard = MutableStateFlow(Array(ROWS) { IntArray(COLUMNS) })
    private val _canGoBack = MutableStateFlow(true)
    val canGoBack: StateFlow<Boolean> = _canGoBack

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score.asStateFlow()

    private val _swipes = MutableStateFlow(0)
    val swipes: StateFlow<Int> = _swipes.asStateFlow()

    private val _isGameOver = MutableStateFlow(false)
    val isGameOver: StateFlow<Boolean> = _isGameOver.asStateFlow()

    private val _is2048 = MutableStateFlow(false)
    val is2048: StateFlow<Boolean> = _is2048.asStateFlow()

    private val _hasWon = MutableStateFlow(false)
    val hasWon: StateFlow<Boolean> = _hasWon.asStateFlow()

    private val _time = MutableStateFlow(0)
    val time: StateFlow<Int> = _time.asStateFlow()

    val gameOverReason = MutableStateFlow<String>("timeout")

    // -------------------------
    // TEMPORIZADOR
    // -------------------------

    private var isTimeRunning = false
    private var timerJob: Job? = null

    fun startTimer() {
        if (isTimeRunning) return
        isTimeRunning = true
        Log.d(TAG, "Timer started with mode: ${currentMode.value}")

        timerJob = viewModelScope.launch {
            while (isTimeRunning) {
                delay(1000L)

                if (currentMode.value == GameMode.CLASSIC) {
                    _time.value += 1
                    if (_time.value % 5 == 0) {
                        Log.d(TAG, "Classic mode time: ${_time.value}s")
                    }
                } else {
                    if (_time.value > 0) {
                        _time.value -= 1
                        if (_time.value % 5 == 0) {
                            Log.d(TAG, "Countdown time: ${_time.value}s")
                        }
                    } else {
                        isTimeRunning = false
                        _isGameOver.value = true
                        Log.d(TAG, "Game Over due to timeout")
                    }
                }
            }
        }
    }



    fun pauseTimer() {
        isTimeRunning = false
        Log.d(TAG, "Timer paused")
        timerJob?.cancel()
    }

    fun resumeTimer() {
        if (!isTimeRunning){
            startTimer()
            Log.d(TAG, "Timer resumed")
        }

    }

    // -------------------------
    // MODOS DE JUEGO
    // -------------------------

    var currentMode = MutableStateFlow(GameMode.CLASSIC)

    enum class GameMode {
        CLASSIC,
        COUNTDOWN_15,
        COUNTDOWN_20,
        COUNTDOWN_30
    }

    fun setGameMode(mode: GameMode) {
        currentMode.value = mode
    }


    // -------------------------
    // CONTROL DE JUEGO
    // -------------------------

    fun resetGame() {
        val newBoard = SampleBoards().empty
        repeat(2) { addRandomTile(newBoard) }
        _board.value = newBoard
        _prevBoard.value = newBoard.map { it.clone() }.toTypedArray()
        _score.value = 0
        _swipes.value = 0
        _isGameOver.value = false
        _is2048.value = false
        _hasWon.value = false

        when (currentMode.value) {
            GameMode.COUNTDOWN_15 -> _time.value = 15 * 60
            GameMode.COUNTDOWN_20 -> _time.value = 20 * 60
            GameMode.COUNTDOWN_30 -> _time.value = 30 * 60
            else -> _time.value = 0
        }

        startTimer()
    }

    fun resumeGame() {
        resumeTimer()
        _is2048.value = false
    }

    fun incrementSwipes() {
        _swipes.value++
    }

    fun undoMove() {
        if (!boardEquals(_board.value, _prevBoard.value)) {
            _board.value = _prevBoard.value.map { it.clone() }.toTypedArray()
            _swipes.value = (_swipes.value - 1).coerceAtLeast(0)
            _isGameOver.value = false
        } else {
            triggerCannotUndoEffect()
        }
    }

    private fun triggerCannotUndoEffect() {
        viewModelScope.launch {
            _canGoBack.emit(false)
            delay(300)
            _canGoBack.emit(true)
        }
    }

    // -------------------------
    // MOVIMIENTOS
    // -------------------------

    fun moveLeft() {
        Log.d(TAG, "Move: LEFT")
        moveRows { merge(it) }
    }

    fun moveRight() {
        Log.d(TAG, "Move: RIGHT")
        moveRows { merge(it.reversedArray()).reversedArray() }
    }

    fun moveUp() {
        Log.d(TAG, "Move: UP")
        moveColumns { merge(it) }
    }

    fun moveDown() {
        Log.d(TAG, "Move: DOWN")
        moveColumns { merge(it.reversedArray()).reversedArray() }
    }


    private fun moveRows(transform: (IntArray) -> IntArray) {
        val current = _board.value
        _prevBoard.value = current.map { it.clone() }.toTypedArray()

        val newBoard = Array(ROWS) { row -> transform(current[row]) }
        applyMove(newBoard)
    }

    private fun moveColumns(transform: (IntArray) -> IntArray) {
        val current = _board.value
        _prevBoard.value = current.map { it.clone() }.toTypedArray()

        val newBoard = Array(ROWS) { IntArray(COLUMNS) }

        for (j in 0 until COLUMNS) {
            val column = IntArray(ROWS) { i -> current[i][j] }
            val transformed = transform(column)
            for (i in 0 until ROWS) newBoard[i][j] = transformed[i]
        }

        applyMove(newBoard)
    }

    private fun applyMove(newBoard: Array<IntArray>) {
        if (!boardEquals(newBoard, _board.value)) {
            _prevBoard.value = _board.value.map { it.clone() }.toTypedArray()
            addRandomTile(newBoard)
            incrementSwipes()
        }
        _board.value = newBoard
        _isGameOver.value = !canMakeMove(newBoard)

        if (_isGameOver.value) {
            gameOverReason.value = if (_swipes.value == 0) "no_moves" else "timeout"
            Log.d(TAG, "Game Over! Reason: ${gameOverReason.value}")
        }
    }

    // -------------------------
    // LÓGICA DEL JUEGO
    // -------------------------

    private fun merge(row: IntArray): IntArray {
        val compacted = row.filter { it != 0 }.toMutableList()
        var i = 0
        var gained = 0

        while (i < compacted.size - 1) {
            if (compacted[i] == compacted[i + 1]) {
                compacted[i] *= 2
                gained += compacted[i]
                Log.d(TAG, "Merged tiles to ${compacted[i]}")
                if (compacted[i] == 2048 && !_hasWon.value) {
                    _is2048.value = true
                    _hasWon.value = true
                    Log.d(TAG, "2048 tile achieved!")
                }
                compacted.removeAt(i + 1)
            }
            i++
        }

        _score.value += gained
        if (gained > 0){
            Log.d(TAG, "Score increased by $gained. Total: ${_score.value}")
        }
        return compacted.toIntArray().copyOf(COLUMNS)
    }

    private fun addRandomTile(board: Array<IntArray>) {
        val emptySpots = buildList {
            for (i in 0 until ROWS) {
                for (j in 0 until COLUMNS) {
                    if (board[i][j] == 0) add(i to j)
                }
            }
        }

        if (emptySpots.isNotEmpty()) {
            val (i, j) = emptySpots.random()
            board[i][j] = if (Random.nextFloat() < 0.9f) 2 else 4
        }
    }

    private fun boardEquals(a: Array<IntArray>, b: Array<IntArray>): Boolean {
        return a.indices.all { i -> a[i].contentEquals(b[i]) }
    }

    private fun canMakeMove(board: Array<IntArray>): Boolean {
        for (i in 0..3) {
            for (j in 0..3) {
                if (board[i][j] == 0) return true
                if (j < 3 && board[i][j] == board[i][j + 1]) return true
                if (i < 3 && board[i][j] == board[i + 1][j]) return true
            }
        }
        return false
    }
}
