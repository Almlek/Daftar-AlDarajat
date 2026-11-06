package com.alborihi.myapp.presentation.ui.screens.reports

data class ReportsState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val snackbarMessage: String? = null
)
