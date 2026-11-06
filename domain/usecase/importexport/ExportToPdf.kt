package com.alborihi.myapp.domain.usecase.importexport

import com.alborihi.myapp.domain.repository.AppRepository

class ExportToPdf(private val repository: AppRepository) {
    suspend operator fun invoke(studentId: Long?, filePath: String): Result<String> {
        return repository.exportToPdf(studentId, filePath)
    }
}
