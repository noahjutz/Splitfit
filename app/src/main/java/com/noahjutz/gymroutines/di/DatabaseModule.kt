package com.noahjutz.gymroutines.di

import android.content.Context
import androidx.room.Room
import com.noahjutz.gymroutines.data.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "workout_routines_database"
        ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideExerciseDao(database: AppDatabase) = database.exerciseDao

    @Provides
    fun provideExerciseHolderDao(database: AppDatabase) = database.exerciseHolderDao

    @Provides
    fun provideExerciseImplDao(database: AppDatabase) = database.exerciseImplDao

    @Provides
    fun provideFullRoutineDao(database: AppDatabase) = database.fullRoutineDao

    @Provides
    fun provideRoutineDao(database: AppDatabase) = database.routineDao

    @Provides
    fun provideSetDao(database: AppDatabase) = database.setDao
}
