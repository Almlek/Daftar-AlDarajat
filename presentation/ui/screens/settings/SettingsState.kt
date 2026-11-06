package com.alborihi.myapp.presentation.ui.screens.settings

data class SettingsState(
    val academicYear: String = "2026-2027",
    val language: String = "ar",
    val isDarkMode: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val snackbarMessage: String? = null
)
