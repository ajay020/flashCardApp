package com.example.flashcard.ui.flashcard

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flashcard.R
import com.example.flashcard.ui.main.MainTopBar
import com.example.flashcard.ui.navigation.NavigationDestination

object FlashcardResultDestination : NavigationDestination {
    override val route: String = "flashcard_result"
    override val titleRes: Int = R.string.flashcard_result
}

@Composable
fun FlashcardResultScreen(
    viewModel: FlashcardViewModel,
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    BackHandler {
        onBackPressed()
        viewModel.resetUiState()
    }

    Scaffold(
        topBar = {
            FlashcardTopBar(
                title = stringResource(FlashcardResultDestination.titleRes),
                onClose = {
                    viewModel.resetUiState()
                    onBackPressed()
                },
                canClose = true
            )
        }
    ) {
        ResultScreenContent(
            modifier = modifier.padding(it),
            correctAnswers = uiState.correctAnswers,
            incorrectAnswers = uiState.incorrectAnswers
        )
    }
}

@Composable
fun ResultScreenContent(
    modifier: Modifier = Modifier,
    correctAnswers: Int = 0,
    incorrectAnswers: Int = 0,
) {

    val totalAnswers = correctAnswers + incorrectAnswers
    val correctProgress = if (totalAnswers > 0) correctAnswers.toFloat() / totalAnswers else 0f
    val incorrectProgress = if (totalAnswers > 0) incorrectAnswers.toFloat() / totalAnswers else 0f

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Donut Chart
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                progress = { 1f },
                modifier = Modifier.size(150.dp),
                color = Color.Red,
                strokeWidth = 12.dp,
            )
            CircularProgressIndicator(
                progress = { correctProgress },
                modifier = Modifier.size(150.dp),
                color = Color.Green,
                strokeWidth = 12.dp,
            )

        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Correct Answers: ${correctAnswers}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "Incorrect Answers: ${incorrectAnswers}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FlashcardResultPreview() {
    ResultScreenContent(
        correctAnswers = 1,
        incorrectAnswers = 3
    )
}
