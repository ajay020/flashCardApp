package com.example.flashcard.ui.card

import AddCardViewModel
import CardDetails
import CardUiState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashcard.AppViewModelProvider
import com.example.flashcard.FlashCardTopAppBar
import com.example.flashcard.R
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
    viewModel: AddCardViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            FlashCardTopAppBar(
                title = stringResource(id = AddCardDestination.titleRes),
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        },

    ) {
        AddCardBody(
            uiState = viewModel.cardUiState,
            onCardValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveCard(categoryId)
                }
            },
            modifier = Modifier
                .background(Color.Yellow)
                .padding(it)
        )
    }
}


@Composable
fun AddCardBody(
    uiState: CardUiState,
    onCardValueChange: (CardDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(Color.Green),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        CardInputForm(
            modifier = Modifier,
            cardDetails = uiState.cardDetails,
            onValueChange = onCardValueChange
        )
        Button(
            onClick = onSaveClick,
            modifier = Modifier
        ) {
            Text("Save")
        }
    }
}

@Composable
fun CardInputForm(
    modifier: Modifier = Modifier,
    cardDetails: CardDetails,
    onValueChange: (CardDetails) -> Unit = {},
) {
    Column(
        modifier = modifier
            .background(Color.Magenta)
            .padding(18.dp),
        verticalArrangement = Arrangement.Top
    ) {
        TextField(
            value = cardDetails.question,
            minLines = 2,
            onValueChange = { onValueChange(cardDetails.copy(question = it)) },
            label = { Text("Front Side") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter front side") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = cardDetails.answer,
            onValueChange = { onValueChange(cardDetails.copy(answer = it)) },
            label = { Text("Back Side") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AddCardPreview() {
    AddCardBody(
        uiState = CardUiState(),
        onCardValueChange = {},
        onSaveClick = { /*TODO*/ })
}
