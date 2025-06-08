package com.jakondev.a2048_game.viewmodel

import android.app.Application
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.game2048.R
import com.jakondev.a2048_game.data.*
import com.jakondev.a2048_game.model.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.random.Random

class GameViewModel(application: Application) : AndroidViewModel(application) {

    // ---------------------------------
    // CONSTANTES
    // ---------------------------------
    val COLUMNS = 4
    val ROWS = 4

    // ---------------------------------
    // REPOSITORIOS Y PREFERENCIAS
    // ---------------------------------
    private val prefsRepo = UserPreferencesRepository(application)
    private val achievementRepo = AchievementRepository(GameDatabase.getInstance(getApplication()).achievementDao())

    private val _userPreferences = MutableStateFlow(UserPreferences())
    val userPreferences: StateFlow<UserPreferences> = _userPreferences

    val achievements = achievementRepo.achievements.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    init {
        viewModelScope.launch {
            Log.d(TAG, "Inicializando logros por defecto y preferencias de usuario...")
            achievementRepo.insertDefaults(defaultAchievements())
            prefsRepo.preferencesFlow.collect {
                _userPreferences.value = it
                Log.d(TAG, "Preferencias de usuario actualizadas: $it")
            }
        }
    }

    // ---------------------------------
    // LOGRO (Achievements)
    // ---------------------------------
    fun unlockAchievement(id: String) {
        viewModelScope.launch {
            achievementRepo.unlockAchievement(id)
        }
    }

    private fun defaultAchievements() = listOf(
        Achievement("first_game", R.string.AC_first_game_title, R.string.AC_first_game_desc),
        Achievement("win_game", R.string.AC_win_game_title, R.string.AC_win_game_desc),
        Achievement("high_score_5000", R.string.AC_high_score_5000_title, R.string.AC_high_score_5000_desc),
        Achievement("high_score_10000", R.string.AC_high_score_10000_title, R.string.AC_high_score_10000_desc),
        Achievement("move_100", R.string.AC_move_100_title, R.string.AC_move_100_desc),
        Achievement("swipe_undo", R.string.AC_swipe_undo_title, R.string.AC_swipe_undo_desc),
        Achievement("tile_512", R.string.AC_tile_512_title, R.string.AC_tile_512_desc),
        Achievement("tile_1024", R.string.AC_tile_1024_title, R.string.AC_tile_1024_desc),
        Achievement("tile_2048", R.string.AC_tile_2048_title, R.string.AC_tile_2048_desc),
    )

    // ---------------------------------
    // ESTADO DEL JUEGO
    // ---------------------------------
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

    val gameOverReason = MutableStateFlow("timeout")
    private var userAlias = "Player"

    private val _canMoveUp = MutableStateFlow(true)
    val canMoveUp: StateFlow<Boolean> = _canMoveUp

    private val _canMoveDown = MutableStateFlow(true)
    val canMoveDown: StateFlow<Boolean> = _canMoveDown

    private val _canMoveLeft = MutableStateFlow(true)
    val canMoveLeft: StateFlow<Boolean> = _canMoveLeft

    private val _canMoveRight = MutableStateFlow(true)
    val canMoveRight: StateFlow<Boolean> = _canMoveRight

    // ---------------------------------
    // TEMPORIZADOR
    // ---------------------------------
    private var isTimeRunning = false
    private var timerJob: Job? = null

    fun startTimer() {
        if (isTimeRunning) return
        isTimeRunning = true
        Log.d(TAG, "Temporizador iniciado. Modo actual: ${currentMode.value}")

        timerJob = viewModelScope.launch {
            while (isTimeRunning) {
                delay(1000L)
                Log.d(TAG, "Tick de temporizador. Tiempo actual: ${_time.value}")

                if (currentMode.value == GameMode.CLASSIC) {
                    _time.value += 1
                } else {
                    if (_time.value > 0) {
                        _time.value -= 1
                    } else {
                        Log.d(TAG, "Tiempo agotado. Fin del juego.")
                        isTimeRunning = false
                        _isGameOver.value = true
                        gameOverReason.value = "timeout"
                        playSoundEffect(SoundEvent.Lose)
                        saveCurrentGame(getApplication(), "timeout")
                    }
                }
            }
        }
    }

