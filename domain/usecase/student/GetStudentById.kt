package com.alborihi.myapp.domain.usecase.student

import com.alborihi.myapp.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow

class GetStudentById(private val repository: AppRepository) {
    operator fun invoke(id: Long): Flow<Result<com.alborihi.myapp.domain.model.Student?>> {
        return repository.getStudentById(id)
    }
}
