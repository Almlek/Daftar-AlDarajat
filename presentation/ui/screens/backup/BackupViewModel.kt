package com.alborihi.myapp.presentation.ui.screens.backup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alborihi.myapp.domain.usecase.backup.CreateBackup
import com.alborihi.myapp.domain.usecase.backup.RestoreBackup
import com.alborihi.myapp.presentation.ui.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BackupViewModel @Inject constructor(
    private val createBackup: CreateBackup,
    private val restoreBackup: RestoreBackup
) : ViewModel() {

    private val _state = MutableStateFlow(BackupState())
    val state: StateFlow<BackupState> = _state.asStateFlow()

    fun createBackup(filePath: String) {
        viewModelScope.launch {
            _state.update { it.copy(isCreatingBackup = true, error = null) }

            val result = createBackup(filePath)

            _state.update { it.copy(isCreatingBackup = false) }

            result.fold(
                onSuccess = { path ->
                    _state.update {
                        it.copy(
                            isCreatingBackup = false,
                            backupPath = path,
                            snackbarMessage = "تم إنشاء النسخة الاحتياطية بنجاح"
                        )
                    }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(
                            isCreatingBackup = false,
                            error = error.message ?: "حدث خطأ أثناء إنشاء النسخة الاحتياطية"
                        )
                    }
                }
            )
        }
    }

    fun restoreBackup(filePath: String) {
        viewModelScope.launch {
            _state.update { it.copy(isRestoringBackup = true, error = null) }

            val result = restoreBackup(filePath)

            _state.update { it.copy(isRestoringBackup = false) }

            result.fold(
                onSuccess = { success ->
                    if (success) {
                        _state.update {
                            it.copy(
                                isRestoringBackup = false,
                                restorePath = filePath,
                                snackbarMessage = "تم استعادة النسخة الاحتياطية بنجاح"
                            )
                        }
                    } else {
                        _state.update {
                            it.copy(
                                isRestoringBackup = false,
                                error = "فشل استعادة النسخة الاحتياطية"
                            )
                        }
                    }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(
                            isRestoringBackup = false,
                            error = error.message ?: "حدث خطأ أثناء الاستعادة"
                        )
                    }
                }
            )
        }
    }

    fun dismissSnackbar() {
        _state.update { it.copy(snackbarMessage = null) }
    }
}
