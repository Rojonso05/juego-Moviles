package com.example.practica_1_moviles.data

enum class Difficulty(
    val label: String,
    val timeLimitMs: Long,
    val multiplier: Float
) {
    EASY("Fácil", 15_000L, 1.0f),
    NORMAL("Normal", 10_000L, 1.5f),
    HARD("Difícil", 6_000L, 2.0f);

    companion object {
        // Convierte el texto guardado en la base de datos en el nivel correspondiente
        fun fromName(name: String): Difficulty =
            entries.firstOrNull { it.name == name } ?: NORMAL
    }
}