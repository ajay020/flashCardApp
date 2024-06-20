package com.example.flashcard.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.flashcard.ui.card.AddCardDestination
import com.example.flashcard.ui.card.AddCardScreen
import com.example.flashcard.ui.home.HomeDestination
import com.example.flashcard.ui.home.HomeScreen
import com.example.flashcard.ui.profile.ProfileScreen


@Composable
fun FlashcardNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
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
                navigateToAddCard = { navController.navigate( "${AddCardDestination.route}/$it" ) },
                modifier = modifier,
                navController = navController
            )
        }
        composable(
            route =  AddCardDestination.routeWithArgs,
            arguments = listOf( navArgument(AddCardDestination.CategoryIdArgs){ type = NavType.IntType } )
        ) {backStackEntry ->
            val categoryId = backStackEntry.arguments?.getInt(AddCardDestination.CategoryIdArgs) ?: 0

            AddCardScreen(
                categoryId = categoryId,
                navigateBack = { navController.navigateUp() },
                onNavigateUp = { navController.navigateUp() },
                modifier = modifier
            )
        }
        composable(NavRoutes.Profile) {
            ProfileScreen(
                modifier = modifier
            )
        }
    }
}