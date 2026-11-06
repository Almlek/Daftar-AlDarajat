package com.alborihi.myapp.domain.usecase.importexport

import com.alborihi.myapp.domain.repository.AppRepository

class ImportFromCsv(private val repository: AppRepository) {
    suspend operator fun invoke(filePath: String): Result<Int> {
        return repository.importFromCsv(filePath)
    }
}
