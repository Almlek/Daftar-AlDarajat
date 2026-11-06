package com.alborihi.myapp.presentation.ui.screens.grades

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alborihi.myapp.domain.model.Month
import com.alborihi.myapp.domain.model.MonthlyGrade
import com.alborihi.myapp.domain.model.Student
import com.alborihi.myapp.domain.usecase.grade.GetMonthlyGrades
import com.alborihi.myapp.domain.usecase.student.GetAllStudents
import com.alborihi.myapp.presentation.ui.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GradesTableViewModel @Inject constructor(
    private val getAllStudents: GetAllStudents,
    private val getMonthlyGrades: GetMonthlyGrades
) : ViewModel() {

    private val _state = MutableStateFlow(GradesTableState())
    val state: StateFlow<GradesTableState> = _state.asStateFlow()

    init {
        loadAllGrades()
    }

    fun loadAllGrades() {
        _state.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val studentsJob = getAllStudents()
            val gradesJob = getMonthlyGrades

            // Collect students
            studentsJob.collect { studentsResult ->
                if (studentsResult is Resource.Success) {
                    updateGradesTable(studentsResult.data)
                }
            }

            // Collect grades
            gradesJob.collect { gradesResult ->
                if (gradesResult is Resource.Success) {
                    updateGradesTable(_state.value.students.map { it.first })
                }
            }
        }
    }

    private fun updateGradesTable(students: List<Student>) {
        val currentState = _state.value
        val studentIdToGrades = mutableMapOf<Long, MonthlyGrade>()

        currentState.students.forEach { (studentId, grade) ->
            studentIdToGrades[studentId] = grade
        }

        val tableData = students.map { student ->
            studentIdToGrades[student.id]?.let { grade ->
                Pair(student.name, grade)
            }
        }.filterNotNull()

        _state.update {
            it.copy(
                students = tableData,
                isLoading = false
            )
        }
    }

    fun onMonthSelected(month: Month) {
        _state.update { it.copy(selectedMonth = month, showMonthSelector = false) }
    }

    fun onTermSelected(term: Int) {
        _state.update { it.copy(selectedTerm = term, showMonthSelector = false) }
    }

    fun showMonthSelector() {
        _state.update { it.copy(showMonthSelector = true) }
    }

    fun dismissSnackbar() {
        _state.update { it.copy(snackbarMessage = null) }
    }
}
