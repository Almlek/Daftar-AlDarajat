package com.alborihi.myapp.domain.model

data class YearlyResult(
    val id: Long = 0,
    val studentId: Long,
    val academicYear: String,
    val termResults: List<TermResult>,
    val overallPercentage: Double = 0.0,
    val overallGradePoint: Double = 0.0,
    val finalRank: Int = 0,
    val finalGrade: String = "",
    val status: StudentStatus = StudentStatus.PENDING,
    val generatedAt: Long = System.currentTimeMillis()
)

enum class StudentStatus {
    PENDING, PROMOTED, REPEATED, GRADED
}
