package com.example.flashcard.ui.mcq

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.flashcard.R
import com.example.flashcard.ui.flashcard.FlashcardTopBar
import com.example.flashcard.ui.navigation.NavigationDestination

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

object MCQDestination : NavigationDestination {
    override val route = "multiple_choice_question"
    val routeWithArgs = "$route/{categoryId}"
    const val CategoryIdArgs = "categoryId"
    override val titleRes: Int = R.string.multiple_choice_questions
}

@Composable
fun MultipleChoiceQuestionScreen(
    categoryId: Int,
    viewModel: MCQViewModel,
    onNavigateUp: () -> Unit,
    onNavigateToResultScreen: () -> Unit
) {
    val uiState by viewModel.mcqState.collectAsState()

    LaunchedEffect(categoryId) {
        viewModel.getMultipleChoiceQuestions(categoryId)
    }

    Scaffold(
        topBar = {
            FlashcardTopBar(
                canClose = true,
                onClose = onNavigateUp
            )
        }
    ) { paddingValues ->
        if (uiState.showResult) {
            onNavigateToResultScreen()
        } else {
            MCQContent(
                modifier = Modifier.padding(paddingValues),
                uiState = uiState,
                onAnswerSelected = { option ->
                    viewModel.onAnswerSelected(option)
                },
                onNextQuestion = {
                    viewModel.onNextQuestion()
                }
            )
        }
    }
}

@Composable
fun MCQContent(
    modifier: Modifier = Modifier,
    uiState: MCQUiState,
    onAnswerSelected: (String) -> Unit,
    onNextQuestion: () -> Unit
) {
    val currentQuestion = uiState.currentQuestion
    if (currentQuestion != null) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            QuestionSection(
                question = currentQuestion.question
            )
            OptionsSection(
                options = currentQuestion.options,
                correctAnswer = currentQuestion.answer,
                selectedAnswer = uiState.selectedAnswer,
                onAnswerSelected = onAnswerSelected
            )

            if (uiState.selectedAnswer != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onNextQuestion,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Next")
                }
            }
        }
    } else {
        LoadingIndicator(modifier = modifier)
    }
}

@Composable
fun QuestionSection(
    question: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = question,
        textAlign = TextAlign.Center,
        fontSize = 24.sp,
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    )
}

@Composable
fun OptionsSection(
    options: List<String>,
    correctAnswer: String,
    selectedAnswer: String?,
    onAnswerSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(4.dp)
    ) {
        options.forEach { option ->
            val backgroundColor = when {
                selectedAnswer == null -> MaterialTheme.colorScheme.primary
                option == correctAnswer -> Color.Green
                option == selectedAnswer -> Color.Red
                else -> MaterialTheme.colorScheme.primary
            }

            OutlinedButton(
                onClick = { if (selectedAnswer == null) onAnswerSelected(option) },
                border = BorderStroke(1.dp, backgroundColor),
                shape = RoundedCornerShape(4.dp),

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(option)
            }
        }
    }
}


@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            strokeWidth = 4.dp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(48.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun QuestionSectionPreview() {
    QuestionSection(question = "Question 1")
}

@Preview(showBackground = true)
@Composable
private fun OptionSelectionPreview() {
    OptionsSection(
        options = listOf("Option 1", "Option 2", "Option 3"),
        onAnswerSelected = {},
        correctAnswer = "Option 1",
        selectedAnswer = "Option 2"
    )
}