package com.alborihi.myapp.domain.usecase.student

import com.alborihi.myapp.domain.repository.AppRepository

class DeleteStudent(private val repository: AppRepository) {
    suspend operator fun invoke(id: Long): Result<Boolean> {
        return repository.deleteStudent(id)
    }
}
