package com.alborihi.myapp.domain.model

data class MonthlyGrade(
    val id: Long = 0,
    val studentId: Long,
    val month: Month,
    val term: Int, // 1, 2, 3
    val subjects: Map<String, SubjectGrade>,
    val totalScore: Double = 0.0,
    val maxScore: Double = 0.0,
    val percentage: Double = 0.0,
    val gradePoint: Double = 0.0,
    val remarks: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    val subjectCount: Int get() = subjects.size
    val hasGrades: Boolean get() = subjects.isNotEmpty()

    fun withRemarks(remarks: String): MonthlyGrade = copy(remarks = remarks)
}

data class SubjectGrade(
    val subjectName: String,
    val score: Double,
    val maxScore: Double = 100.0,
    val gradePoint: Double = 0.0,
    val remarks: String = "",
    val examType: ExamType = ExamType.TOTAL
)

enum class ExamType {
    TOTAL, QUIZ, ORAL, PRACTICAL
}
