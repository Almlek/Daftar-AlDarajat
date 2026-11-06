package com.alborihi.myapp.presentation.ui.screens.students

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alborihi.myapp.domain.model.Student
import com.alborihi.myapp.domain.usecase.student.UpdateStudent
import com.alborihi.myapp.domain.usecase.student.AddStudent
import com.alborihi.myapp.presentation.ui.util.Resource
import com.alborihi.myapp.presentation.ui.util.Validators
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class StudentFormViewModel @Inject constructor(
    private val addStudent: AddStudent,
    private val updateStudent: UpdateStudent
) : ViewModel() {

    private val _state = MutableStateFlow(StudentFormState())
    val state: StateFlow<StudentFormState> = _state.asStateFlow()

    fun initializeWithStudent(student: Student) {
        _state.update {
            it.copy(
                student = student,
                name = student.name,
                className = student.className,
                classSection = student.classSection,
                parentName = student.parentName,
                parentPhone = student.parentPhone,
                address = student.address,
                notes = student.notes
            )
        }
    }

    fun onNameChange(name: String) {
        _state.update { it.copy(name = name) }
    }

    fun onClassNameChange(className: String) {
        _state.update { it.copy(className = className) }
    }

    fun onClassSectionChange(classSection: String) {
        _state.update { it.copy(classSection = classSection) }
    }

    fun onParentNameChange(parentName: String) {
        _state.update { it.copy(parentName = parentName) }
    }

    fun onParentPhoneChange(parentPhone: String) {
        _state.update { it.copy(parentPhone = parentPhone) }
    }

    fun onAddressChange(address: String) {
        _state.update { it.copy(address = address) }
    }

    fun onNotesChange(notes: String) {
        _state.update { it.copy(notes = notes) }
    }

    fun saveStudent() {
        val currentState = _state.value
        val validationErrors = validateStudent(currentState)

        if (validationErrors.isNotEmpty()) {
            _state.update { it.copy(validationErrors = validationErrors) }
            return
        }

        val student = Student(
            id = currentState.student?.id ?: 0,
            name = currentState.name,
            className = currentState.className,
            classSection = currentState.classSection,
            parentName = currentState.parentName,
            parentPhone = currentState.parentPhone,
            address = currentState.address,
            notes = currentState.notes
        )

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
                            isSubmitting = false,
                            validationErrors = emptyMap()
                        )
                    }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(
                            isSubmitting = false,
                            error = error.message ?: "حدث خطأ أثناء الحفظ"
                        )
                    }
                }
            )
        }
    }

    private fun validateStudent(state: StudentFormState): Map<String, String> {
        val errors = mutableMapOf<String, String>()
        if (!Validators.isValidName(state.name)) {
            errors["name"] = "الاسم غير صحيح (يجب أن يتكون من حرفين على الأقل)"
        }
        if (!Validators.isValidClassName(state.className)) {
            errors["className"] = "اسم الفصل غير صحيح"
        }
        if (state.parentPhone.isNotEmpty() && !Validators.isValidPhone(state.parentPhone)) {
            errors["parentPhone"] = "رقم الهاتف غير صحيح"
        }
        return errors
    }
}
