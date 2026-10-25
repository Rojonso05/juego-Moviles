package com.example.practica_1_moviles.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Tabla con una única fila (id = 1) que guarda los ajustes del juego
@Entity(tableName = "settings")
data class SettingsEntity(
    @PrimaryKey val id: Int = 1,
    val playerName: String = "",
    val difficulty: String = Difficulty.NORMAL.name,
    val soundEnabled: Boolean = true
)