package com.noahjutz.gymroutines.util

import android.app.Application
import com.noahjutz.gymroutines.data.Repository

object InjectorUtils {
    fun provideViewModelFactory(application: Application): ViewModelFactory {
        val repository = Repository.getInstance(application)
        return ViewModelFactory(repository)
    }

    fun provideCreateViewModelFactory(application: Application, id: Int): CreateViewModelFactory {
        val repository = Repository.getInstance(application)
        return CreateViewModelFactory(repository, id)
    }
}
