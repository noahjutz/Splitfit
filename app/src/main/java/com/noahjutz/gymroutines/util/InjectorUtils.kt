package com.noahjutz.gymroutines.util

import android.app.Application
import com.noahjutz.gymroutines.data.AppDatabase
import com.noahjutz.gymroutines.data.Repository

// TODO: Replace with hilt
object InjectorUtils {
    fun provideViewModelFactory(application: Application): ViewModelFactory {
        val repository = Repository.getInstance(AppDatabase.getInstance(application))
        return ViewModelFactory(repository)
    }

    fun provideCreateViewModelFactory(application: Application, id: Int): CreateViewModelFactory {
        val repository = Repository.getInstance(AppDatabase.getInstance(application))
        return CreateViewModelFactory(repository, id)
    }
}
