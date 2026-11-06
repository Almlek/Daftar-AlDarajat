package com.alborihi.myapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "yearly_results")
data class YearlyResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val studentId: Long,
    val academicYear: String,
    val overallPercentage: Double = 0.0,
    val overallGradePoint: Double = 0.0,
    val finalRank: Int = 0,
    val finalGrade: String = "",
    val status: String = "PENDING",
    val generatedAt: Long = System.currentTimeMillis()
)
