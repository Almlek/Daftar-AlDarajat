package com.alborihi.myapp.presentation.ui.screens.export

data class ExportState(
    val isExporting: Boolean = false,
    val exportPath: String? = null,
    val error: String? = null,
    val snackbarMessage: String? = null,
    val supportedFormats: List<String> = listOf("PDF", "CSV", "XLSX")
)
