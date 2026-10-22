package com.example.practica_1_moviles.ui.navigation

object Routes {
    const val HOME = "home"
    const val GAME = "game"
    const val RESULT = "result/{victory}"

    fun result(victory: Boolean) = "result/$victory"
}