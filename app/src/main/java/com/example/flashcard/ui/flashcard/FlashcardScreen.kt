package com.example.flashcard.ui.flashcard

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flashcard.R
import com.example.flashcard.model.Flashcard
import com.example.flashcard.ui.main.MainTopBar
import com.example.flashcard.ui.navigation.NavigationDestination

object FlashcardDestination : NavigationDestination {
    override val route: String = "flashcard"
    override val titleRes: Int = R.string.flashcard
    const val CategoryIdArgs = "categoryId"
    val routeWithArgs = "$route/{$CategoryIdArgs}"
}

@Composable
fun FlashcardScreen(
    categoryId: Int,
    modifier: Modifier = Modifier,
    viewModel: FlashcardViewModel,
    onNavigateUp: () -> Unit,
    onNavigateToFlashcardResultScreen: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(categoryId) {
//        Log.d("FlashcardScreen", "LaunchedEffect triggered with categoryId: $categoryId")
        viewModel.getFlashcardsByCategory(categoryId)
    }

    LaunchedEffect(uiState.canNavigateToResultScreen){
        if (uiState.canNavigateToResultScreen) {
//            Log.d(
//                "FlashcardScreen",
//                "Navigating to result screen: currentIndex=${uiState.currentIndex}"
//            )
            onNavigateToFlashcardResultScreen()
        }
    }

    Scaffold(
        topBar = {
            MainTopBar(
                title = stringResource(id = FlashcardDestination.titleRes),
                canNavigateBack = true,
                onNavigateUp = onNavigateUp
            )

        },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Log.d("FlashcardScreen", "uiState: $uiState")

        if (uiState.loading) {
            // Display a loading indicator while the data is being loaded
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            FlashcardScreenContent(
                modifier = Modifier
                    .padding(
                        start = paddingValues.calculateStartPadding(LocalLayoutDirection.current),
                        top = paddingValues.calculateTopPadding(),
                        end = paddingValues.calculateEndPadding(LocalLayoutDirection.current),
                    )
                    .fillMaxSize(),
                flashcard = uiState.currentFlashcard,
                onCorrectAnswer = {
                    viewModel.onCorrectAnswer()
                },
                onIncorrectAnswer = {
                    viewModel.onIncorrectAnswer()
                }
            )
        }
    }
}

@Composable
fun FlashcardScreenContent(
    modifier: Modifier = Modifier,
    flashcard: Flashcard?,
    onCorrectAnswer: () -> Unit,
    onIncorrectAnswer: () -> Unit
) {
    Column(
        modifier = modifier
            .background(Color.Gray)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (flashcard != null) {
            Flashcard(
                question = flashcard.question,
                answer = flashcard.answer,
                onCorrectAnswer = onCorrectAnswer,
                onIncorrectAnswer = onIncorrectAnswer
            )
        } else {
            Text("No flashcards available")
        }
    }
}

@Composable
fun Flashcard(
    question: String,
    answer: String,
    onCorrectAnswer: () -> Unit,
    onIncorrectAnswer: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showAnswer by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .padding(
                top = 18.dp,
                bottom = 0.dp,
                start = 8.dp,
                end = 8.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .weight(4f)
                .fillMaxWidth()
                .clickable { showAnswer = !showAnswer },
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
//                    .background(Color.Red)
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (showAnswer) answer else question,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .weight(1f)
//                .background(Color.Gray)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onIncorrectAnswer() }) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    tint = Color.Red,
                    modifier = Modifier
                        .width(50.dp)
                        .height(50.dp),
                    contentDescription = null
                )
            }
            IconButton(onClick = {
                onCorrectAnswer()
            }) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    tint = Color.Green,
                    modifier = Modifier
                        .width(50.dp)
                        .height(50.dp),
                    contentDescription = null
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FlashcardPreview() {
    Flashcard(
        question = "Question",
        answer = "Answer",
        onCorrectAnswer = {},
        onIncorrectAnswer = {}
    )
}
