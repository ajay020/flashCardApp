package com.example.flashcard

import FlashcardApp
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.flashcard.ui.theme.FlashCardTheme
import com.example.flashcard.ui.theme.ThemeViewModel

class MainActivity : ComponentActivity() {
    private val themeViewModel by viewModels<ThemeViewModel>()
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