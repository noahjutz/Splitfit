package com.noahjutz.gymroutines.util

import android.app.Application
import com.noahjutz.gymroutines.data.AppDatabase
import com.noahjutz.gymroutines.data.Repository

// TODO: Replace with hilt
@Deprecated("Use hilt DI instead.")
object InjectorUtils {
    fun provideViewModelFactory(application: Application): ViewModelFactory {
        val database = AppDatabase.getInstance(application)
        val repository = Repository.getInstance(
            database.exerciseDao,
            database.exerciseHolderDao,
            database.exerciseImplDao,
            database.fullRoutineDao,
            database.routineDao,
            database.setDao
        )
        return ViewModelFactory(repository)
    }

    fun provideCreateViewModelFactory(application: Application, id: Int): CreateViewModelFactory {
        val database = AppDatabase.getInstance(application)
        val repository = Repository.getInstance(
            database.exerciseDao,
            database.exerciseHolderDao,
            database.exerciseImplDao,
            database.fullRoutineDao,
            database.routineDao,
            database.setDao
        )
        return CreateViewModelFactory(repository, id)
    }
}
