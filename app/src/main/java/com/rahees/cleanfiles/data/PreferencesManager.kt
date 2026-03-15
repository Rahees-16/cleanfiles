package com.rahees.cleanfiles.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.rahees.cleanfiles.ui.theme.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val THEME_MODE = intPreferencesKey("theme_mode")
        private val IS_GRID_VIEW = booleanPreferencesKey("is_grid_view")
        private val SHOW_HIDDEN_FILES = booleanPreferencesKey("show_hidden_files")
    }

    val themeMode: Flow<ThemeMode> = dataStore.data.map { preferences ->
        when (preferences[THEME_MODE] ?: 0) {
            1 -> ThemeMode.LIGHT
            2 -> ThemeMode.DARK
            else -> ThemeMode.SYSTEM
        }
    }

    val isGridView: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[IS_GRID_VIEW] ?: false
    }

    val showHiddenFiles: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[SHOW_HIDDEN_FILES] ?: false
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        dataStore.edit { preferences ->
            preferences[THEME_MODE] = when (mode) {
                ThemeMode.SYSTEM -> 0
                ThemeMode.LIGHT -> 1
                ThemeMode.DARK -> 2
            }
        }
    }

    suspend fun setGridView(isGrid: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_GRID_VIEW] = isGrid
        }
    }

    suspend fun setShowHiddenFiles(show: Boolean) {
        dataStore.edit { preferences ->
            preferences[SHOW_HIDDEN_FILES] = show
        }
    }
}
