package com.example.flashcard.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flashcard.FlashcardApplication
import com.example.flashcard.data.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThemeViewModel(
    val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> get() = _isDarkTheme

    init {
        viewModelScope.launch {
            userPreferencesRepository.isDarkTheme.collect { isDarkTheme ->
                _isDarkTheme.value = isDarkTheme
            }
        }
    }

    fun toggleTheme() {
        viewModelScope.launch {
            val newThemeState = !_isDarkTheme.value
            _isDarkTheme.value = newThemeState

            userPreferencesRepository.saveDarkThemePreference(newThemeState)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as FlashcardApplication)
                ThemeViewModel(
                    userPreferencesRepository = application.userPreferencesRepository,
                )
            }
        }
    }
}
