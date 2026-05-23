package com.example.colorlink.ui.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Home : Screen("home")
    data object LevelSelection : Screen("level_selection")
    data object Gameplay : Screen("gameplay/{levelId}") {
        fun createRoute(levelId: String) = "gameplay/$levelId"
    }
    data object DailyPuzzle : Screen("daily_puzzle")
    data object Settings : Screen("settings")
    data object Onboarding : Screen("onboarding")
}
