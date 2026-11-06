package com.alborihi.myapp.data.mapper

import com.alborihi.myapp.data.local.entity.StudentEntity
import com.alborihi.myapp.domain.model.Student
import java.time.LocalDate

object StudentMapper {
    fun Entity.toDomain(): Student {
        return Student(
            id = this.id,
            name = this.name,
            className = this.className,
            classSection = this.classSection,
            dateOfBirth = this.dateOfBirth?.let { LocalDate.parse(it) },
            admissionDate = this.admissionDate?.let { LocalDate.parse(it) },
            parentName = this.parentName,
            parentPhone = this.parentPhone,
            address = this.address,
            notes = this.notes,
            isActive = this.isActive,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
    }

    fun Domain.toEntity(): StudentEntity {
        return StudentEntity(
            id = this.id,
            name = this.name,
            className = this.className,
            classSection = this.classSection,
            dateOfBirth = this.dateOfBirth?.toString(),
            admissionDate = this.admissionDate?.toString(),
            parentName = this.parentName,
            parentPhone = this.parentPhone,
            address = this.address,
            notes = this.notes,
            isActive = this.isActive,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
    }

    fun List<StudentEntity>.toDomainList(): List<Student> = map { it.toDomain() }
    fun List<Student>.toEntityList(): List<StudentEntity> = map { it.toEntity() }
}