    fun pauseTimer() {
        isTimeRunning = false
        timerJob?.cancel()
        Log.d(TAG, "Timer paused")
    }

    fun resumeTimer() {
        if (!isTimeRunning) startTimer()
    }

    // ---------------------------------
    // MODOS DE JUEGO
    // ---------------------------------
    var currentMode = MutableStateFlow(GameMode.CLASSIC)

    enum class GameMode {
        CLASSIC, COUNTDOWN_15, COUNTDOWN_20, COUNTDOWN_30
    }

    fun setGameMode(mode: GameMode) {
        currentMode.value = mode
    }

    // ---------------------------------
    // EFECTOS DE SONIDO
    // ---------------------------------
    private val _playSound = MutableSharedFlow<SoundEvent>()
    val playSound: SharedFlow<SoundEvent> = _playSound

    sealed class SoundEvent {
        object Win : SoundEvent()
        object Lose : SoundEvent()
        object Button : SoundEvent()
        object Pop : SoundEvent()
    }

    fun playSoundEffect(event: SoundEvent) {
        if (_userPreferences.value.isMuted) return
        viewModelScope.launch { _playSound.emit(event) }
    }

    // ---------------------------------
    // CONTROL DEL JUEGO
    // ---------------------------------
    fun resetGame() {
        Log.d(TAG, "Reiniciando juego...")
        unlockAchievement("first_game")
        val newBoard = SampleBoards().empty
        repeat(2) { addRandomTile(newBoard) }
        _board.value = newBoard
        _prevBoard.value = newBoard.map { it.clone() }.toTypedArray()
        _score.value = 0
        _swipes.value = 0
        _isGameOver.value = false
        _is2048.value = false
        _hasWon.value = false

        _time.value = when (currentMode.value) {
            GameMode.COUNTDOWN_15 -> 15 * 60
            GameMode.COUNTDOWN_20 -> 20 * 60
            GameMode.COUNTDOWN_30 -> 30 * 60
            else -> 0
        }

        Log.d(TAG, "Tablero inicializado. Tiempo asignado: ${_time.value} segundos")

        updateMovableDirections()
        startTimer()
    }

    fun resumeGame() {
        resumeTimer()
        _is2048.value = false
    }

    fun incrementSwipes() {
        _swipes.value++
        if (_swipes.value >= 100) unlockAchievement("move_100")
    }

    fun undoMove() {
        if (!boardEquals(_board.value, _prevBoard.value)) {
            Log.d(TAG, "Deshacer movimiento activado.")
            unlockAchievement("swipe_undo")
            _board.value = _prevBoard.value.map { it.clone() }.toTypedArray()
            _swipes.value = (_swipes.value - 1).coerceAtLeast(0)
            _isGameOver.value = false
        } else {
            Log.d(TAG, "Deshacer movimiento no permitido.")
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

    fun setUserAlias(alias: String) {
        userAlias = alias
    }

    // ---------------------------------
    // MOVIMIENTOS
    // ---------------------------------
    enum class Direction { UP, DOWN, LEFT, RIGHT }

    fun moveLeft() = moveRows { merge(it) }.also { updateMovableDirections() }
    fun moveRight() = moveRows { merge(it.reversedArray()).reversedArray() }.also { updateMovableDirections() }
    fun moveUp() = moveColumns { merge(it) }.also { updateMovableDirections() }
    fun moveDown() = moveColumns { merge(it.reversedArray()).reversedArray() }.also { updateMovableDirections() }

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
            Log.d(TAG, "Movimiento aplicado. Tablero ha cambiado.")
            _prevBoard.value = _board.value.map { it.clone() }.toTypedArray()
            addRandomTile(newBoard)
            incrementSwipes()
        } else {
            Log.d(TAG, "Movimiento ignorado. El tablero no ha cambiado.")
        }

        _board.value = newBoard

        if (!canMakeMove(newBoard)) {
            Log.d(TAG, "No se pueden hacer más movimientos. Juego terminado.")
            _isGameOver.value = true
            gameOverReason.value = "no_moves"
            playSoundEffect(SoundEvent.Lose)
            saveCurrentGame(getApplication(), "lose")
        }
    }

