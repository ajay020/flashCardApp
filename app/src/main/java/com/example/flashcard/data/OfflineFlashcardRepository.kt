package com.example.flashcard.data

import com.example.flashcard.model.Category
import com.example.flashcard.model.Flashcard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class OfflineFlashcardRepository (
    private val flashcardDao: FlashcardDao,
    private val categoryDao: CategoryDao
): FlashcardRepository {
    override suspend fun insertCategory(category: Category): Boolean {
        return withContext(Dispatchers.IO) {
            if (categoryDao.isCategoryNameExists(category.name)) {
                false
            } else {
                categoryDao.insert(category)
                true
            }
        }
    }

    override suspend fun updateCategory(category: Category): Boolean {

        return withContext(Dispatchers.IO) {
            if (categoryDao.isCategoryNameExists(category.name)) {
                false
            } else {
                categoryDao.updateCategory(category)
                true
            }
        }
    }

    override suspend fun deleteCategory(category: Category) {
        categoryDao.deleteCategory(category)
    }

    override fun getAllCategoriesStream(): Flow<List<Category>> {
        return categoryDao.getAllCategories()
    }

    override  fun isCategoryNameExists(name:String) = categoryDao.isCategoryNameExists(name)


    override fun getCategoryStream(category: Category): Flow<Category> {
        return categoryDao.getCategoryStream(category.id)
    }

    // ========================= Flash card =================================

    override suspend fun insertFlashcard(flashcard: Flashcard) {
        flashcardDao.insert(flashcard)
    }

    override suspend fun updateFlashcard(flashcard: Flashcard): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFlashcard(flashcard: Flashcard) {
        TODO("Not yet implemented")
    }

    override fun getAllFlashcardsStream(): Flow<List<Flashcard>> = flashcardDao.getAllFlashcards()
    override fun getFlashcardsByCategoryStream(categoryId: Int): Flow<List<Flashcard>> {
      return flashcardDao.getFlashcardsByCategoryId(categoryId)
    }
}