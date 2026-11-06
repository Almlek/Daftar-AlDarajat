package com.alborihi.myapp.domain.usecase.analytics

import com.alborihi.myapp.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow

class GetDashboardStats(private val repository: AppRepository) {
    operator fun invoke(): Flow<Result<com.alborihi.myapp.domain.model.DashboardStats>> {
        return repository.getDashboardStats()
    }
}
