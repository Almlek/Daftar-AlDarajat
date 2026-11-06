package com.alborihi.myapp.presentation.ui.screens.grades

import com.alborihi.myapp.domain.model.Month
import com.alborihi.myapp.domain.model.MonthlyGrade
import com.alborihi.myapp.domain.model.Student

data class GradeEntryState(
    val students: List<Student> = emptyList(),
    val selectedStudent: Student? = null,
    val selectedMonth: Month = Month.JANUARY,
    val selectedTerm: Int = 1,
    val subjects: Map<String, Double> = emptyMap(),
    val monthlyGrade: MonthlyGrade? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val snackbarMessage: String? = null
)
