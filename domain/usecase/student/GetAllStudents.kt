package com.alborihi.myapp.domain.usecase.student

import com.alborihi.myapp.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow

class GetAllStudents(private val repository: AppRepository) {
    operator fun invoke(): Flow<Result<List<com.alborihi.myapp.domain.model.Student>>> {
        return repository.getAllStudents()
    }
}
