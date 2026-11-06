package com.alborihi.myapp.presentation.ui.screens.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alborihi.myapp.domain.model.MonthlyGrade
import com.alborihi.myapp.domain.model.Student
import com.alborihi.myapp.domain.usecase.grade.GetMonthlyGrades
import com.alborihi.myapp.domain.usecase.student.DeleteStudent
import com.alborihi.myapp.domain.usecase.student.GetStudentById
import com.alborihi.myapp.presentation.ui.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentProfileViewModel @Inject constructor(
    private val getStudentById: GetStudentById,
    private val getMonthlyGrades: GetMonthlyGrades,
    private val deleteStudent: DeleteStudent
) : ViewModel() {

    private val _state = MutableStateFlow(StudentProfileState())
    val state: StateFlow<StudentProfileState> = _state.asStateFlow()

    fun loadStudentProfile(studentId: Long) {
        _state.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            val studentJob = getStudentById(studentId)
            val gradesJob = getMonthlyGrades(studentId)

            studentJob.collect { studentResult ->
                if (studentResult is Resource.Success) {
                    studentResult.data?.let { student ->
                        _state.update {
                            it.copy(
                                student = student,
                                isLoading = false
                            )
                        }
                    }
                } else if (studentResult is Resource.Error) {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = studentResult.exception.message ?: "حدث خطأ"
                        )
                    }
                }
            }

            gradesJob.collect { gradesResult ->
                if (gradesResult is Resource.Success) {
                    _state.update {
                        it.copy(
                            monthlyGrades = gradesResult.data
                        )
                    }
                }
            }
        }
    }

    fun deleteStudent() {
        val student = _state.value.student ?: return

        viewModelScope.launch {
            val result = deleteStudent(student.id)

            result.fold(
                onSuccess = {
                    _state.update {
                        it.copy(
                            showDeleteConfirmation = false,
                            snackbarMessage = "تم حذف الطالب بنجاح"
                        )
                    }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(
                            error = error.message ?: "حدث خطأ أثناء الحذف"
                        )
                    }
                }
            )
        }
    }

    fun toggleDeleteConfirmation(show: Boolean) {
        _state.update { it.copy(showDeleteConfirmation = show) }
    }

    fun dismissSnackbar() {
        _state.update { it.copy(snackbarMessage = null) }
    }
}
