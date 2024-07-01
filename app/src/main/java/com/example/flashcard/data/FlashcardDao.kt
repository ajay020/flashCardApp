package com.example.flashcard.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.flashcard.model.Flashcard
import com.example.flashcard.ui.home.CategoryDetails

import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(flashcard: Flashcard)

    @Update
    suspend fun update(flashcard: Flashcard)

    @Delete
    suspend fun delete(flashcard: Flashcard)

    @Query("SELECT * FROM flashcards")
    fun getAllFlashcards(): Flow<List<Flashcard>>

    @Query("SELECT * FROM flashcards WHERE categoryId = :categoryId ORDER BY id DESC")
    fun getFlashcardsByCategoryId(categoryId: Int): Flow<List<Flashcard>>

    @Query("""
        SELECT c.id, c.name, COUNT(f.id) as flashcardCount 
        FROM categories c 
        LEFT JOIN flashcards f ON c.id = f.categoryId 
        GROUP BY c.id
    """)
    fun getCategoriesWithFlashcardCount(): Flow<List<CategoryDetails>>
}