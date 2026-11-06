package com.alborihi.myapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "monthly_grades")
data class MonthlyGradeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val studentId: Long,
    val month: String,
    val term: Int,
    val subjectsJson: String, // JSON serialized Map<String, SubjectGradeEntity>
    val totalScore: Double = 0.0,
    val maxScore: Double = 0.0,
    val percentage: Double = 0.0,
    val gradePoint: Double = 0.0,
    val remarks: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
