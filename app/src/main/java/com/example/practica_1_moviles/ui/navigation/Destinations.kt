package com.example.practica_1_moviles.ui.navigation

import android.net.Uri

object Routes {
    const val HOME = "home"
    const val CATEGORY = "category"
    const val GAME = "game/{category}"
    const val RESULT = "result/{victory}/{score}/{category}"

    const val RANKING = "ranking"

    fun game(category: String) = "game/${Uri.encode(category)}"

    fun result(victory: Boolean, score: Int, category: String) =
        "result/$victory/$score/${Uri.encode(category)}"
}