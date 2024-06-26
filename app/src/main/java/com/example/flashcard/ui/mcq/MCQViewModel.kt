package com.example.flashcard.ui.mcq

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcard.data.FlashcardRepository
import com.example.flashcard.model.Flashcard
import com.example.flashcard.model.MultipleChoiceQuestion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MCQViewModel(
    private val flashcardRepository: FlashcardRepository
) : ViewModel() {

    private val _mcqState = MutableStateFlow(MCQUiState())
    val mcqState: StateFlow<MCQUiState> = _mcqState

    fun getMultipleChoiceQuestions(categoryId: Int) {
        viewModelScope.launch {
            flashcardRepository.getFlashcardsByCategoryStream(categoryId)
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = emptyList()
                ).map { flashcards ->
                    val mcqs = convertFlashcardsToMultipleChoiceQuestions(flashcards)
                    MCQUiState(
                        questions = mcqs,
                        currentQuestion = mcqs.firstOrNull()
                    )
                }.collect {
                    _mcqState.value = it
                }
        }
    }

    fun onAnswerSelected(option: String) {
        val isCorrect = option == _mcqState.value.currentQuestion?.answer
        val currentQuestion = _mcqState.value.currentQuestion ?: return

        val newReview = QuestionReview(
            question = currentQuestion.question,
            userAnswer = option,
            correctAnswer = currentQuestion.answer,
            isCorrect = isCorrect
        )

        _mcqState.value = _mcqState.value.copy(
            selectedAnswer = option,
            correctAnswers = if (isCorrect) _mcqState.value.correctAnswers + 1 else _mcqState.value.correctAnswers,
            incorrectAnswers = if (isCorrect) _mcqState.value.incorrectAnswers else _mcqState.value.incorrectAnswers + 1,
            questionReviews = _mcqState.value.questionReviews + newReview
        )
    }

     fun onNextQuestion() {
        val nextIndex = _mcqState.value.currentIndex + 1
        if (nextIndex < _mcqState.value.questions.size) {
            _mcqState.value = _mcqState.value.copy(
                currentIndex = nextIndex,
                selectedAnswer = null,
                currentQuestion = _mcqState.value.questions[nextIndex]
            )
        } else {
            _mcqState.value = _mcqState.value.copy(showResult = true, selectedAnswer = null)
        }
    }

    fun resetMcqState() {
        _mcqState.value = MCQUiState()
    }

    private fun convertFlashcardsToMultipleChoiceQuestions(
        flashcards: List<Flashcard>,
        numOptions: Int = 4
    ): List<MultipleChoiceQuestion> {
        return flashcards.map { flashcard ->
            val options = mutableListOf<String>()
            options.add(flashcard.answer)

            // Get other options from the same flashcards, excluding the correct answer
            val distractors = flashcards
                .filter { it.answer != flashcard.answer }
                .shuffled()
                .take(numOptions - 1)
                .map { it.answer }

            options.addAll(distractors)
            options.shuffle()

            MultipleChoiceQuestion(
                question = flashcard.question,
                answer = flashcard.answer,
                options = options
            )
        }
    }

}

data class MCQUiState(
    val questions: List<MultipleChoiceQuestion> = emptyList(),
    val currentQuestion: MultipleChoiceQuestion? = null,
    val currentIndex: Int = 0,
    val correctAnswers: Int = 0,
    val incorrectAnswers: Int = 0,
    val showResult: Boolean = false,
    val selectedAnswer: String? = null,
    val questionReviews: List<QuestionReview> = emptyList()
)

data class QuestionReview(
    val question: String,
    val userAnswer: String,
    val correctAnswer: String,
    val isCorrect: Boolean
)