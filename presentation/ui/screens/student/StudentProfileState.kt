package com.alborihi.myapp.presentation.ui.screens.student

import com.alborihi.myapp.domain.model.MonthlyGrade
import com.alborihi.myapp.domain.model.Student

data class StudentProfileState(
    val student: Student? = null,
    val monthlyGrades: List<MonthlyGrade> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val snackbarMessage: String? = null,
    val isEditing: Boolean = false,
    val showDeleteConfirmation: Boolean = false
)
