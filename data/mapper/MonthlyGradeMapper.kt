package com.alborihi.myapp.data.mapper

import com.alborihi.myapp.data.local.entity.MonthlyGradeEntity
import com.alborihi.myapp.data.local.entity.SubjectGradeEntity
import com.alborihi.myapp.domain.model.Month
import com.alborihi.myapp.domain.model.MonthlyGrade
import com.alborihi.myapp.domain.model.SubjectGrade

object MonthlyGradeMapper {
    fun Entity.toDomain(): MonthlyGrade {
        val subjects = subjectsJson.toSubjectGrades()
        return MonthlyGrade(
            id = this.id,
            studentId = this.studentId,
            month = Month.fromNumber(this.month.toInt()) ?: Month.JANUARY,
            term = this.term,
            subjects = subjects,
            totalScore = this.totalScore,
            maxScore = this.maxScore,
            percentage = this.percentage,
            gradePoint = this.gradePoint,
            remarks = this.remarks,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
    }

    fun Domain.toEntity(): MonthlyGradeEntity {
        return MonthlyGradeEntity(
            id = this.id,
            studentId = this.studentId,
            month = this.month.monthNumber.toString(),
            term = this.term,
            subjectsJson = this.subjects.toJson(),
            totalScore = this.totalScore,
            maxScore = this.maxScore,
            percentage = this.percentage,
            gradePoint = this.gradePoint,
            remarks = this.remarks,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
    }

    fun List<MonthlyGradeEntity>.toDomainList(): List<MonthlyGrade> = map { it.toDomain() }
    fun List<MonthlyGrade>.toEntityList(): List<MonthlyGradeEntity> = map { it.toEntity() }
}

private fun String.toSubjectGrades(): Map<String, SubjectGrade> {
    val json = this
    if (json.isEmpty()) return emptyMap()
    val map = gson.fromJson(json, object : TypeToken<Map<String, SubjectGradeEntity>>() {}.type)
    return map?.mapValues { (_, v) -> v.toDomain() } ?: emptyMap()
}

private fun Map<String, SubjectGrade>.toJson(): String {
    return gson.toJson(mapValues { (_, v) -> v.toEntity() })
}
