package com.alborihi.myapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.alborihi.myapp.data.local.dao.*
import com.alborihi.myapp.data.local.entity.*

@Database(
    entities = [
        StudentEntity::class,
        MonthlyGradeEntity::class,
        TermResultEntity::class,
        YearlyResultEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun studentDao(): StudentDao
    abstract fun monthlyGradeDao(): MonthlyGradeDao
    abstract fun termResultDao(): TermResultDao
    abstract fun yearlyResultDao(): YearlyResultDao

    companion object {
        const val DATABASE_NAME = "school_grades_database"
    }
}
