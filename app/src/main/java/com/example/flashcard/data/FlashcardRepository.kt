package com.example.flashcard.data

import androidx.lifecycle.LiveData
import com.example.flashcard.model.Category
import com.example.flashcard.model.Flashcard
import com.example.flashcard.ui.home.CategoryDetails

import kotlinx.coroutines.flow.Flow

interface FlashcardRepository {
    // Category
    suspend  fun insertCategory(category: Category): Boolean
    suspend fun updateCategory(category: Category): Boolean
    suspend fun deleteCategory(category: Category)
    fun getAllCategoriesStream(): Flow<List<Category>>
    fun isCategoryNameExists(name: String): Boolean
    fun getCategoryStream(categoryId: Int): Flow<Category>
    fun getCategoriesWithFlashcardCount(): Flow<List<CategoryDetails>>

    // ================ Flashcard =======================================
    suspend fun insertFlashcard(flashcard: Flashcard)
    suspend fun updateFlashcard(flashcard: Flashcard)
    suspend fun deleteFlashcard(flashcard: Flashcard)
    fun getAllFlashcardsStream(): Flow<List<Flashcard>>
    fun getFlashcardsByCategoryStream(categoryId: Int): Flow<List<Flashcard>>
}