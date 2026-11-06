package com.alborihi.myapp.data.mapper

import com.alborihi.myapp.data.local.entity.TermResultEntity
import com.alborihi.myapp.domain.model.TermResult

object TermResultMapper {
    fun Entity.toDomain(grades: List<com.alborihi.myapp.domain.model.MonthlyGrade> = emptyList()): TermResult {
        return TermResult(
            id = this.id,
            studentId = this.studentId,
            term = this.term,
            monthlyGrades = grades,
            totalScore = this.totalScore,
            maxScore = this.maxScore,
            percentage = this.percentage,
            gradePoint = this.gradePoint,
            rank = this.rank,
            remarks = this.remarks,
            generatedAt = this.generatedAt
        )
    }

    fun Domain.toEntity(): TermResultEntity {
        return TermResultEntity(
            id = this.id,
            studentId = this.studentId,
            term = this.term,
            totalScore = this.totalScore,
            maxScore = this.maxScore,
            percentage = this.percentage,
            gradePoint = this.gradePoint,
            rank = this.rank,
            remarks = this.remarks,
            generatedAt = this.generatedAt
        )
    }
}
