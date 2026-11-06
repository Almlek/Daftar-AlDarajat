package com.alborihi.myapp.data.local.dao

import androidx.room.*
import com.alborihi.myapp.data.local.entity.MonthlyGradeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MonthlyGradeDao {
    @Query("SELECT * FROM monthly_grades WHERE studentId = :studentId ORDER BY month ASC, term ASC")
    fun getMonthlyGradesByStudent(studentId: Long): Flow<List<MonthlyGradeEntity>>

    @Query("SELECT * FROM monthly_grades ORDER BY createdAt DESC")
    fun getAllMonthlyGrades(): Flow<List<MonthlyGradeEntity>>

    @Query("SELECT * FROM monthly_grades WHERE studentId = :studentId AND month = :month AND term = :term")
    fun getMonthlyGrade(studentId: Long, month: String, term: Int): Flow<MonthlyGradeEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGrade(grade: MonthlyGradeEntity): Long

    @Update
    suspend fun updateGrade(grade: MonthlyGradeEntity): Int

    @Delete
    suspend fun deleteGrade(grade: MonthlyGradeEntity): Int

    @Query("DELETE FROM monthly_grades WHERE studentId = :studentId")
    suspend fun deleteByStudentId(studentId: Long): Int

    @Query("SELECT COUNT(*) FROM monthly_grades")
    fun getTotalGradesCount(): Flow<Int>
}
