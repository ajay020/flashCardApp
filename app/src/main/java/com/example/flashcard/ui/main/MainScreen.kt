package com.example.flashcard.ui.main

import MainScreenViewModel
import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.compose.FlashCardTheme
import com.example.flashcard.AppViewModelProvider
import com.example.flashcard.R
import com.example.flashcard.ui.components.BottomSheet
import com.example.flashcard.ui.components.CategoryCreateDialog
import com.example.flashcard.ui.flashcard.FlashcardDestination
import com.example.flashcard.ui.home.HomeDestination
import com.example.flashcard.ui.navigation.FlashcardNavHost
import com.example.flashcard.ui.navigation.NavigationDestination
import com.example.flashcard.ui.profile.ProfileDestination
import kotlinx.coroutines.launch

object MainScreenNavigation : NavigationDestination {
    override val route: String = "flashcard"
    override val titleRes: Int = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val navController = rememberNavController()
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Get the current backstack entry to determine the current route
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            Log.d("MainScreen", "currentRoute: $currentRoute")
            if (currentRoute != "learn" && currentRoute?.startsWith(FlashcardDestination.route) == false ){
                FlashCardBottomAppBar(
                    navController = navController,
                    modifier = modifier,
                    onAddClick = { showBottomSheet = true }
                )
            }

        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        FlashcardNavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController
        )

        BottomSheet(
            showCategoryDialog = { viewModel.showDialog = true },
            modifier = Modifier,
            showBottomSheet = showBottomSheet,
            coroutineScope = coroutineScope,
            sheetState = sheetState,
            hideBottomSheet = { showBottomSheet = false }
        )

        if (viewModel.showDialog) {
            CategoryCreateDialog(
                onDismiss = {
                    viewModel.showDialog = false
                    viewModel.clearError()
                },
                onConfirm = {
                    coroutineScope.launch {
                        viewModel.saveCategory()
                        if (!viewModel.flashCardUiState.isDuplicateError) {
                            viewModel.showDialog = false
                        }
                    }
                },
                onCategoryValueChange = viewModel::updateUiState,
                categoryDetails = viewModel.flashCardUiState.categoryDetails,
                isDuplicateError = viewModel.flashCardUiState.isDuplicateError
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = false,
    onNavigateUp: () -> Unit = {},
    title: String = stringResource(id = R.string.app_name),
) {
    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        title = {
            Text(
                text = title,
            )
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
        Spacer(Modifier.weight(1f, true)) // To push actions to the right
        IconButton(
            onClick = onAddClick
        ) {
            Icon(Icons.Filled.AddCircle, contentDescription = "Add")
        }
        Spacer(Modifier.weight(1f, true)) // To push actions to the right
        IconButton(onClick = { navController.navigate(ProfileDestination.route) }) {
            Icon(Icons.Filled.AccountCircle, contentDescription = "Account")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FlashCardAppPreview() {
    FlashCardTheme {
        MainScreen()
    }
}