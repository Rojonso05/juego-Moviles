package com.example.practica_1_moviles.ui.navigation

import android.net.Uri

object Routes {
    const val HOME = "home"
    const val CATEGORY = "category"
    const val GAME = "game/{category}"
    const val RESULT = "result/{victory}"

    // Uri.encode protege los espacios y tildes ("Cultura general")
    fun game(category: String) = "game/${Uri.encode(category)}"
    fun result(victory: Boolean) = "result/$victory"
}