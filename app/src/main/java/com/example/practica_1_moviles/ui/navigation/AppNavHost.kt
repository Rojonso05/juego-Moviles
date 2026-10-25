package com.example.practica_1_moviles.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.practica_1_moviles.ui.category.CategoryScreen
import com.example.practica_1_moviles.ui.game.GameScreen
import com.example.practica_1_moviles.ui.home.HomeScreen
import com.example.practica_1_moviles.ui.ranking.RankingScreen
import com.example.practica_1_moviles.ui.result.ResultScreen
import com.example.practica_1_moviles.ui.settings.SettingsScreen

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
        modifier = modifier
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                onPlay = { navController.navigate(Routes.CATEGORY) },
                onRanking = { navController.navigate(Routes.RANKING) },
                onSettings = { navController.navigate(Routes.SETTINGS) }
            )
        }

        composable(Routes.CATEGORY) {
            CategoryScreen(
                onCategorySelected = { category ->
                    navController.navigate(Routes.game(category))
                }
            )
        }

        composable(Routes.RANKING) {
            RankingScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }

        composable(
            route = Routes.GAME,
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { entry ->
            val category = Uri.decode(entry.arguments?.getString("category") ?: "")
            GameScreen(
                onFinish = { victory, score, difficulty ->
                    navController.navigate(Routes.result(victory, score, category, difficulty)) {
                        popUpTo(Routes.HOME)
                    }
                }
            )
        }

        composable(
            route = Routes.RESULT,
            arguments = listOf(
                navArgument("victory") { type = NavType.BoolType },
                navArgument("score") { type = NavType.IntType },
                navArgument("category") { type = NavType.StringType },
                navArgument("difficulty") { type = NavType.StringType }
            )
        ) { entry ->
            val victory = entry.arguments?.getBoolean("victory") ?: false
            val score = entry.arguments?.getInt("score") ?: 0
            val category = Uri.decode(entry.arguments?.getString("category") ?: "")
            val difficulty = entry.arguments?.getString("difficulty") ?: ""
            ResultScreen(
                victory = victory,
                score = score,
                category = category,
                difficulty = difficulty,
                onPlayAgain = {
                    navController.navigate(Routes.CATEGORY) { popUpTo(Routes.HOME) }
                },
                onHome = { navController.popBackStack(Routes.HOME, inclusive = false) },
                onRanking = { navController.navigate(Routes.RANKING) }
            )
        }
    }
}