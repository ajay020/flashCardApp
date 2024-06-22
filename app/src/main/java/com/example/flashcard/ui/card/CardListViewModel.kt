package com.example.flashcard.ui.card

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcard.data.FlashcardRepository
import com.example.flashcard.model.Flashcard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CardListViewModel(
    private val flashcardRepository: FlashcardRepository
) : ViewModel() {

    private val _categoryId = MutableStateFlow(0)
    private val _cardsUiState = MutableStateFlow(CardUiState())
    val cardsUiState: StateFlow<CardUiState> = _cardsUiState

    init {
        viewModelScope.launch {
            _categoryId.collectLatest { categoryId ->
                val cardsFlow = flashcardRepository.getFlashcardsByCategoryStream(categoryId)
                cardsFlow.map { flashcards ->
                    CardUiState(flashcards)
                }.collect { state ->
                    _cardsUiState.value = state
                }
            }
        }
    }

    fun setCategoryId(categoryId: Int) {
        if (categoryId != _categoryId.value) {
            _categoryId.value = categoryId
        }
    }
}

data class CardUiState(
    val flashcards: List<Flashcard> = emptyList()
)