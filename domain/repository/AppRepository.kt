package com.alborihi.myapp.domain.repository

import com.alborihi.myapp.domain.model.*
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    // Students
    fun getAllStudents(): Flow<Result<List<Student>>>
    fun getStudentById(id: Long): Flow<Result<Student?>>
    suspend fun addStudent(student: Student): Result<Long>
    suspend fun updateStudent(student: Student): Result<Boolean>
    suspend fun deleteStudent(id: Long): Result<Boolean>
    suspend fun searchStudents(query: String): Flow<Result<List<Student>>>

    // Monthly Grades
    fun getMonthlyGradesByStudent(studentId: Long): Flow<Result<List<MonthlyGrade>>>
    fun getAllMonthlyGrades(): Flow<Result<List<MonthlyGrade>>>
    suspend fun saveMonthlyGrade(grade: MonthlyGrade): Result<Long>
    suspend fun updateMonthlyGrade(grade: MonthlyGrade): Result<Boolean>
    suspend fun deleteMonthlyGrade(id: Long): Result<Boolean>
    suspend fun deleteMonthlyGradesByStudent(studentId: Long): Result<Boolean>

    // Term Results
    suspend fun calculateTermResult(studentId: Long, term: Int): Result<TermResult>
    fun getTermResultsByStudent(studentId: Long, term: Int): Flow<Result<List<TermResult>>>

    // Yearly Results
    suspend fun calculateYearlyResult(studentId: Long, academicYear: String): Result<YearlyResult>
    fun getYearlyResultsByYear(year: String): Flow<Result<List<YearlyResult>>>

    // Analytics
    fun getDashboardStats(): Flow<Result<DashboardStats>>
    fun getStudentProgress(studentId: Long): Flow<Result<List<MonthlyGrade>>>

    // Import / Export
    suspend fun importFromCsv(filePath: String): Result<Int>
    suspend fun importFromXlsx(filePath: String): Result<Int>
    suspend fun exportToCsv(studentId: Long?, filePath: String): Result<String>
    suspend fun exportToXlsx(studentId: Long?, filePath: String): Result<String>
    suspend fun exportToPdf(studentId: Long?, filePath: String): Result<String>

    // Backup
    suspend fun createBackup(filePath: String): Result<String>
    suspend fun restoreBackup(filePath: String): Result<Boolean>
}

data class DashboardStats(
    val totalStudents: Int = 0,
    val activeStudents: Int = 0,
    val totalGrades: Int = 0,
    val averagePercentage: Double = 0.0,
    val topStudents: List<Student> = emptyList(),
    val recentActivity: List<String> = emptyList()
)
