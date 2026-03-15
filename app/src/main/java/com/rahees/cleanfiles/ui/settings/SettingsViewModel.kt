package com.rahees.cleanfiles.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahees.cleanfiles.data.PreferencesManager
import com.rahees.cleanfiles.ui.theme.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    val themeMode: StateFlow<ThemeMode> = preferencesManager.themeMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ThemeMode.SYSTEM)

    val isGridView: StateFlow<Boolean> = preferencesManager.isGridView
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val showHiddenFiles: StateFlow<Boolean> = preferencesManager.showHiddenFiles
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    suspend fun setThemeMode(mode: ThemeMode) {
        preferencesManager.setThemeMode(mode)
    }

    suspend fun setGridView(isGrid: Boolean) {
        preferencesManager.setGridView(isGrid)
    }

    suspend fun setShowHiddenFiles(show: Boolean) {
        preferencesManager.setShowHiddenFiles(show)
    }
}
