package com.alborihi.myapp.domain.usecase.backup

import com.alborihi.myapp.domain.repository.AppRepository

class RestoreBackup(private val repository: AppRepository) {
    suspend operator fun invoke(filePath: String): Result<Boolean> {
        return repository.restoreBackup(filePath)
    }
}
