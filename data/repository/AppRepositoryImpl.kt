package com.alborihi.myapp.data.repository

import android.util.Log
import com.alborihi.myapp.data.local.dao.StudentDao
import com.alborihi.myapp.data.local.dao.TermResultDao
import com.alborihi.myapp.data.local.dao.YearlyResultDao
import com.alborihi.myapp.data.mapper.*
import com.alborihi.myapp.domain.model.*
import com.alborihi.myapp.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AppRepositoryImpl(
    private val studentDao: StudentDao,
    private val monthlyGradeDao: MonthlyGradeDao,
    private val termResultDao: TermResultDao,
    private val yearlyResultDao: YearlyResultDao
) : AppRepository {

    companion object {
        private const val TAG = "AppRepositoryImpl"
    }

    // ================= Students =================
    override fun getAllStudents(): Flow<Result<List<Student>>> {
        return studentDao.getAllStudents().map { Result.success(it.toDomainList()) }
    }

    override fun getStudentById(id: Long): Flow<Result<Student?>> {
        return studentDao.getStudentById(id).map { it?.let { s -> Result.success(s.toDomain()) } ?: Result.success(null) }
    }

    override suspend fun addStudent(student: Student): Result<Long> {
        return try {
            val id = studentDao.insertStudent(student.toEntity())
            Result.success(id)
        } catch (e: Exception) {
            Log.e(TAG, "Error adding student", e)
            Result.failure(e)
        }
    }

    override suspend fun updateStudent(student: Student): Result<Boolean> {
        return try {
            val rows = studentDao.updateStudent(student.toEntity())
            Result.success(rows > 0)
        } catch (e: Exception) {
            Log.e(TAG, "Error updating student", e)
            Result.failure(e)
        }
    }

    override suspend fun deleteStudent(id: Long): Result<Boolean> {
        return try {
            studentDao.softDeleteStudent(id)
            Result.success(true)
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting student", e)
            Result.failure(e)
        }
    }

    override fun searchStudents(query: String): Flow<Result<List<Student>>> {
        return studentDao.searchStudents("*$query*").map { Result.success(it.toDomainList()) }
    }

    // ================= Monthly Grades =================
    override fun getMonthlyGradesByStudent(studentId: Long): Flow<Result<List<MonthlyGrade>>> {
        return monthlyGradeDao.getMonthlyGradesByStudent(studentId).map { Result.success(it.toDomainList()) }
    }

    override fun getAllMonthlyGrades(): Flow<Result<List<MonthlyGrade>>> {
        return monthlyGradeDao.getAllMonthlyGrades().map { Result.success(it.toDomainList()) }
    }

    override suspend fun saveMonthlyGrade(grade: MonthlyGrade): Result<Long> {
        return try {
            val calculated = calculateGradeMetrics(grade)
            val entity = calculated.toEntity()
            val id = monthlyGradeDao.insertGrade(entity)
            Result.success(id)
        } catch (e: Exception) {
            Log.e(TAG, "Error saving grade", e)
            Result.failure(e)
        }
    }

    override suspend fun updateMonthlyGrade(grade: MonthlyGrade): Result<Boolean> {
        return try {
            val calculated = calculateGradeMetrics(grade)
            val entity = calculated.toEntity()
            val rows = monthlyGradeDao.updateGrade(entity)
            Result.success(rows > 0)
        } catch (e: Exception) {
            Log.e(TAG, "Error updating grade", e)
            Result.failure(e)
        }
    }

    override suspend fun deleteMonthlyGrade(id: Long): Result<Boolean> {
        return try {
            val grade = monthlyGradeDao.getMonthlyGrade(id) // Need to fetch first
            monthlyGradeDao.deleteGrade(grade!!)
            Result.success(true)
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting grade", e)
            Result.failure(e)
        }
    }

    override suspend fun deleteMonthlyGradesByStudent(studentId: Long): Result<Boolean> {
        return try {
            monthlyGradeDao.deleteByStudentId(studentId)
            Result.success(true)
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting grades by student", e)
            Result.failure(e)
        }
    }

    // ================= Term Results =================
    override suspend fun calculateTermResult(studentId: Long, term: Int): Result<TermResult> {
        return try {
            val grades = monthlyGradeDao.getMonthlyGradesByStudent(studentId)
                .firstOrNull { it != null && it.term == term }
                ?.let { listOf(it) }
                ?: emptyList()

            val domainGrades = grades.toDomainList()
            val (total, max, subjects) = calculateTermMetrics(domainGrades)

            val result = TermResult(
                studentId = studentId,
                term = term,
                monthlyGrades = domainGrades,
                totalScore = total,
                maxScore = max,
                percentage = if (max > 0) (total / max) * 100 else 0.0,
                gradePoint = calculateGradePoint(total / max),
                generatedAt = System.currentTimeMillis()
            )

            termResultDao.insertTermResult(result.toEntity())
            Result.success(result)
        } catch (e: Exception) {
            Log.e(TAG, "Error calculating term result", e)
            Result.failure(e)
        }
    }

    override fun getTermResultsByStudent(studentId: Long, term: Int): Flow<Result<List<TermResult>>> {
        return termResultDao.getTermResults(studentId, term).map { Result.success(it.map { r -> r.toDomain() }) }
    }

    // ================= Yearly Results =================
    override suspend fun calculateYearlyResult(studentId: Long, academicYear: String): Result<YearlyResult> {
        return try {
            val termResults = mutableListOf<com.alborihi.myapp.domain.model.TermResult>()
            for (term in 1..3) {
                calculateTermResult(studentId, term).getOrNull()?.let { termResults.add(it) }
            }

            val total = termResults.sumOf { it.totalScore }
            val max = termResults.sumOf { it.maxScore }
            val percentage = if (max > 0) (total / max) * 100 else 0.0

            val result = YearlyResult(
                studentId = studentId,
                academicYear = academicYear,
                termResults = termResults,
                overallPercentage = percentage,
                overallGradePoint = calculateGradePoint(percentage),
                finalGrade = calculateLetterGrade(percentage),
                status = determineStatus(percentage),
                generatedAt = System.currentTimeMillis()
            )

            yearlyResultDao.insertYearlyResult(result.toEntity())
            Result.success(result)
        } catch (e: Exception) {
            Log.e(TAG, "Error calculating yearly result", e)
            Result.failure(e)
        }
    }

    override fun getYearlyResultsByYear(year: String): Flow<Result<List<YearlyResult>>> {
        return yearlyResultDao.getYearlyResults(year).map { Result.success(it.map { r -> r.toDomain() }) }
    }

    // ================= Analytics =================
    override fun getDashboardStats(): Flow<Result<DashboardStats>> {
        return flow {
            try {
                val activeCount = studentDao.getActiveStudentCount().firstOrNull() ?: 0
                val totalGrades = monthlyGradeDao.getTotalGradesCount().firstOrNull() ?: 0

                val students = studentDao.getAllStudents().firstOrNull()?.toDomainList() ?: emptyList()
                val grades = monthlyGradeDao.getAllMonthlyGrades().firstOrNull()?.toDomainList() ?: emptyList()

                val avgPercentage = if (grades.isNotEmpty()) {
                    grades.averageOf { it.percentage }
                } else 0.0

                val topStudents = students.sortedByDescending { it.name }.take(5)

                emit(
                    Result.success(
                        DashboardStats(
                            totalStudents = students.size,
                            activeStudents = activeCount,
                            totalGrades = totalGrades,
                            averagePercentage = avgPercentage,
                            topStudents = topStudents,
                            recentActivity = listOf("تم إضافة طالب جديد", "تم تحديث درجات شهر يناير")
                        )
                    )
                )
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }.map { it }
    }

    override fun getStudentProgress(studentId: Long): Flow<Result<List<MonthlyGrade>>> {
        return getMonthlyGradesByStudent(studentId)
    }

    // ================= Import / Export =================
    override suspend fun importFromCsv(filePath: String): Result<Int> {
        return try {
            val file = File(filePath)
            if (!file.exists()) return Result.failure(Exception("File not found"))

            val content = file.readText()
            val lines = content.split("\n").filter { it.isNotBlank() }
            if (lines.size < 2) return Result.failure(Exception("Invalid CSV format"))

            val header = lines[0].split(",")
            var inserted = 0

            for (i in 1 until lines.size) {
                val values = lines[i].split(",")
                if (values.size != header.size) continue

                val student = Student(
                    name = values[0].trim(),
                    className = values[1].trim(),
                    classSection = if (header.size > 2) values[2].trim() else "",
                    parentName = if (header.size > 3) values[3].trim() else "",
                    parentPhone = if (header.size > 4) values[4].trim() else "",
                    address = if (header.size > 5) values[5].trim() else ""
                )
                addStudent(student)
                inserted++
            }

            Result.success(inserted)
        } catch (e: Exception) {
            Log.e(TAG, "Error importing CSV", e)
            Result.failure(e)
        }
    }

    override suspend fun importFromXlsx(filePath: String): Result<Int> {
        return Result.failure(Exception("XLSX import not yet implemented"))
    }

    override suspend fun exportToCsv(studentId: Long?, filePath: String): Result<String> {
        return try {
            val students = studentId?.let {
                getStudentById(it).firstOrNull()?.getOrNull()?.let { s -> listOf(s) }
                    ?: emptyList()
            } ?: studentDao.getAllStudents().firstOrNull()?.toDomainList() ?: emptyList()

            val lines = mutableListOf<String>()
            lines.add("الاسم,الفصل,القسم,اسم ولي الأمر,هاتف ولي الأمر,العنوان")
            for (student in students) {
                lines.add("${student.name},${student.className},${student.classSection},${student.parentName},${student.parentPhone},${student.address}")
            }

            val file = File(filePath)
            file.writeText(lines.joinToString("\n"))
            Result.success(filePath)
        } catch (e: Exception) {
            Log.e(TAG, "Error exporting CSV", e)
            Result.failure(e)
        }
    }

    override suspend fun exportToXlsx(studentId: Long?, filePath: String): Result<String> {
        return Result.failure(Exception("XLSX export not yet implemented"))
    }

    override suspend fun exportToPdf(studentId: Long?, filePath: String): Result<String> {
        return Result.failure(Exception("PDF export not yet implemented"))
    }

    // ================= Backup =================
    override suspend fun createBackup(filePath: String): Result<String> {
        return try {
            // Export entire database as JSON backup
            val students = studentDao.getAllStudents().firstOrNull()?.toDomainList() ?: emptyList()
            val grades = monthlyGradeDao.getAllMonthlyGrades().firstOrNull()?.toDomainList() ?: emptyList()

            val backup = mapOf(
                "students" to students,
                "grades" to grades,
                "timestamp" to System.currentTimeMillis()
            )

            val json = gson.toJson(backup)
            val file = File(filePath)
            file.writeText(json)

            Result.success(filePath)
        } catch (e: Exception) {
            Log.e(TAG, "Error creating backup", e)
            Result.failure(e)
        }
    }

    override suspend fun restoreBackup(filePath: String): Result<Boolean> {
        return try {
            val file = File(filePath)
            if (!file.exists()) return Result.failure(Exception("Backup file not found"))

            val json = file.readText()
            val backup = gson.fromJson(json, BackupData::class.java)

            for (student in backup.students) {
                addStudent(student)
            }
            // Restore grades
            for (grade in backup.grades) {
                saveMonthlyGrade(grade)
            }

            Result.success(true)
        } catch (e: Exception) {
            Log.e(TAG, "Error restoring backup", e)
            Result.failure(e)
        }
    }

    // ================= Helpers =================
    private fun calculateGradeMetrics(grade: MonthlyGrade): MonthlyGrade {
        var total = 0.0
        var max = 0.0
        val subjectMap = mutableMapOf<String, SubjectGrade>()

        for ((subject, sg) in grade.subjects) {
            val score = sg.score
            val maxScore = sg.maxScore
            val point = calculateGradePoint(score / maxScore)
            subjectMap[subject] = sg.copy(score = score, maxScore = maxScore, gradePoint = point)
            total += score
            max += maxScore
        }

        return grade.copy(
            subjects = subjectMap,
            totalScore = total,
            maxScore = max,
            percentage = if (max > 0) (total / max) * 100 else 0.0,
            gradePoint = calculateGradePoint(total / max)
        )
    }

    private fun calculateTermMetrics(grades: List<MonthlyGrade>): Triple<Double, Double, Map<String, SubjectGrade>> {
        var total = 0.0
        var max = 0.0
        val subjectMap = mutableMapOf<String, SubjectGrade>()

        for (grade in grades) {
            for ((subject, sg) in grade.subjects) {
                val current = subjectMap.getOrPut(subject) {
                    SubjectGrade(subject, 0.0, sg.maxScore)
                }
                current.score += sg.score
                total += sg.score
                max += sg.maxScore
            }
        }

        return Triple(total, max, subjectMap)
    }

    private fun calculateGradePoint(percentage: Double): Double {
        return when {
            percentage >= 90 -> 4.0
            percentage >= 80 -> 3.5
            percentage >= 70 -> 3.0
            percentage >= 60 -> 2.5
            percentage >= 50 -> 2.0
            percentage >= 40 -> 1.5
            else -> 0.0
        }
    }

    private fun calculateLetterGrade(percentage: Double): String {
        return when {
            percentage >= 90 -> "A"
            percentage >= 80 -> "B"
            percentage >= 70 -> "C"
            percentage >= 60 -> "D"
            percentage >= 50 -> "E"
            else -> "F"
        }
    }

    private fun determineStatus(percentage: Double): StudentStatus {
        return if (percentage >= 50) StudentStatus.PROMOTED else StudentStatus.REPEATED
    }

    private fun List<MonthlyGrade>.averageOf(selector: (MonthlyGrade) -> Double): Double {
        if (isEmpty()) return 0.0
        return sumOf { selector(it) } / size
    }
}

// Simple JSON serialization helper (use Moshi/Gson in production)
private val gson = com.google.gson.Gson()

data class BackupData(
    val students: List<Student> = emptyList(),
    val grades: List<MonthlyGrade> = emptyList(),
    val timestamp: Long = System.currentTimeMillis()
)
