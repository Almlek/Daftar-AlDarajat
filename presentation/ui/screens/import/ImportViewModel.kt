package com.alborihi.myapp.presentation.ui.screens.import

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alborihi.myapp.domain.usecase.importexport.ImportFromCsv
import com.alborihi.myapp.presentation.ui.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImportViewModel @Inject constructor(
    private val importFromCsv: ImportFromCsv
) : ViewModel() {

    private val _state = MutableStateFlow(ImportState())
    val state: StateFlow<ImportState> = _state.asStateFlow()

    fun importFromPath(filePath: String) {
        viewModelScope.launch {
            _state.update { it.copy(isImporting = true, error = null) }

            val result = importFromCsv(filePath)

            _state.update { it.copy(isImporting = false) }

            result.fold(
                onSuccess = { count ->
                    _state.update {
                        it.copy(
                            isImporting = false,
                            importedCount = count,
                            snackbarMessage = "تم استيراد $count طالب بنجاح"
                        )
                    }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(
                            isImporting = false,
                            error = error.message ?: "حدث خطأ أثناء الاستيراد"
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
