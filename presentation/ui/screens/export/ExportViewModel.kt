package com.alborihi.myapp.presentation.ui.screens.export

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alborihi.myapp.domain.usecase.importexport.ExportToPdf
import com.alborihi.myapp.presentation.ui.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExportViewModel @Inject constructor(
    private val exportToPdf: ExportToPdf
) : ViewModel() {

    private val _state = MutableStateFlow(ExportState())
    val state: StateFlow<ExportState> = _state.asStateFlow()

    fun exportToPdf(studentId: Long?, filePath: String) {
        viewModelScope.launch {
            _state.update { it.copy(isExporting = true, error = null) }

            val result = exportToPdf(studentId, filePath)

            _state.update { it.copy(isExporting = false) }

            result.fold(
                onSuccess = { path ->
                    _state.update {
                        it.copy(
                            isExporting = false,
                            exportPath = path,
                            snackbarMessage = "تم التصدير بنجاح إلى $path"
                        )
                    }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(
                            isExporting = false,
                            error = error.message ?: "حدث خطأ أثناء التصدير"
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
