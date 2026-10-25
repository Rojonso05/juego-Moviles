package com.example.practica_1_moviles.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.practica_1_moviles.data.Difficulty
import com.example.practica_1_moviles.data.SettingsEntity
import com.example.practica_1_moviles.data.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SettingsRepository.create(application)

    // null mientras se leen los ajustes de la base de datos
    val settings: StateFlow<SettingsEntity?> = repository.settings
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = null
        )

    fun setPlayerName(name: String) {
        viewModelScope.launch { repository.setPlayerName(name) }
    }

    fun setDifficulty(difficulty: Difficulty) {
        viewModelScope.launch { repository.setDifficulty(difficulty) }
    }

    fun setSoundEnabled(enabled: Boolean) {
        viewModelScope.launch { repository.setSoundEnabled(enabled) }
    }
}