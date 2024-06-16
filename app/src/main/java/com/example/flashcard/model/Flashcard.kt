package com.example.flashcard.model

data class Flashcard(
    val id: Int = 0,
    val question: String,
    val answer: String,
    val categoryId: Int
)
