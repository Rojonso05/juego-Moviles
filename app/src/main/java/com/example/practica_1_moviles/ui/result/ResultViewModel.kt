package com.example.practica_1_moviles.ui.result

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.practica_1_moviles.data.QuestionDatabase
import com.example.practica_1_moviles.data.ScoreEntity
import com.example.practica_1_moviles.data.SettingsRepository
import kotlinx.coroutines.launch

class ResultViewModel(application: Application) : AndroidViewModel(application) {

    private val scoreDao = QuestionDatabase.getDatabase(application).scoreDao()
    private val settingsRepository = SettingsRepository.create(application)

    // Evita guardar la misma partida dos veces
    var saved by mutableStateOf(false)
        private set

    // Nombre guardado en Ajustes. Es null hasta que se leen de la base de datos
    var defaultName by mutableStateOf<String?>(null)
        private set

    init {
        viewModelScope.launch {
            defaultName = settingsRepository.get().playerName
        }
    }

    fun saveScore(
        name: String,
        points: Int,
        category: String,
        victory: Boolean,
        difficulty: String
    ) {
        if (saved) return
        saved = true
        viewModelScope.launch {
            scoreDao.insert(
                ScoreEntity(
                    playerName = name.trim(),
                    points = points,
                    category = category,
                    victory = victory,
                    difficulty = difficulty
                )
            )
        }
    }
}