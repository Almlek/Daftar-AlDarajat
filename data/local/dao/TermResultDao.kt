package com.alborihi.myapp.data.local.dao

import androidx.room.*
import com.alborihi.myapp.data.local.entity.TermResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TermResultDao {
    @Query("SELECT * FROM term_results WHERE studentId = :studentId AND term = :term ORDER BY generatedAt DESC")
    fun getTermResults(studentId: Long, term: Int): Flow<List<TermResultEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTermResult(result: TermResultEntity): Long

    @Query("DELETE FROM term_results WHERE studentId = :studentId AND term = :term")
    suspend fun deleteTermResult(studentId: Long, term: Int): Int
}