    private fun updateMovableDirections() {
        _canMoveUp.value = canMoveInDirection(Direction.UP)
        _canMoveDown.value = canMoveInDirection(Direction.DOWN)
        _canMoveLeft.value = canMoveInDirection(Direction.LEFT)
        _canMoveRight.value = canMoveInDirection(Direction.RIGHT)
    }

    private fun canMoveInDirection(direction: Direction): Boolean {
        val board = _board.value
        for (row in 0 until ROWS) {
            for (col in 0 until COLUMNS) {
                val current = board[row][col]
                if (current == 0) continue
                val (nextRow, nextCol) = when (direction) {
                    Direction.UP -> row - 1 to col
                    Direction.DOWN -> row + 1 to col
                    Direction.LEFT -> row to col - 1
                    Direction.RIGHT -> row to col + 1
                }
                if (nextRow !in 0 until ROWS || nextCol !in 0 until COLUMNS) continue
                val target = board[nextRow][nextCol]
                if (target == 0 || target == current) return true
            }
        }
        return false
    }

    // ---------------------------------
    // LÓGICA DEL JUEGO
    // ---------------------------------
    private fun merge(row: IntArray): IntArray {
        val compacted = row.filter { it != 0 }.toMutableList()
        var i = 0
        var gained = 0

        while (i < compacted.size - 1) {
            if (compacted[i] == compacted[i + 1]) {
                compacted[i] *= 2
                playSoundEffect(SoundEvent.Pop)
                gained += compacted[i]
                Log.d(TAG, "Combinación realizada: ${compacted[i]} en índice $i")

                when (compacted[i]) {
                    512 -> unlockAchievement("tile_512")
                    1024 -> unlockAchievement("tile_1024")
                    2048 -> {
                        unlockAchievement("tile_2048")
                        if (!_hasWon.value) {
                            Log.d(TAG, "Jugador ha ganado con 2048!")
                            _is2048.value = true
                            _hasWon.value = true
                            playSoundEffect(SoundEvent.Win)
                            saveCurrentGame(getApplication(), "win")
                            unlockAchievement("win_game")
                        }
                    }
                }
                compacted.removeAt(i + 1)
            }
            i++
        }

        _score.value += gained
        Log.d(TAG, "Puntos ganados en merge: $gained, Puntaje total: ${_score.value}")

        if (_score.value >= 5000) unlockAchievement("high_score_5000")
        if (_score.value >= 10000) unlockAchievement("high_score_10000")

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

    private fun boardEquals(a: Array<IntArray>, b: Array<IntArray>) =
        a.indices.all { i -> a[i].contentEquals(b[i]) }

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

    fun saveCurrentGame(context: Context, reason: String) {
        val boardState = _board.value.joinToString("|") { row -> row.joinToString(",") }
        val result = when (reason) {
            "win" -> "WIN"
            "timeout" -> "TIMEOUT"
            else -> "LOSE"
        }

        val gameResult = GameResult(
            score = _score.value,
            time = _time.value,
            swipes = _swipes.value,
            board = boardState,
            result = result,
            date = System.currentTimeMillis()
        )

        Log.d(TAG, "Guardando resultado del juego: $gameResult")

        viewModelScope.launch {
            val db = GameDatabase.getInstance(getApplication())
            val repo = GameRepository(db.gameResultDao())
            repo.insert(gameResult)
            Log.d(TAG, "Resultado del juego guardado en base de datos.")
        }
    }
}
