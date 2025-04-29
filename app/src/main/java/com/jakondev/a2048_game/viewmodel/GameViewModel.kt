package com.jakondev.a2048_game.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.random.Random

class GameViewModel : ViewModel() {

    // -------------------------
    // ESTADO DEL JUEGO
    // -------------------------
    private val _board = MutableStateFlow(Array(4) { IntArray(4) })
    val board: StateFlow<Array<IntArray>> = _board.asStateFlow()

    private val _prevBoard = MutableStateFlow(Array(4) { IntArray(4) })
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

    // -------------------------
    // TEMPORIZADOR
    // -------------------------
    private var isTimeRunning = false
    private var timerJob: Job? = null

    fun startTimer() {
        if (isTimeRunning) return
        isTimeRunning = true
        timerJob = viewModelScope.launch {
            while (isTimeRunning) {
                delay(1000L)
                _time.value += 1
            }
        }
    }

    fun pauseTimer() {
        isTimeRunning = false
        timerJob?.cancel()
    }

    fun resumeTimer() {
        if (!isTimeRunning) startTimer()
    }

    // -------------------------
    // CONTROL DE JUEGO
    // -------------------------
    fun resetGame() {
        val newBoard = SampleBoards().almostLost
        repeat(2) { addRandomTile(newBoard) }
        _board.value = newBoard
        _prevBoard.value = newBoard.map { it.clone() }.toTypedArray()
        _score.value = 0
        _swipes.value = 0
        _time.value = 0
        _isGameOver.value = false
        _is2048.value = false
        _hasWon.value = false
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
    fun moveLeft() = moveRows { merge(it) }
    fun moveRight() = moveRows { merge(it.reversedArray()).reversedArray() }
    fun moveUp() = moveColumns { merge(it) }
    fun moveDown() = moveColumns { merge(it.reversedArray()).reversedArray() }

    private fun moveRows(transform: (IntArray) -> IntArray) {
        val current = _board.value
        _prevBoard.value = current.map { it.clone() }.toTypedArray()

        val newBoard = Array(4) { row -> transform(current[row]) }
        applyMove(newBoard)
    }

    private fun moveColumns(transform: (IntArray) -> IntArray) {
        val current = _board.value
        _prevBoard.value = current.map { it.clone() }.toTypedArray()

        val newBoard = Array(4) { IntArray(4) }

        for (j in 0..3) {
            val column = IntArray(4) { i -> current[i][j] }
            val transformed = transform(column)
            for (i in 0..3) newBoard[i][j] = transformed[i]
        }

        applyMove(newBoard)
    }

    private fun applyMove(newBoard: Array<IntArray>) {
        if (!boardEquals(newBoard, _board.value)) {
            addRandomTile(newBoard)
            incrementSwipes()
        }
        _board.value = newBoard
        _isGameOver.value = !canMakeMove(newBoard)
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
                if (compacted[i] == 2048 && !_hasWon.value) {
                    _is2048.value = true
                    _hasWon.value = true
                }
                compacted.removeAt(i + 1)
            }
            i++
        }

        _score.value += gained
        return compacted.toIntArray().copyOf(4)
    }

    private fun addRandomTile(board: Array<IntArray>) {
        val emptySpots = buildList {
            for (i in 0..3) {
                for (j in 0..3) {
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
