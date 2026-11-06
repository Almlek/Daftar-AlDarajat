package com.alborihi.myapp.presentation.ui.screens.grades

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alborihi.myapp.domain.model.*
import com.alborihi.myapp.domain.usecase.grade.SaveMonthlyGrade
import com.alborihi.myapp.domain.usecase.student.GetAllStudents
import com.alborihi.myapp.presentation.ui.util.Resource
import com.alborihi.myapp.presentation.ui.util.Validators
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GradeEntryViewModel @Inject constructor(
    private val getAllStudents: GetAllStudents,
    private val saveMonthlyGrade: SaveMonthlyGrade
) : ViewModel() {

    private val _state = MutableStateFlow(GradeEntryState())
    val state: StateFlow<GradeEntryState> = _state.asStateFlow()

    init {
        loadStudents()
    }

    fun loadStudents() {
        _state.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            getAllStudents()
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
                                students = result.data
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

    fun onStudentSelected(student: Student) {
        _state.update { it.copy(selectedStudent = student) }
    }

    fun onMonthSelected(month: Month) {
        _state.update { it.copy(selectedMonth = month) }
    }

    fun onTermSelected(term: Int) {
        _state.update { it.copy(selectedTerm = term) }
    }

    fun onSubjectScoreChange(subject: String, score: Double) {
        val currentSubjects = _state.value.subjects.toMutableMap()
        currentSubjects[subject] = score
        _state.update { it.copy(subjects = currentSubjects) }
    }

    fun addSubject(subjectName: String) {
        if (subjectName.isNotBlank()) {
            val currentSubjects = _state.value.subjects.toMutableMap()
            currentSubjects[subjectName] = 0.0
            _state.update { it.copy(subjects = currentSubjects) }
        }
    }

    fun removeSubject(subject: String) {
        val currentSubjects = _state.value.subjects.toMutableMap()
        currentSubjects.remove(subject)
        _state.update { it.copy(subjects = currentSubjects) }
    }

    fun saveGrade() {
        val currentState = _state.value

        if (currentState.selectedStudent == null) {
            _state.update { it.copy(error = "الرجاء اختيار طالب") }
            return
        }

        if (currentState.subjects.isEmpty()) {
            _state.update { it.copy(error = "الرجاء إضافة مادة واحدة على الأقل") }
            return
        }

        val invalidScores = currentState.subjects.filterValues { !Validators.isValidGrade(it) }
        if (invalidScores.isNotEmpty()) {
            _state.update {
                it.copy(
                    error = "الدرجات غير صحيحة: ${invalidScores.keys.joinToString(", ")}"
                )
            }
            return
        }

        val subjects = currentState.subjects.mapValues { (name, score) ->
            SubjectGrade(subjectName = name, score = score)
        }

        val grade = MonthlyGrade(
            studentId = currentState.selectedStudent.id,
            month = currentState.selectedMonth,
            term = currentState.selectedTerm,
            subjects = subjects
        )

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, error = null) }

            val result = saveMonthlyGrade(grade)

            _state.update { it.copy(isSaving = false) }

            result.fold(
                onSuccess = {
                    _state.update {
                        it.copy(
                            isSaving = false,
                            snackbarMessage = "تم حفظ درجات $currentState.selectedMonth بنجاح"
                        )
                    }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(
                            isSaving = false,
                            error = error.message ?: "حدث خطأ أثناء الحفظ"
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
