package com.alborihi.myapp.domain.usecase.student

import com.alborihi.myapp.domain.repository.AppRepository

class UpdateStudent(private val repository: AppRepository) {
    suspend operator fun invoke(student: com.alborihi.myapp.domain.model.Student): Result<Boolean> {
        return repository.updateStudent(student)
    }
}
