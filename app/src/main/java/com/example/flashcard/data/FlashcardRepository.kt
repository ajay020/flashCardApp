package com.example.flashcard.data

import com.example.flashcard.model.Category
import kotlinx.coroutines.flow.Flow

interface FlashcardRepository {
    // Category
    suspend  fun insertCategory(category: Category): Boolean
    suspend fun updateCategory(category: Category)
    suspend fun deleteCategory(category: Category)
    fun getAllCategoriesStream(): Flow<List<Category>>

    fun getCategoryStream(category: Category): Flow<Category>

    // Flashcard


}