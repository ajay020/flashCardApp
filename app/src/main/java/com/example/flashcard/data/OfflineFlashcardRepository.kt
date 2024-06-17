package com.example.flashcard.data

import com.example.flashcard.model.Category
import kotlinx.coroutines.flow.Flow

class OfflineFlashcardRepository (
    private val flashcardDao: FlashcardDao,
    private val categoryDao: CategoryDao
): FlashcardRepository {
    override suspend fun insertCategory(category: Category) {
        categoryDao.insert(category)
    }

    override suspend fun updateCategory(category: Category) {

    }

    override suspend fun deleteCategory(category: Category) {

    }

    override suspend fun getAllCategoriesStream(): Flow<List<Category>> {
        return categoryDao.getAllCategories()
    }

}