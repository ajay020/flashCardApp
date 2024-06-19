package com.example.flashcard.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.flashcard.model.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(category: Category)

    @Query("SELECT * FROM categories")
    fun getAllCategories(): Flow<List<Category>>

    @Query("SELECT * FROM categories WHERE id = :id")
    fun getCategoryStream(id: Int): Flow<Category>

    @Query("SELECT EXISTS(SELECT 1 FROM categories WHERE Lower(name) =  Lower(:name))")
    fun isCategoryNameExists(name: String): Boolean

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)
}