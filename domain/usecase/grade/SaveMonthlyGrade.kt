package com.alborihi.myapp.domain.usecase.grade

import com.alborihi.myapp.domain.repository.AppRepository

class SaveMonthlyGrade(private val repository: AppRepository) {
    suspend operator fun invoke(grade: com.alborihi.myapp.domain.model.MonthlyGrade): Result<Long> {
        return repository.saveMonthlyGrade(grade)
    }
}
