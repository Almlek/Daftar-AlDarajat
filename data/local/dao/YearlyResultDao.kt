package com.alborihi.myapp.data.local.dao

import androidx.room.*
import com.alborihi.myapp.data.local.entity.YearlyResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface YearlyResultDao {
    @Query("SELECT * FROM yearly_results WHERE academicYear = :year ORDER BY generatedAt DESC")
    fun getYearlyResults(year: String): Flow<List<YearlyResultEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertYearlyResult(result: YearlyResultEntity): Long

    @Query("DELETE FROM yearly_results WHERE studentId = :studentId")
    suspend fun deleteByStudentId(studentId: Long): Int
}
