package com.alborihi.myapp.domain.usecase.backup

import com.alborihi.myapp.domain.repository.AppRepository

class CreateBackup(private val repository: AppRepository) {
    suspend operator fun invoke(filePath: String): Result<String> {
        return repository.createBackup(filePath)
    }
}
