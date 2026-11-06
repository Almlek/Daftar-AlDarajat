package com.alborihi.myapp.presentation.ui.screens.students

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alborihi.myapp.domain.model.Student
import com.alborihi.myapp.domain.usecase.student.*
import com.alborihi.myapp.presentation.ui.util.Resource
import com.alborihi.myapp.presentation.ui.util.Validators
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentsViewModel @Inject constructor(
    private val getAllStudents: GetAllStudents,
    private val addStudent: AddStudent,
    private val updateStudent: UpdateStudent,
    private val deleteStudent: DeleteStudent,
    private val getStudentById: GetStudentById
) : ViewModel() {

    private val _state = MutableStateFlow(StudentsState())
    val state: StateFlow<StudentsState> = _state.asStateFlow()

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
                                students = result.data,
                                filteredStudents = result.data.filter {
                                    it.name.contains(currentState.searchQuery, ignoreCase = true) ||
                                            it.className.contains(currentState.searchQuery, ignoreCase = true)
                                }
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

    fun onSearchQueryChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
        filterStudents(query)
    }

    private fun filterStudents(query: String) {
        val students = _state.value.students
        val filtered = students.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.className.contains(query, ignoreCase = true) ||
                    it.parentPhone.contains(query, ignoreCase = true)
        }
        _state.update { it.copy(filteredStudents = filtered) }
    }

    fun openAddForm() {
        _state.update {
            it.copy(
                isFormVisible = true,
                editingStudent = null
            )
        }
    }

    fun openEditForm(student: Student) {
        _state.update {
            it.copy(
                isFormVisible = true,
                editingStudent = student
            )
        }
    }

    fun closeForm() {
        _state.update {
            it.copy(
                isFormVisible = false,
                editingStudent = null
            )
        }
    }

    fun saveStudent(student: Student) {
        viewModelScope.launch {
            _state.update { it.copy(isSubmitting = true) }

            val result = if (student.id > 0) {
                updateStudent(student)
            } else {
                addStudent(student)
            }

            _state.update { it.copy(isSubmitting = false) }

            result.fold(
                onSuccess = {
                    _state.update {
                        it.copy(
                            isFormVisible = false,
                            editingStudent = null,
                            snackbarMessage = if (student.id > 0) "تم تحديث الطالب بنجاح" else "تم إضافة الطالب بنجاح"
                        )
                    }
                    loadStudents()
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(
                            error = error.message ?: "حدث خطأ أثناء الحفظ"
                        )
                    }
                }
            )
        }
    }

    fun deleteStudent(student: Student) {
        viewModelScope.launch {
            val result = deleteStudent(student.id)

            result.fold(
                onSuccess = {
                    _state.update {
                        it.copy(
                            snackbarMessage = "تم حذف الطالب بنجاح"
                        )
                    }
                    loadStudents()
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

    fun validateStudent(student: Student): Map<String, String> {
        val errors = mutableMapOf<String, String>()
        if (!Validators.isValidName(student.name)) {
            errors["name"] = "الاسم غير صحيح (يجب أن يتكون من حرفين على الأقل)"
        }
        if (!Validators.isValidClassName(student.className)) {
            errors["className"] = "اسم الفصل غير صحيح"
        }
        if (student.parentPhone.isNotEmpty() && !Validators.isValidPhone(student.parentPhone)) {
            errors["parentPhone"] = "رقم الهاتف غير صحيح"
        }
        return errors
    }

    fun dismissSnackbar() {
        _state.update { it.copy(snackbarMessage = null) }
    }
}
