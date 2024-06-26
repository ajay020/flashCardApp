package com.example.flashcard.model

data class MultipleChoiceQuestion(
    val question: String,
    val answer: String,
    val options: List<String>
)
