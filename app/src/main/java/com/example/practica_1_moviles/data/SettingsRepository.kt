package com.example.practica_1_moviles.data

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class SettingsRepository(private val dao: SettingsDao) {

    companion object {
        const val MAX_NAME_LENGTH = 12

        fun create(context: Context) =
            SettingsRepository(QuestionDatabase.getDatabase(context).settingsDao())
    }

    // Evita que dos cambios seguidos (por ejemplo, al teclear el nombre) se pisen
    private val writeMutex = Mutex()

    // Ajustes actuales. Si aún no se ha guardado nada, devuelve los valores por defecto
    val settings: Flow<SettingsEntity> = dao.observe().map { it ?: SettingsEntity() }

    // Lectura única, para usarla al empezar una partida
    suspend fun get(): SettingsEntity = dao.get() ?: SettingsEntity()

    suspend fun setPlayerName(name: String) = update { it.copy(playerName = name.take(MAX_NAME_LENGTH)) }

    suspend fun setDifficulty(difficulty: Difficulty) = update { it.copy(difficulty = difficulty.name) }

    suspend fun setSoundEnabled(enabled: Boolean) = update { it.copy(soundEnabled = enabled) }

    // Lee los ajustes, cambia solo un campo y guarda la fila completa
    private suspend fun update(change: (SettingsEntity) -> SettingsEntity) {
        writeMutex.withLock {
            dao.save(change(get()))
        }
    }
}