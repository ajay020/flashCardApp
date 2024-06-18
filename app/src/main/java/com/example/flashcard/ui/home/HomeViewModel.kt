package com.example.flashcard.ui.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcard.data.FlashcardRepository
import com.example.flashcard.model.Category
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

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

    val homeUiState: StateFlow<HomeUiState> =
        flashcardRepository.getAllCategoriesStream().map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )

    // State for showing and hiding the dialog
    var showDialog = mutableStateOf(false)
    var selectedCategory = mutableStateOf<Category?>(null)
    var showEditCategoryDialog = mutableStateOf(false)

    fun onCategoryLongPress(category: Category) {
        selectedCategory.value = category
        showDialog.value = true
    }

    fun showEditCategoryDialog(){
        showEditCategoryDialog.value = true
    }

    fun dismissEditCategoryDialog(){
        showEditCategoryDialog.value =false
    }

    fun onDialogDismiss() {
        showDialog.value = false
    }

    suspend fun deleteCategory(category: Category){
        flashcardRepository.deleteCategory(category)
    }

    suspend fun updateCategory(category: Category){
        flashcardRepository.updateCategory(category)
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class HomeUiState(
    val categoryList: List<Category> = emptyList()
)