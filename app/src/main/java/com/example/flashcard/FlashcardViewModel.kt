package com.example.flashcard

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FlashcardViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(FlashcardUiState())
    val uiState: StateFlow<FlashcardUiState> = _uiState.asStateFlow()

    fun displayCreateCategoryDialog() {
         _uiState.value = FlashcardUiState(showCreateCategoryDialog = true)
    }
    fun hideCreateCategoryDialog(){
        _uiState.value = FlashcardUiState(showCreateCategoryDialog = false)
    }

}

data class FlashcardUiState(
    val showCreateCategoryDialog: Boolean = false,
)