package com.example.flashcard.ui.flashcard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcard.data.FlashcardRepository
import com.example.flashcard.model.Flashcard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FlashcardViewModel(
    private val flashcardRepository: FlashcardRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FlashcardUiState())
    val uiState: StateFlow<FlashcardUiState> = _uiState

    fun onCorrectAnswer() {
        _uiState.value = _uiState.value.copy(
            correctAnswers = _uiState.value.correctAnswers + 1
        )
        onNextFlashcard()
    }

    fun onIncorrectAnswer() {
//        Log.d("FlashcardViewModel", "Incorrect answer")
        _uiState.value = _uiState.value.copy(
            incorrectAnswers = _uiState.value.incorrectAnswers + 1
        )
        onNextFlashcard()
    }

    private fun onNextFlashcard() {
        val newIndex = _uiState.value.currentIndex + 1
//        Log.d("FlashcardViewModel", "Next flashcard index: $newIndex")

        if (newIndex >= _uiState.value.flashcards.size) {
            // Set flag for navigation to result screen
            _uiState.value = _uiState.value.copy(
                currentIndex = newIndex,
                canNavigateToResultScreen = true
            )
        } else {
            _uiState.value = _uiState.value.copy(
                currentIndex = newIndex,
                currentFlashcard = _uiState.value.flashcards[newIndex]
            )
        }
    }

    fun resetUiState() {
        _uiState.value = FlashcardUiState() // Reset to initial state
    }

    fun getFlashcardsByCategory(categoryId: Int) {
        viewModelScope.launch {
            Log.d("FlashcardViewModel", "Fetching flashcards for category: $categoryId")
            _uiState.value = _uiState.value.copy(loading = true)
            flashcardRepository.getFlashcardsByCategoryStream(categoryId)
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = emptyList()
                ).map { flashcards ->
                    Log.d("FlashcardViewModel", "New state1: ${flashcards.size}")
                    _uiState.value.copy(
                        flashcards = flashcards,
                        currentFlashcard = flashcards.getOrNull(0),
                        loading = false
                    )
                }.collect { newState ->
                    _uiState.value = newState
                    Log.d("FlashcardViewModel", "New state: $newState")
                }
        }
    }
}

data class FlashcardUiState(
    val currentFlashcard: Flashcard? = null,
    val correctAnswers: Int = 0,
    val incorrectAnswers: Int = 0,
    val loading: Boolean = true,
    val currentIndex: Int = 0,
    val flashcards: List<Flashcard> = emptyList(),
    val canNavigateToResultScreen: Boolean = false,
)
