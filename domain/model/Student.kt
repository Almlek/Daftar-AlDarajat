package com.alborihi.myapp.domain.model

import java.time.LocalDate

data class Student(
    val id: Long = 0,
    val name: String,
    val className: String,
    val classSection: String = "",
    val dateOfBirth: LocalDate? = null,
    val admissionDate: LocalDate? = null,
    val parentName: String = "",
    val parentPhone: String = "",
    val address: String = "",
    val notes: String = "",
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    val fullName: String get() = name
    val age: Int? by lazy {
        dateOfBirth?.let { calculateAge(it) }
    }

    private fun calculateAge(dateOfBirth: LocalDate): Int {
        val today = LocalDate.now()
        var age = today.year - dateOfBirth.year
        if (today.monthValue < dateOfBirth.monthValue ||
            (today.monthValue == dateOfBirth.monthValue && today.dayOfMonth < dateOfBirth.dayOfMonth)
        ) {
            age--
        }
        return age
    }

    fun copy(
        name: String = this.name,
        className: String = this.className,
        classSection: String = this.classSection,
        dateOfBirth: LocalDate? = this.dateOfBirth,
        admissionDate: LocalDate? = this.admissionDate,
        parentName: String = this.parentName,
        parentPhone: String = this.parentPhone,
        address: String = this.address,
        notes: String = this.notes,
        isActive: Boolean = this.isActive
    ): Student {
        return copy(
            id = id,
            name = name,
            className = className,
            classSection = classSection,
            dateOfBirth = dateOfBirth,
            admissionDate = admissionDate,
            parentName = parentName,
            parentPhone = parentPhone,
            address = address,
            notes = notes,
            isActive = isActive,
            updatedAt = System.currentTimeMillis()
        )
    }
}
