package com.alborihi.myapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class StudentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val className: String,
    val classSection: String = "",
    val dateOfBirth: String? = null,
    val admissionDate: String? = null,
    val parentName: String = "",
    val parentPhone: String = "",
    val address: String = "",
    val notes: String = "",
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
