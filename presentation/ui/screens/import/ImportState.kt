package com.alborihi.myapp.presentation.ui.screens.import

data class ImportState(
    val isImporting: Boolean = false,
    val importedCount: Int? = null,
    val error: String? = null,
    val snackbarMessage: String? = null,
    val supportedFormats: List<String> = listOf("CSV")
)
