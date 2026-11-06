package com.alborihi.myapp.domain.model

data class TermResult(
    val id: Long = 0,
    val studentId: Long,
    val term: Int,
    val monthlyGrades: List<MonthlyGrade>,
    val totalScore: Double = 0.0,
    val maxScore: Double = 0.0,
    val percentage: Double = 0.0,
    val gradePoint: Double = 0.0,
    val rank: Int = 0,
    val remarks: String = "",
    val generatedAt: Long = System.currentTimeMillis()
)
