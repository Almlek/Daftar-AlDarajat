package com.alborihi.myapp.presentation.ui.screens.analytics

import com.alborihi.myapp.domain.model.Student

data class AnalyticsState(
    val topStudents: List<Student> = emptyList(),
    val averagePercentage: Double = 0.0,
    val totalStudents: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val snackbarMessage: String? = null
)
