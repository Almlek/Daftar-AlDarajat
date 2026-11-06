package com.alborihi.myapp.domain.usecase.grade

import com.alborihi.myapp.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow

class GetMonthlyGrades(private val repository: AppRepository) {
    operator fun invoke(studentId: Long): Flow<Result<List<com.alborihi.myapp.domain.model.MonthlyGrade>>> {
        return repository.getMonthlyGradesByStudent(studentId)
    }
}
