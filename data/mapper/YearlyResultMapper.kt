package com.alborihi.myapp.data.mapper

import com.alborihi.myapp.data.local.entity.YearlyResultEntity
import com.alborihi.myapp.domain.model.YearlyResult

object YearlyResultMapper {
    fun Entity.toDomain(): YearlyResult {
        return YearlyResult(
            id = this.id,
            studentId = this.studentId,
            academicYear = this.academicYear,
            overallPercentage = this.overallPercentage,
            overallGradePoint = this.overallGradePoint,
            finalRank = this.finalRank,
            finalGrade = this.finalGrade,
            status = com.alborihi.myapp.domain.model.StudentStatus.valueOf(this.status),
            generatedAt = this.generatedAt
        )
    }

    fun Domain.toEntity(): YearlyResultEntity {
        return YearlyResultEntity(
            id = this.id,
            studentId = this.studentId,
            academicYear = this.academicYear,
            overallPercentage = this.overallPercentage,
            overallGradePoint = this.overallGradePoint,
            finalRank = this.finalRank,
            finalGrade = this.finalGrade,
            status = this.status.name,
            generatedAt = this.generatedAt
        )
    }
}
