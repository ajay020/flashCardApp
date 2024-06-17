package com.example.flashcard.data

import com.example.flashcard.model.Category
import kotlinx.coroutines.flow.Flow

interface FlashcardRepository {
    // Category
    suspend  fun insertCategory(category: Category)
    suspend fun updateCategory(category: Category)
    suspend fun deleteCategory(category: Category)
    suspend  fun getAllCategoriesStream(): Flow<List<Category>>

    // Flashcard


}