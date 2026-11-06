package com.alborihi.myapp.presentation.ui.screens.students

import com.alborihi.myapp.domain.model.Student

data class StudentsState(
    val students: List<Student> = emptyList(),
    val filteredStudents: List<Student> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val snackbarMessage: String? = null,
    val isFormVisible: Boolean = false,
    val editingStudent: Student? = null
)
