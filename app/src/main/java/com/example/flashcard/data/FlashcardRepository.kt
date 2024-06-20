package com.example.flashcard.data

import com.example.flashcard.model.Category
import com.example.flashcard.model.Flashcard
import kotlinx.coroutines.flow.Flow

interface FlashcardRepository {
    // Category
    suspend  fun insertCategory(category: Category): Boolean
    suspend fun updateCategory(category: Category): Boolean
    suspend fun deleteCategory(category: Category)
    fun getAllCategoriesStream(): Flow<List<Category>>

    fun isCategoryNameExists(name: String): Boolean

    fun getCategoryStream(category: Category): Flow<Category>

    // ================ Flashcard =======================================
    suspend fun insertFlashcard(flashcard: Flashcard)
    suspend fun updateFlashcard(flashcard: Flashcard): Boolean
    suspend fun deleteFlashcard(flashcard: Flashcard)
    fun getAllFlashcardsStream(): Flow<List<Flashcard>>


}