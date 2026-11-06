package com.alborihi.myapp.di

import com.alborihi.myapp.data.local.dao.StudentDao
import com.alborihi.myapp.data.local.dao.TermResultDao
import com.alborihi.myapp.data.local.dao.YearlyResultDao
import com.alborihi.myapp.data.repository.AppRepositoryImpl
import com.alborihi.myapp.domain.repository.AppRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideRepository(
        studentDao: StudentDao,
        monthlyGradeDao: com.alborihi.myapp.data.local.dao.MonthlyGradeDao,
        termResultDao: TermResultDao,
        yearlyResultDao: YearlyResultDao
    ): AppRepository {
        return AppRepositoryImpl(studentDao, monthlyGradeDao, termResultDao, yearlyResultDao)
    }
}
