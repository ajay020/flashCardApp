package com.example.flashcard.ui.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcard.data.FlashcardRepository
import com.example.flashcard.model.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel to retrieve all categories in the Room database.
 */
class HomeViewModel(
    private val flashcardRepository: FlashcardRepository
) : ViewModel() {

    /**
     * Holds home ui state. The list of categories are retrieved from [FlashcardRepository] and mapped to
     * [HomeUiState]
     */

    val categoryListFlow: StateFlow<List<Category>> =
        flashcardRepository.getAllCategoriesStream()
            .map {
                it
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = emptyList()
            )
    var homeUiState by mutableStateOf(HomeUiState())
        private set

    var showDialog by mutableStateOf(false)

    suspend fun deleteCategory(category: Category) {
        flashcardRepository.deleteCategory(category)
    }

    suspend fun updateCategory(category: Category) {
        Log.d("HomeViewModel", "Updating category: $category")
        flashcardRepository.updateCategory(category)
    }

    fun updateUiState(categoryDetails: CategoryDetails) {
        homeUiState = homeUiState.copy(
            categoryDetails = categoryDetails,
            isEntryValid = validateInput(categoryDetails)
        )
    }

    suspend fun saveCategory() {
        if (validateInput()) {
            val isSaved =
                flashcardRepository.insertCategory(homeUiState.categoryDetails.toCategory())
            homeUiState = homeUiState.copy(isDuplicateError = !isSaved)

            // Saved category successfully, reset the form
            if (isSaved) {
                resetCategory()
            }
        }
    }

    fun clearError() {
        homeUiState = homeUiState.copy(
            isDuplicateError = false
        )
        resetCategory()
    }

    private fun resetCategory() {
        val categoryDetails = homeUiState.categoryDetails.copy(name = "")
        homeUiState = homeUiState.copy(categoryDetails = categoryDetails)
    }

    private fun validateInput(categoryDetails: CategoryDetails = homeUiState.categoryDetails): Boolean {
        with(categoryDetails) {
            return name.isNotBlank()
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class HomeUiState(
    val categoryDetails: CategoryDetails = CategoryDetails(),
    val isEntryValid: Boolean = false,
    val isDuplicateError: Boolean = false
)


data class CategoryDetails(
    val id: Int = 0,
    val name: String = "",
)

fun CategoryDetails.toCategory(): Category {
    return Category(
        id = id,
        name = name.trim()
    )
}