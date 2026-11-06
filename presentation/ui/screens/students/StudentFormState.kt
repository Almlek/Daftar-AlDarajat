package com.alborihi.myapp.presentation.ui.screens.students

import com.alborihi.myapp.domain.model.Student

data class StudentFormState(
    val student: Student? = null,
    val name: String = "",
    val className: String = "",
    val classSection: String = "",
    val parentName: String = "",
    val parentPhone: String = "",
    val address: String = "",
    val notes: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSubmitting: Boolean = false,
    val validationErrors: Map<String, String> = emptyMap()
)
