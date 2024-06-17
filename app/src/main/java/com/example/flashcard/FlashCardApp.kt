package com.example.flashcard

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.collectAsState
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
import androidx.navigation.compose.rememberNavController
import com.example.compose.FlashCardTheme
import com.example.flashcard.ui.components.BottomSheet
import com.example.flashcard.ui.components.CategoryCreateDialog
import com.example.flashcard.ui.navigation.FlashCardNavigation
import com.example.flashcard.ui.navigation.FlashcardNavHost

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun FlashCardApp(
    modifier: Modifier = Modifier,
    flashcardViewModel: FlashcardViewModel = viewModel()
) {
    val navController = rememberNavController()
    val uiState by flashcardViewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = { FlashCardTopAppBar() },
        bottomBar = {
            FlashCardBottomAppBar(
                navController = navController,
                modifier = modifier,
                onAddClick = { showBottomSheet = true }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        FlashcardNavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController
        )

        BottomSheet(
            showCategoryDialog = { flashcardViewModel.displayCreateCategoryDialog() },
            modifier = Modifier,
            showBottomSheet = showBottomSheet,
            coroutineScope = coroutineScope,
            sheetState = sheetState,
            hideBottomSheet = { showBottomSheet = false }
        )

        if (uiState.showCreateCategoryDialog) {
            CategoryCreateDialog(
                onDismiss = { flashcardViewModel.hideCreateCategoryDialog() },
                onConfirm = { }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashCardTopAppBar(modifier: Modifier = Modifier) {
    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
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
            onClick = { navController.navigate(FlashCardNavigation.HOME.route) },
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
        IconButton(onClick = { navController.navigate(FlashCardNavigation.PROFILE.route) }) {
            Icon(Icons.Filled.AccountCircle, contentDescription = "Account")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FlashCardAppPreview() {
    FlashCardTheme {
        FlashCardApp()
    }
}