package com.example.flashcard.data

import com.example.flashcard.model.Category
import com.example.flashcard.model.Flashcard

object FlashcardData {
    private fun getFlashcards(): List<Flashcard> {
        return listOf(
            Flashcard(id = 1, question = "What is 2 + 2?", answer = "4", categoryId = 1),
            Flashcard(
                id = 2,
                question = "What is the chemical symbol for water?",
                answer = "H2O",
                categoryId = 2
            ),
            Flashcard(
                id = 3,
                question = "What is the square root of 16?",
                answer = "4",
                categoryId = 1
            )
        )
    }
    fun getFlashcardsByCategory(categoryId: Int): List<Flashcard> {
        return getFlashcards().filter { it.categoryId == categoryId }
    }

    fun getCategories(): List<Category> {
        val sampleCategories = listOf(
            Category(id = 1, name = "Math"),
            Category(id = 2, name = "Science")
        )
        return sampleCategories
    }
}