package com.example.practica_1_moviles.ui.game

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.practica_1_moviles.data.Difficulty
import com.example.practica_1_moviles.data.Monster
import com.example.practica_1_moviles.data.Question
import com.example.practica_1_moviles.data.QuestionRepository
import com.example.practica_1_moviles.data.SettingsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

enum class GameStatus { LOADING, ANSWERING, FEEDBACK, VICTORY, DEFEAT }

class GameViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    companion object {
        const val PLAYER_MAX_HP = 100
        const val MIN_DAMAGE = 10
        const val MAX_DAMAGE = 50
        const val TICK_MS = 100L
        const val HP_POINTS = 2
        const val VICTORY_BONUS = 100
    }

    private val category: String =
        savedStateHandle.get<String>("category")?.let { Uri.decode(it) }
            ?: QuestionRepository.RANDOM

    private val repository = QuestionRepository.create(application)
    private val settingsRepository = SettingsRepository.create(application)   // NUEVO

    // --- Datos del juego ---
    private val monsters = listOf(
        Monster("Slime", maxHp = 60, damage = 15),
        Monster("Goblin", maxHp = 90, damage = 20),
        Monster("Dragón", maxHp = 150, damage = 30)
    )
    private var questions: List<Question> = emptyList()

    // --- Estado observable por la UI ---
    var status by mutableStateOf(GameStatus.LOADING)
        private set
    var playerHp by mutableIntStateOf(PLAYER_MAX_HP)
        private set
    var monsterIndex by mutableIntStateOf(0)
        private set
    var monsterHp by mutableIntStateOf(monsters[0].maxHp)
        private set
    var questionIndex by mutableIntStateOf(0)
        private set
    var timeLeftMs by mutableLongStateOf(0L)
        private set
    var selectedAnswer by mutableStateOf<Int?>(null)
        private set
    var lastDamageDealt by mutableIntStateOf(0)
        private set
    var lastDamageTaken by mutableIntStateOf(0)
        private set
    var totalDamage by mutableIntStateOf(0)
        private set

    // Dificultad de esta partida
    var difficulty by mutableStateOf(Difficulty.NORMAL)
        private set

    // Tiempo por pregunta según la dificultad
    val timeLimitMs: Long get() = difficulty.timeLimitMs

    // Puntos que se ven durante la partida
    val liveScore: Int
        get() = (totalDamage * difficulty.multiplier).roundToInt()

    // Puntuación final con multiplicador
    val finalScore: Int
        get() {
            val base = totalDamage +
                    playerHp * HP_POINTS +
                    if (status == GameStatus.VICTORY) VICTORY_BONUS else 0
            return (base * difficulty.multiplier).roundToInt()
        }

    val currentMonster: Monster get() = monsters[monsterIndex]
    val currentQuestion: Question get() = questions[questionIndex % questions.size]

    private var timerJob: Job? = null

    init { loadGame() }

    // --- Carga de ajustes y preguntas desde Room ---
    private fun loadGame() {
        viewModelScope.launch {
            difficulty = Difficulty.fromName(settingsRepository.get().difficulty)
            questions = repository.getQuestions(category)
            status = GameStatus.ANSWERING
            startTimer()
        }
    }

    // --- Temporizador ---
    private fun startTimer() {
        timerJob?.cancel()
        timeLeftMs = timeLimitMs
        timerJob = viewModelScope.launch {
            while (timeLeftMs > 0) {
                delay(TICK_MS)
                timeLeftMs -= TICK_MS
            }
            onTimeout()
        }
    }

    // --- Acciones del jugador ---
    fun onAnswerSelected(index: Int) {
        if (status != GameStatus.ANSWERING) return
        timerJob?.cancel()
        selectedAnswer = index

        if (index == currentQuestion.correctIndex) {
            val fraction = timeLeftMs.toFloat() / timeLimitMs
            val damage = (MIN_DAMAGE + (MAX_DAMAGE - MIN_DAMAGE) * fraction).toInt()
            monsterHp = (monsterHp - damage).coerceAtLeast(0)
            lastDamageDealt = damage
            totalDamage += damage
            lastDamageTaken = 0
        } else {
            hitPlayer()
        }
        resolveTurn()
    }

    private fun onTimeout() {
        selectedAnswer = null
        hitPlayer()
        resolveTurn()
    }

    private fun hitPlayer() {
        lastDamageDealt = 0
        lastDamageTaken = currentMonster.damage
        playerHp = (playerHp - lastDamageTaken).coerceAtLeast(0)
    }

    private fun resolveTurn() {
        status = when {
            playerHp <= 0 -> GameStatus.DEFEAT
            monsterHp <= 0 && monsterIndex == monsters.lastIndex -> GameStatus.VICTORY
            else -> GameStatus.FEEDBACK
        }
    }

    fun nextTurn() {
        if (status != GameStatus.FEEDBACK) return
        if (monsterHp <= 0) {
            monsterIndex++
            monsterHp = currentMonster.maxHp
        }
        questionIndex++
        selectedAnswer = null
        status = GameStatus.ANSWERING
        startTimer()
    }
}