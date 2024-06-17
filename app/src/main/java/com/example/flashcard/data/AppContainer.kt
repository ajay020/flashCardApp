package com.example.flashcard.data

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val flashcardRepository: FlashcardRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineFlashcardRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    override val flashcardRepository: FlashcardRepository by lazy {
        OfflineFlashcardRepository(
            flashcardDao = FlashcardDatabase.getDatabase(context).flashcardDao(),
            categoryDao = FlashcardDatabase.getDatabase(context).categoryDao()
        )
    }
}