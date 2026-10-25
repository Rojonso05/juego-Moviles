package com.example.practica_1_moviles.ui.navigation

import android.net.Uri
import com.example.practica_1_moviles.data.Difficulty

object Routes {
    const val HOME = "home"
    const val CATEGORY = "category"
    const val GAME = "game/{category}"
    const val RESULT = "result/{victory}/{score}/{category}/{difficulty}"
    const val RANKING = "ranking"
    const val SETTINGS = "settings"

    fun game(category: String) = "game/${Uri.encode(category)}"

    fun result(victory: Boolean, score: Int, category: String, difficulty: Difficulty) =
        "result/$victory/$score/${Uri.encode(category)}/${difficulty.name}"
}