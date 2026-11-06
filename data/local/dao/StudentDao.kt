package com.alborihi.myapp.data.local.dao

import androidx.room.*
import com.alborihi.myapp.data.local.entity.StudentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao {
    @Query("SELECT * FROM students WHERE isActive = 1 ORDER BY name ASC")
    fun getAllStudents(): Flow<List<StudentEntity>>

    @Query("SELECT * FROM students WHERE id = :id")
    fun getStudentById(id: Long): Flow<StudentEntity?>

    @Query("SELECT * FROM students WHERE name LIKE :query OR className LIKE :query OR parentPhone LIKE :query ORDER BY name ASC")
    fun searchStudents(query: String): Flow<List<StudentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: StudentEntity): Long

    @Update
    suspend fun updateStudent(student: StudentEntity): Int

    @Delete
    suspend fun deleteStudent(student: StudentEntity): Int

    @Query("UPDATE students SET isActive = 0 WHERE id = :id")
    suspend fun softDeleteStudent(id: Long): Int

    @Query("SELECT COUNT(*) FROM students WHERE isActive = 1")
    fun getActiveStudentCount(): Flow<Int>
}
