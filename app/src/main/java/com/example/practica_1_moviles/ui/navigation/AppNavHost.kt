package com.example.practica_1_moviles.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.practica_1_moviles.ui.game.GameScreen
import com.example.practica_1_moviles.ui.home.HomeScreen
import com.example.practica_1_moviles.ui.result.ResultScreen

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
        modifier = modifier
    ) {
        composable(Routes.HOME) {
            HomeScreen(onPlay = { navController.navigate(Routes.GAME) })
        }

        composable(Routes.GAME) {
            GameScreen(
                onFinish = { victory ->
                    navController.navigate(Routes.result(victory)) {
                        popUpTo(Routes.HOME)
                    }
                }
            )
        }

        composable(
            route = Routes.RESULT,
            arguments = listOf(navArgument("victory") { type = NavType.BoolType })
        ) { entry ->
            val victory = entry.arguments?.getBoolean("victory") ?: false
            ResultScreen(
                victory = victory,
                onPlayAgain = {
                    navController.navigate(Routes.GAME) { popUpTo(Routes.HOME) }
                },
                onHome = { navController.popBackStack(Routes.HOME, inclusive = false) }
            )
        }
    }
}