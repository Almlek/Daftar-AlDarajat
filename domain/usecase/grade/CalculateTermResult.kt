package com.alborihi.myapp.domain.usecase.grade

import com.alborihi.myapp.domain.model.MonthlyGrade
import com.alborihi.myapp.domain.repository.AppRepository

class CalculateTermResult(private val repository: AppRepository) {
    suspend operator fun invoke(studentId: Long, term: Int): Result<com.alborihi.myapp.domain.model.TermResult> {
        return repository.calculateTermResult(studentId, term)
    }
}
