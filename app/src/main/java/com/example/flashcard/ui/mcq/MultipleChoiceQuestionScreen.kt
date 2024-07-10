package com.example.flashcard.ui.mcq

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashcard.model.MultipleChoiceQuestion
import kotlinx.coroutines.launch

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

    LaunchedEffect(uiState.showResult) {
        if (uiState.showResult) {
            onNavigateToResultScreen()
        }
    }

    Scaffold(
        topBar = {
            FlashcardTopBar(
                canClose = true,
                onClose = onNavigateUp,
                title = "${uiState.currentIndex} / ${uiState.questions.size}"
            )
        }
    ) { paddingValues ->

        if (uiState.questions.isEmpty()) {
            EmptyListMessage(
                modifier = Modifier.padding(paddingValues)
            )
        } else {
            MCQContent(
                modifier = Modifier.padding(paddingValues),
                selectedAnswer = uiState.selectedAnswer,

                onAnswerSelected = { option ->
                    viewModel.onAnswerSelected(option)
                },
                onNextQuestion = {
                    viewModel.onNextQuestion()
                },
                questions = uiState.questions
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MCQContent(
    modifier: Modifier = Modifier,
    selectedAnswer: String?,
    onAnswerSelected: (String) -> Unit,
    onNextQuestion: () -> Unit,
    questions: List<MultipleChoiceQuestion>
) {

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { questions.size }
    )
    val coroutineScope = rememberCoroutineScope()
    val currentQuestion = questions.getOrNull(pagerState.currentPage)
    val isOptionSelected = selectedAnswer != null
    val animatedAlpha by animateFloatAsState(
        targetValue = if (isOptionSelected) 1.0f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "alpha"
    )

    if (currentQuestion != null) {

        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false
        ) { page ->
            Column(
                modifier = modifier
                    .fillMaxSize()
//                    .background(Color.Gray)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                QuestionSection(
                    question = currentQuestion.question
                )
                OptionsSection(
                    options = currentQuestion.options,
                    correctAnswer = currentQuestion.answer,
                    selectedAnswer = selectedAnswer,
                    onAnswerSelected = onAnswerSelected
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (pagerState.currentPage < questions.size - 1) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(
                                    pagerState.currentPage + 1,
                                    animationSpec = tween(
                                        durationMillis = 300,
                                        easing = LinearEasing
                                    )
                                )
                            }
                        }
                        onNextQuestion()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            alpha = animatedAlpha
                        }
                        .height(48.dp),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Next")
                }
            }
        }

    } else {
        EmptyListMessage()
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
fun EmptyListMessage(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Add some cards",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun MCQContentPreview() {

    val questions = listOf(
        MultipleChoiceQuestion(
            question = "Question 1",
            answer = "Option 1",
            options = listOf("Option 1", "Option 2", "Option 3")
        ),
        MultipleChoiceQuestion(
            question = "Question 2",
            answer = "Option 2",
            options = listOf("Option 1", "Option 2", "Option 3")
        ),
        MultipleChoiceQuestion(
            question = "Question 3",
            answer = "Option 3",
            options = listOf("Option 1", "Option 2", "Option 3")
        ),
        MultipleChoiceQuestion(
            question = "Question 4",
            answer = "Option 4",
            options = listOf("Option 1", "Option 2", "Option 3")
        )
    )

    MCQContent(
        selectedAnswer = "",
        onAnswerSelected = {},
        onNextQuestion = {},
        questions = questions
    )
}

@Preview(showBackground = true)
@Composable
private fun QuestionSectionPreview() {
//    QuestionSection(question = "Question 1")
}

@Preview(showBackground = true)
@Composable
private fun OptionSelectionPreview() {
//    OptionsSection(
//        options = listOf("Option 1", "Option 2", "Option 3"),
//        onAnswerSelected = {},
//        correctAnswer = "Option 1",
//        selectedAnswer = "Option 2"
//    )
}