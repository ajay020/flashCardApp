package com.example.flashcard.ui.mcq

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashcard.AppViewModelProvider
import com.example.flashcard.R
import com.example.flashcard.data.FlashcardRepository
import com.example.flashcard.ui.flashcard.FlashcardTopBar
import com.example.flashcard.ui.flashcard.FlashcardViewModel
import com.example.flashcard.ui.main.MainTopBar
import com.example.flashcard.ui.navigation.NavigationDestination

object MCQResultDestination : NavigationDestination {
    override val route: String = "mcq_result"
    override val titleRes: Int = R.string.mcq_result
}

@Composable
fun MCQResultScreen(
    viewModel: MCQViewModel,
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    val uiState by viewModel.mcqState.collectAsState()

    BackHandler {
        onBackPressed()
        viewModel.resetMcqState()
    }

    Scaffold(
        topBar = {
            FlashcardTopBar(
                onClose = {
                    viewModel.resetMcqState()
                    onBackPressed()
                },
                canClose = true
            )
        }
    ) { paddingValues ->
        ResultScreenContent(
            modifier = Modifier.padding(
                start = paddingValues.calculateStartPadding(LocalLayoutDirection.current),
                top = paddingValues.calculateTopPadding(),
                end = paddingValues.calculateEndPadding(LocalLayoutDirection.current),
            ),
            correctAnswers = uiState.correctAnswers,
            incorrectAnswers = uiState.incorrectAnswers,
            questionReviews = uiState.questionReviews
        )
    }
}

@Composable
fun ResultScreenContent(
    modifier: Modifier = Modifier,
    correctAnswers: Int = 0,
    incorrectAnswers: Int = 0,
    questionReviews: List<QuestionReview> = emptyList()
) {

    val totalAnswers = correctAnswers + incorrectAnswers
    val percentage = if (totalAnswers > 0) (correctAnswers.toFloat() / totalAnswers) * 100 else 0f
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
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 8.dp),
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                DonutChart(
                    modifier = Modifier.size(200.dp),
                    correctProgress,
                    percentage
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Correct : ${correctAnswers}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(
                    text = "Incorrect : ${incorrectAnswers}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Review Your Answers:",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            items(questionReviews) { question ->
                QuestionReviewItem(question = question)
            }
        }
    }
}

@Composable
fun DonutChart(
    modifier: Modifier = Modifier,
    correctProgress: Float,
    percentage: Float
) {
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

        Text(
            text = "${percentage.toInt().toString()}%",
            fontSize = 28.sp,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

    }
}

@Composable
fun QuestionReviewItem(question: QuestionReview) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.onBackground)
            .padding(16.dp)
    ) {
        Text(
            text = "Question: ${question.question}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Your Answer: ${question.userAnswer}",
            style = MaterialTheme.typography.bodyMedium,
            color = if (question.isCorrect) Color.Green else Color.Red,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Correct Answer: ${question.correctAnswer}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun MCQResultScreenPreview() {
    val questions = listOf(
        QuestionReview(
            question = "What is 2 + 2?",
            userAnswer = "4",
            correctAnswer = "4",
            isCorrect = true
        ),
        QuestionReview(
            question = "What is the capital of France?",
            userAnswer = "London",
            correctAnswer = "Paris",
            isCorrect = false
        )
    )
    ResultScreenContent(
        correctAnswers = 1,
        incorrectAnswers = 1,
        questionReviews = questions
    )
}
