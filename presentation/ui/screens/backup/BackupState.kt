package com.alborihi.myapp.presentation.ui.screens.backup

data class BackupState(
    val isCreatingBackup: Boolean = false,
    val isRestoringBackup: Boolean = false,
    val backupPath: String? = null,
    val restorePath: String? = null,
    val error: String? = null,
    val snackbarMessage: String? = null
)
