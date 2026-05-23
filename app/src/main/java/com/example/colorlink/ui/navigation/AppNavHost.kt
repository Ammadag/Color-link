package com.example.colorlink.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.colorlink.feature.gameplay.GameplayRoute
import com.example.colorlink.feature.home.HomeRoute
import com.example.colorlink.feature.levelselection.LevelSelectionRoute
import com.example.colorlink.feature.onboarding.OnboardingRoute
import com.example.colorlink.feature.settings.SettingsRoute
import com.example.colorlink.feature.splash.SplashRoute

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        composable(Screen.Splash.route) {
            SplashRoute(
                onNavigateToOnboarding = {
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Onboarding.route) {
            OnboardingRoute(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeRoute(
                onNavigateToGameplay = { levelId ->
                    navController.navigate(Screen.Gameplay.createRoute(levelId))
                },
                onNavigateToLevelSelection = {
                    navController.navigate(Screen.LevelSelection.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToDailyPuzzle = {
                    // Navigate to Daily Puzzle when implemented
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.LevelSelection.route) {
            LevelSelectionRoute(
                onNavigateToGameplay = { levelId ->
                    navController.navigate(Screen.Gameplay.createRoute(levelId))
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Gameplay.route) { backStackEntry ->
            val levelId = backStackEntry.arguments?.getString("levelId") ?: "level_1"
            GameplayRoute(
                levelId = levelId,
                onBack = {
                    navController.popBackStack()
                },
                onNavigateToNextLevel = {
                    // Navigate to next logic
                }
            )
        }

        composable(Screen.Settings.route) {
            SettingsRoute(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
