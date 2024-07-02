package com.example.flashcard.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.flashcard.AppViewModelProvider
import com.example.flashcard.ui.card.AddCardDestination
import com.example.flashcard.ui.card.AddCardScreen
import com.example.flashcard.ui.flashcard.FlashcardDestination
import com.example.flashcard.ui.flashcard.FlashcardResultDestination
import com.example.flashcard.ui.flashcard.FlashcardResultScreen
import com.example.flashcard.ui.flashcard.FlashcardScreen
import com.example.flashcard.ui.flashcard.FlashcardViewModel
import com.example.flashcard.ui.home.HomeDestination
import com.example.flashcard.ui.home.HomeScreen
import com.example.flashcard.ui.learn.LearnScreen
import com.example.flashcard.ui.mcq.MCQViewModel
import com.example.flashcard.ui.mcq.MCQDestination
import com.example.flashcard.ui.mcq.MCQResultDestination
import com.example.flashcard.ui.mcq.MCQResultScreen
import com.example.flashcard.ui.mcq.MultipleChoiceQuestionScreen
import com.example.flashcard.ui.profile.ProfileDestination
import com.example.flashcard.ui.profile.ProfileScreen
import com.example.flashcard.ui.settings.SettingsDestination
import com.example.flashcard.ui.settings.SettingsScreen
import com.example.flashcard.ui.settings.SettingsViewModel

@Composable
fun FlashcardNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit,
    flashcardViewmodel: FlashcardViewModel = viewModel(factory = AppViewModelProvider.Factory),
    mcqViewmodel: MCQViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(
            HomeDestination.route,
        ) {
            HomeScreen(
                navigateToAddCard = { navController.navigate("${AddCardDestination.route}/$it") },
                isDarkTheme = isDarkTheme,
                onToggleTheme = onThemeToggle,
                navigateToSettings = { navController.navigate(SettingsDestination.route) },
                onNavigateUp = { navController.navigateUp() },
            )
        }

        composable(
            route = AddCardDestination.routeWithArgs,
            arguments = listOf(navArgument(AddCardDestination.CategoryIdArgs) {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val categoryId =
                backStackEntry.arguments?.getInt(AddCardDestination.CategoryIdArgs) ?: 0
            AddCardScreen(
                categoryId = categoryId,
                navigateBack = { navController.navigateUp() },
                onNavigateUp = { navController.navigateUp() },
                navigateToMCQ = { navController.navigate("${MCQDestination.route}/$it") },
                navigateToFlashcard = { navController.navigate("${FlashcardDestination.route}/$it") },
                modifier = modifier
            )
        }

        composable(
            ProfileDestination.route,
        ) {
            ProfileScreen(
                modifier = modifier
            )
        }

        composable(
            FlashcardDestination.routeWithArgs,
            arguments = listOf(navArgument(FlashcardDestination.CategoryIdArgs) {
                type = NavType.IntType
            })
        ) { navBackStackEntry ->
            val categoryId =
                navBackStackEntry.arguments?.getInt(FlashcardDestination.CategoryIdArgs) ?: 0

            FlashcardScreen(
                categoryId = categoryId,
                viewModel = flashcardViewmodel,
                onNavigateUp = { navController.navigateUp() },
                modifier = modifier,
                onNavigateToFlashcardResultScreen = {
                    navController.navigate(FlashcardResultDestination.route)
                }
            )
        }

        composable(FlashcardResultDestination.route) {
            FlashcardResultScreen(
                viewModel = flashcardViewmodel,
                onNavigateUp = {
                    navController.popBackStack(
                        AddCardDestination.routeWithArgs,
                        inclusive = false
                    )
                },
                onBackPressed = {
                    navController.popBackStack(
                        AddCardDestination.routeWithArgs,
                        inclusive = false
                    )
                }
            )
        }

        composable(
            MCQDestination.routeWithArgs,
            arguments = listOf(navArgument(MCQDestination.CategoryIdArgs) {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val categoryId =
                backStackEntry.arguments?.getInt(MCQDestination.CategoryIdArgs)
                    ?: 0

            MultipleChoiceQuestionScreen(
                categoryId = categoryId,
                viewModel = mcqViewmodel,
                onNavigateUp = { navController.popBackStack() },
                onNavigateToResultScreen = { navController.navigate(MCQResultDestination.route) }
            )
        }

        composable(MCQResultDestination.route) {
            MCQResultScreen(
                viewModel = mcqViewmodel,
                onNavigateUp = {
                    navController.popBackStack(
                        AddCardDestination.routeWithArgs,
                        inclusive = false
                    )
                },
                onBackPressed = {
                    navController.popBackStack(
                        AddCardDestination.routeWithArgs,
                        inclusive = false
                    )
                }
            )
        }

        composable(SettingsDestination.route) {

            SettingsScreen(
                isDarkTheme = isDarkTheme,
                onThemeToggle = onThemeToggle,
                onNavigateUp = { navController.popBackStack() },
            )
        }
    }
}