package com.alborihi.myapp.presentation.ui.screens.grades

import com.alborihi.myapp.domain.model.Month
import com.alborihi.myapp.domain.model.MonthlyGrade

data class GradesTableState(
    val students: List<Pair<String, MonthlyGrade>> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val snackbarMessage: String? = null,
    val showMonthSelector: Boolean = false,
    val selectedMonth: Month = Month.JANUARY,
    val selectedTerm: Int = 1
)
