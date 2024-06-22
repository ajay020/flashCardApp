package com.example.flashcard

import AddCardViewModel
import MainScreenViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flashcard.ui.card.CardListViewModel
import com.example.flashcard.ui.home.HomeViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for FlashCardViewModel
        initializer {
            MainScreenViewModel(flashcardApplication().container.flashcardRepository)
        }
        initializer {
            HomeViewModel(flashcardApplication().container.flashcardRepository)
        }
        initializer {
            AddCardViewModel(
                flashcardApplication().container.flashcardRepository
            )
        }
        initializer {
            CardListViewModel(flashcardApplication().container.flashcardRepository)
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [FlashCardApplication].
 */
fun CreationExtras.flashcardApplication(): FlashcardApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as FlashcardApplication)