package com.alborihi.myapp.di

import com.alborihi.myapp.data.local.AppDatabase
import com.alborihi.myapp.data.local.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideStudentDao(database: AppDatabase): StudentDao {
        return database.studentDao()
    }

    @Provides
    @Singleton
    fun provideMonthlyGradeDao(database: AppDatabase): MonthlyGradeDao {
        return database.monthlyGradeDao()
    }

    @Provides
    @Singleton
    fun provideTermResultDao(database: AppDatabase): TermResultDao {
        return database.termResultDao()
    }

    @Provides
    @Singleton
    fun provideYearlyResultDao(database: AppDatabase): YearlyResultDao {
        return database.yearlyResultDao()
    }
}
