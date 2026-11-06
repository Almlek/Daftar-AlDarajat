package com.alborihi.myapp.domain.usecase.student

import com.alborihi.myapp.domain.repository.AppRepository

class AddStudent(private val repository: AppRepository) {
    suspend operator fun invoke(student: com.alborihi.myapp.domain.model.Student): Result<Long> {
        return repository.addStudent(student)
    }
}
