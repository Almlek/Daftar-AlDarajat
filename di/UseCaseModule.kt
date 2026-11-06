package com.alborihi.myapp.di

import com.alborihi.myapp.domain.repository.AppRepository
import com.alborihi.myapp.domain.usecase.*
import com.alborihi.myapp.domain.usecase.analytics.GetDashboardStats
import com.alborihi.myapp.domain.usecase.backup.CreateBackup
import com.alborihi.myapp.domain.usecase.backup.RestoreBackup
import com.alborihi.myapp.domain.usecase.grade.*
import com.alborihi.myapp.domain.usecase.importexport.ExportToPdf
import com.alborihi.myapp.domain.usecase.importexport.ImportFromCsv
import com.alborihi.myapp.domain.usecase.student.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideGetAllStudents(repository: AppRepository): GetAllStudents {
        return GetAllStudents(repository)
    }

    @Provides
    @Singleton
    fun provideGetStudentById(repository: AppRepository): GetStudentById {
        return GetStudentById(repository)
    }

    @Provides
    @Singleton
    fun provideAddStudent(repository: AppRepository): AddStudent {
        return AddStudent(repository)
    }

    @Provides
    @Singleton
    fun provideUpdateStudent(repository: AppRepository): UpdateStudent {
        return UpdateStudent(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteStudent(repository: AppRepository): DeleteStudent {
        return DeleteStudent(repository)
    }

    @Provides
    @Singleton
    fun provideGetMonthlyGrades(repository: AppRepository): GetMonthlyGrades {
        return GetMonthlyGrades(repository)
    }

    @Provides
    @Singleton
    fun provideSaveMonthlyGrade(repository: AppRepository): SaveMonthlyGrade {
        return SaveMonthlyGrade(repository)
    }

    @Provides
    @Singleton
    fun provideCalculateTermResult(repository: AppRepository): CalculateTermResult {
        return CalculateTermResult(repository)
    }

    @Provides
    @Singleton
    fun provideGetDashboardStats(repository: AppRepository): GetDashboardStats {
        return GetDashboardStats(repository)
    }

    @Provides
    @Singleton
    fun provideImportFromCsv(repository: AppRepository): ImportFromCsv {
        return ImportFromCsv(repository)
    }

    @Provides
    @Singleton
    fun provideExportToPdf(repository: AppRepository): ExportToPdf {
        return ExportToPdf(repository)
    }

    @Provides
    @Singleton
    fun provideCreateBackup(repository: AppRepository): CreateBackup {
        return CreateBackup(repository)
    }

    @Provides
    @Singleton
    fun provideRestoreBackup(repository: AppRepository): RestoreBackup {
        return RestoreBackup(repository)
    }
}
