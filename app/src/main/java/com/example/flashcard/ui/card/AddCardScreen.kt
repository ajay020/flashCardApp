package com.example.flashcard.ui.card

import AddCardDialog
import AddCardViewModel
import CardDetails
import CardInputForm
import CardUiState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashcard.AppViewModelProvider
import com.example.flashcard.FlashCardTopAppBar
import com.example.flashcard.R
import com.example.flashcard.model.Flashcard
import com.example.flashcard.ui.navigation.NavigationDestination
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
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddCardViewModel = viewModel(factory = AppViewModelProvider.Factory),
    cardListViewModel: CardListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val flashcardsUiState by cardListViewModel.cardsUiState.collectAsState()

    LaunchedEffect(categoryId) {
        cardListViewModel.setCategoryId(categoryId)
    }

    Scaffold(
        modifier = Modifier
            .background(Color.Red),
        topBar = {
            FlashCardTopAppBar(
                title = stringResource(id = AddCardDestination.titleRes),
                canNavigateBack = true,
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
            onCardValueChange = viewModel::updateUiState,
            uiState = viewModel.cardUiState,
            flashCardsList = flashcardsUiState.flashcards,
            onSavedClick = {
                coroutineScope.launch {
                    viewModel.saveCard(categoryId)
                }
            },
            onLearnClick = { /* Handle Learn click */ },
            onTestClick = { /* Handle Test click */ },
            onMatchClick = { /* Handle Match click */ }
        )
    }
}

@Composable
fun AddCardContent(
    modifier: Modifier = Modifier,
    flashCardsList: List<Flashcard>,
    onCardValueChange: (CardDetails) -> Unit,
    uiState: CardUiState,
    onSavedClick: () -> Unit,
    onLearnClick: () -> Unit,
    onTestClick: () -> Unit,
    onMatchClick: () -> Unit
) {

    var showDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Card list header
        item {

            CardListHeader(
                onLearnClick = { /*TODO*/ },
                onTestClick = { /*TODO*/ },
                onMatchClick = { /*TODO*/ },
                onAddCardClick = { showDialog = true}
            )
        }
        // card list items
        items(flashCardsList) { flashcard ->
            CardItem(flashcard = flashcard)
        }
    }

    if (showDialog) {
        AddCardDialog(
            uiState = uiState,
            onCardValueChange = onCardValueChange,
            onDismissRequest = { showDialog = false },
            onSaveClick = {
                onSavedClick()
                showDialog = false
            }
        )
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
    AddCardContent(
        onCardValueChange = {},
        uiState = CardUiState(),
        onSavedClick = {},
        flashCardsList = flashcards,
        onLearnClick = { /* Handle Learn click */ },
        onTestClick = { /* Handle Test click */ },
        onMatchClick = { /* Handle Match click */ }
    )
}



