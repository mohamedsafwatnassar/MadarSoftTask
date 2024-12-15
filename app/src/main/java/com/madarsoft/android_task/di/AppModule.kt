package com.madarsoft.android_task.di

import android.content.Context
import androidx.room.Room
import com.madarsoft.android_task.data.local.dao.PersonalDataDao
import com.madarsoft.android_task.data.local.database.PersonalDataDatabase
import com.madarsoft.android_task.data.repository.PersonalDataRepositoryImplLocal
import com.madarsoft.android_task.domain.repository.LocalPersonalDataRepository
import com.madarsoft.android_task.domain.usecase.FetchPersonalDatUseCase
import com.madarsoft.android_task.domain.usecase.InsertPersonalDataUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMovieDatabase(appContext: Context): PersonalDataDatabase {
        return Room.databaseBuilder(
            appContext,
            PersonalDataDatabase::class.java,
            "personal_data_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMovieDao(database: PersonalDataDatabase): PersonalDataDao {
        return database.personalDataDao()
    }


    @Provides
    @Singleton
    fun provideLocalMoviesRepository(dao: PersonalDataDao): LocalPersonalDataRepository {
        return PersonalDataRepositoryImplLocal(dao)
    }

    @Provides
    @Singleton
    fun provideIODispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }


    @Provides
    @Singleton
    fun provideInsertPersonalDataUseCase(
        localRepository: LocalPersonalDataRepository
    ): InsertPersonalDataUseCase {
        return InsertPersonalDataUseCase(localRepository)
    }

    @Provides
    @Singleton
    fun provideFetchPersonalDatUseCase(
        localRepository: LocalPersonalDataRepository
    ): FetchPersonalDatUseCase {
        return FetchPersonalDatUseCase(localRepository)
    }


    // Provide the application context as a dependency
    @Provides
    @Singleton
    fun provideContext(@ApplicationContext appContext: Context): Context {
        return appContext
    }
}
