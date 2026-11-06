package com.alborihi.myapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "term_results")
data class TermResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val studentId: Long,
    val term: Int,
    val totalScore: Double = 0.0,
    val maxScore: Double = 0.0,
    val percentage: Double = 0.0,
    val gradePoint: Double = 0.0,
    val rank: Int = 0,
    val remarks: String = "",
    val generatedAt: Long = System.currentTimeMillis()
)
