package com.alborihi.myapp.presentation.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    fun setLanguage(language: String) {
        _state.update { it.copy(language = language) }
    }

    fun toggleDarkMode(enabled: Boolean) {
        _state.update { it.copy(isDarkMode = enabled) }
    }

    fun setAcademicYear(year: String) {
        _state.update { it.copy(academicYear = year) }
    }

    fun saveSettings() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            // Placeholder for settings persistence
            _state.update { it.copy(isLoading = false, snackbarMessage = "تم حفظ الإعدادات بنجاح") }
        }
    }

    fun dismissSnackbar() {
        _state.update { it.copy(snackbarMessage = null) }
    }
}
