package com.example.flashcard.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flashcard.FlashcardApplication
import com.example.flashcard.data.ReminderRepository
import com.example.flashcard.data.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val reminderRepository: ReminderRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _isReminderEnabled = MutableStateFlow(false)
    val isReminderEnabled: StateFlow<Boolean> get() = _isReminderEnabled

    init {
        viewModelScope.launch {
            userPreferencesRepository.isReminderSet.collect { isReminderEnabled ->
                _isReminderEnabled.value = isReminderEnabled
            }
        }
    }

    fun toggleReminder() {
        viewModelScope.launch {
            val newReminderState = !_isReminderEnabled.value
            _isReminderEnabled.value = newReminderState
            userPreferencesRepository.saveReminderPreference(newReminderState)

            if (newReminderState) {
                // Schedule reminder
                reminderRepository.scheduleDailyReminder()
            } else {
                // Cancel reminder
                reminderRepository.cancelWork()
            }
        }
    }

    /**
     * Factory for SettingsViewModel that takes ReminderRepository and UserPreferencesRepository as a dependency
     *
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as FlashcardApplication)
                SettingsViewModel(
                    userPreferencesRepository = application.userPreferencesRepository,
                    reminderRepository = application.container.workManagerReminderRepository
                )
            }
        }
    }
}

