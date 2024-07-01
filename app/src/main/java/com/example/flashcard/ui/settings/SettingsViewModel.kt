package com.example.flashcard.ui.settings

import androidx.lifecycle.ViewModel
import com.example.flashcard.data.ReminderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel (
    private val reminderRepository: ReminderRepository
): ViewModel() {
    private val _isReminderEnabled = MutableStateFlow(false)
    val isReminderEnabled: StateFlow<Boolean> get() = _isReminderEnabled

    fun toggleReminder() {
        _isReminderEnabled.value = !_isReminderEnabled.value
        if (isReminderEnabled.value) {
            // Schedule reminder
            reminderRepository.scheduleDailyReminder()
        } else {
            // Cancel reminder
            reminderRepository.cancelWork()
        }
    }
}
