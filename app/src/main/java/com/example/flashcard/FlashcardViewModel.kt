package com.example.flashcard

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashcard.data.FlashcardRepository
import com.example.flashcard.model.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FlashcardViewModel(
    private val flashcardRepository: FlashcardRepository
) : ViewModel() {
    var flashCardUiState by mutableStateOf(FlashcardUiState())
        private set

    var showDialog by mutableStateOf(false)


    fun updateUiState(categoryDetails: CategoryDetails) {
        flashCardUiState = flashCardUiState.copy(
            categoryDetails = categoryDetails,
            isEntryValid = validateInput(categoryDetails)
        )
//        Log.d("FlashcardViewModel", "updateUiState: ${flashCardUiState.isEntryValid}")
    }

    suspend fun saveCategory() {
        if (validateInput()) {
            val isSaved = flashcardRepository.insertCategory(flashCardUiState.categoryDetails.toCategory())
            flashCardUiState = flashCardUiState.copy(isDuplicateError = !isSaved)

            // Saved category successfully, reset the form
            if(isSaved){
                resetCategory()
            }
        }
    }

    fun clearError() {
        flashCardUiState = flashCardUiState.copy(
            isDuplicateError = false
        )
        resetCategory()
    }

    private fun resetCategory() {
        val categoryDetails = flashCardUiState.categoryDetails.copy(name = "")
        flashCardUiState = flashCardUiState.copy( categoryDetails = categoryDetails)
    }

    private fun validateInput(categoryDetails: CategoryDetails = flashCardUiState.categoryDetails): Boolean {
        with(categoryDetails) {
            return name.isNotBlank()
        }
    }
}

data class FlashcardUiState(
    val categoryDetails: CategoryDetails = CategoryDetails(),
    val isEntryValid: Boolean = false,
    val isDuplicateError: Boolean  = false
)

data class CategoryDetails(
    val id: Int = 0,
    val name: String = "",
)

fun CategoryDetails.toCategory(): Category {
    return Category(
        id = id,
        name = name
    )
}

