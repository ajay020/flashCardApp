package com.example.flashcard

import FlashcardApp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashcard.data.UserPreferencesRepository
import com.example.flashcard.ui.theme.FlashCardTheme
import com.example.flashcard.ui.theme.ThemeViewModel

class MainActivity() : ComponentActivity() {

    private val themeViewModel: ThemeViewModel by viewModels {
        ThemeViewModel.Factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()

            FlashCardTheme(
                darkTheme = isDarkTheme
            ) {
                FlashcardApp(
                    isDarkTheme = isDarkTheme,
                    onThemeToggle = { themeViewModel.toggleTheme() }
                )
            }
        }
    }
}
