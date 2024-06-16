package com.example.flashcard.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.flashcard.ui.screens.AddCardScreen
import com.example.flashcard.ui.screens.HomeScreen
import com.example.flashcard.ui.screens.ProfileScreen

enum class FlashCardNavigation(val route: String) {
    HOME("home"),
    PROFILE("profile"),
    ADDCARD(route = "addCard")
}
@Composable
fun FlashcardNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    NavHost(
        navController = navController,
        startDestination = FlashCardNavigation.HOME.route,
        modifier = modifier
    ) {
        composable(FlashCardNavigation.HOME.route) {
            HomeScreen(
                modifier = modifier
            )
        }
        composable(FlashCardNavigation.ADDCARD.route) {
            AddCardScreen(
                modifier = modifier
            )
        }
        composable(FlashCardNavigation.PROFILE.route) {
            ProfileScreen(
                modifier = modifier
            )
        }
    }
}