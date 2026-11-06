package com.alborihi.myapp.presentation.ui.screens.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alborihi.myapp.domain.usecase.analytics.GetDashboardStats
import com.alborihi.myapp.presentation.ui.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val getDashboardStats: GetDashboardStats
) : ViewModel() {

    private val _state = MutableStateFlow(AnalyticsState())
    val state: StateFlow<AnalyticsState> = _state.asStateFlow()

    init {
        loadAnalytics()
    }

    fun loadAnalytics() {
        _state.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            getDashboardStats()
                .catch { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "حدث خطأ غير متوقع"
                        )
                    }
                }
                .collect { result ->
                    _state.update { currentState ->
                        when (result) {
                            is Resource.Success -> currentState.copy(
                                isLoading = false,
                                topStudents = result.data.topStudents,
                                averagePercentage = result.data.averagePercentage,
                                totalStudents = result.data.totalStudents
                            )
                            is Resource.Error -> currentState.copy(
                                isLoading = false,
                                error = result.exception.message ?: "حدث خطأ"
                            )
                            is Resource.Loading -> currentState.copy(isLoading = true)
                        }
                    }
                }
        }
    }

    fun dismissSnackbar() {
        _state.update { it.copy(snackbarMessage = null) }
    }
}
