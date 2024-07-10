package com.example.flashcard.ui.card

import AddCardDialog
import AddCardViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashcard.AppViewModelProvider
import com.example.flashcard.ui.main.MainTopBar
import com.example.flashcard.R
import com.example.flashcard.model.Flashcard
import com.example.flashcard.ui.navigation.NavigationDestination
import com.example.flashcard.ui.theme.FlashCardTheme
import kotlinx.coroutines.launch

object AddCardDestination : NavigationDestination {
    override val route: String = "add_card"
    override val titleRes: Int = R.string.add_card
    val CategoryIdArgs = "categoryId"
    val routeWithArgs = "$route/{$CategoryIdArgs}"
}

@Composable
fun AddCardScreen(
    categoryId: Int,
    navigateToMCQ: (Int) -> Unit,
    navigateToFlashcard: (Int) -> Unit,
    onNavigateUp: () -> Unit,
    viewModel: AddCardViewModel = viewModel(factory = AppViewModelProvider.Factory),
    cardListViewModel: CardListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val flashcardsUiState by cardListViewModel.cardsUiState.collectAsState()
    val currentCategory by viewModel.currentCategory.collectAsState()

    var title by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(categoryId) {
        cardListViewModel.setCategoryId(categoryId)
        viewModel.setCategoryId(categoryId)
    }

    LaunchedEffect(currentCategory) {
        currentCategory?.let {
            title = it.name
        }
    }

    Scaffold(
        modifier = Modifier
            .background(Color.Red),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showDialog = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add)
                )
            }
        },
        topBar = {
            MainTopBar(
                title = if (title.isEmpty()) "" else title,
                canNavigateBack = true,
                showTitle = true,
                onNavigateUp = onNavigateUp
            )
        },
    ) { paddingValues ->
        AddCardContent(
            modifier = Modifier.padding(
                start = paddingValues.calculateStartPadding(LocalLayoutDirection.current),
                top = paddingValues.calculateTopPadding(),
                end = paddingValues.calculateEndPadding(LocalLayoutDirection.current),
            ),
            flashCardsList = flashcardsUiState.flashcards,
            onMCQClick = { navigateToMCQ(categoryId) },
            onMatchClick = { /* Handle Match click */ },
            onFlashcardClick = { navigateToFlashcard(categoryId) }
        )

        if (showDialog) {
            AddCardDialog(
                uiState = viewModel.cardUiState,
                onCardValueChange = viewModel::updateUiState,
                onDismissRequest = { showDialog = false },
                onSaveClick = {
                    viewModel.saveCard(categoryId)
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun AddCardContent(
    modifier: Modifier = Modifier,
    flashCardsList: List<Flashcard>,
    onMCQClick: () -> Unit,
    onMatchClick: () -> Unit,
    onFlashcardClick: () -> Unit,
) {

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Card list header
        item {
            CardListHeader(
                onMCQClick = onMCQClick,
                onMatchClick = { /*TODO*/ },
                onFlashcardClick = onFlashcardClick
            )
        }
        // card list items
        items(flashCardsList) { flashcard ->
            CardItem(flashcard = flashcard)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddCardContentPreview() {
    val flashcards = listOf(
        Flashcard(
            id = 1,
            question = "Question 1",
            answer = "Answer 1",
            categoryId = 1
        ),
        Flashcard(
            id = 2,
            question = "Question 2",
            answer = "Answer 2",
            categoryId = 1
        ),
        Flashcard(
            id = 3,
            question = "Question 3",
            answer = "Answer 3",
            categoryId = 1
        )
    )

    FlashCardTheme(
        darkTheme = false
    ) {
        AddCardContent(
            flashCardsList = flashcards,
            onMatchClick = { /* Handle Match click */ },
            onFlashcardClick = { /* Handle Flashcard click */ },
            onMCQClick = { /* Handle MCQ click */ }
        )
    }
}



