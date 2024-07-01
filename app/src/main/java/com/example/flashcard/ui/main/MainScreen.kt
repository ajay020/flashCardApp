package com.example.flashcard.ui.main

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.flashcard.ui.theme.FlashCardTheme
import com.example.flashcard.R
import com.example.flashcard.ui.card.AddCardDestination
import com.example.flashcard.ui.flashcard.FlashcardDestination
import com.example.flashcard.ui.home.HomeDestination
import com.example.flashcard.ui.mcq.MCQDestination
import com.example.flashcard.ui.mcq.MCQResultDestination
import com.example.flashcard.ui.navigation.FlashcardNavHost
import com.example.flashcard.ui.navigation.NavigationDestination
import com.example.flashcard.ui.profile.ProfileDestination
import kotlinx.coroutines.launch

object MainScreenNavigation : NavigationDestination {
    override val route: String = "flashcard"
    override val titleRes: Int = R.string.app_name
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit
) {
    val navController = rememberNavController()
    // Get the current backstack entry to determine the current route
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
//        bottomBar = {
//            if (currentRoute?.startsWith(MCQDestination.route) == false
//                && currentRoute != AddCardDestination.routeWithArgs
//                && currentRoute != MCQResultDestination.route
//                && !currentRoute.startsWith(
//                    FlashcardDestination.route
//                )
//            ) {
//                FlashCardBottomAppBar(
//                    navController = navController,
//                    modifier = modifier,
//                    onAddClick = { showBottomSheet = true }
//                )
//            }
//
//        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        FlashcardNavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            isDarkTheme = isDarkTheme,
            onThemeToggle = onThemeToggle
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = false,
    onNavigateUp: () -> Unit = {},
    showTitle: Boolean = false,
    showThemeButton: Boolean = false,
    isDarkTheme: Boolean = false,
    onThemeToggle: () -> Unit = {},
    title: String = stringResource(id = R.string.app_name),
) {

    CenterAlignedTopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        title = {
            if (showTitle) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.displayMedium
                )
            }
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = onNavigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "back arrow"
                    )
                }
            }
        },
        actions = {
           if(showThemeButton){
               IconButton(onClick = onThemeToggle) {
                   Icon(
                       imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                       contentDescription = "Toggle Theme",
                       tint = MaterialTheme.colorScheme.onPrimary
                   )
               }
           }
        }
    )
}

@Composable
fun FlashCardBottomAppBar(
    navController: NavController,
    modifier: Modifier = Modifier,
    onAddClick: () -> Unit = {},
) {

    BottomAppBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        IconButton(
            modifier = Modifier.width(54.dp),
            onClick = { navController.navigate(HomeDestination.route) },
            enabled = true
        ) {
            Icon(
                Icons.Filled.Home, contentDescription = "Home",
                modifier = Modifier
            )
        }
//        Spacer(Modifier.weight(1f, true)) // To push actions to the right
//        IconButton(
//            onClick = onAddClick
//        ) {
//            Icon(Icons.Filled.AddCircle, contentDescription = "Add")
//        }
        Spacer(Modifier.weight(1f, true)) // To push actions to the right
        IconButton(onClick = { navController.navigate(ProfileDestination.route) }) {
            Icon(Icons.Filled.AccountCircle, contentDescription = "Account")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FlashCardAppPreview() {
    FlashCardTheme(
        darkTheme = false
    ) {
        MainTopBar(
            showTitle = true
        )
    }
}