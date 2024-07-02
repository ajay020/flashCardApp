package com.example.flashcard.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {

    val isDarkTheme: Flow<Boolean> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[IS_DARK_THEME] ?: true
        }

    val isReminderSet: Flow<Boolean> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[IS_REMINDER_SET] ?: false
        }

    private companion object {
        val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
        val IS_REMINDER_SET = booleanPreferencesKey("is_reminder_set")
        const val TAG = "UserPreferencesRepo"
    }

    suspend fun saveDarkThemePreference(isDarkTheme: Boolean) {
        dataStore.edit { preference ->
            preference[IS_DARK_THEME] = isDarkTheme
        }
    }

    suspend fun saveReminderPreference(isReminderSet: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_REMINDER_SET] = isReminderSet
        }
    }
}