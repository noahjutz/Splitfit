package com.noahjutz.gymroutines

import android.app.Application
import com.example.mvvmtutorial.viewmodel.ViewModelFactory
import com.noahjutz.gymroutines.data.Repository

object InjectorUtils {
    fun provideViewModelFactory(application: Application): ViewModelFactory {
        val repository = Repository.getInstance(application)
        return ViewModelFactory(repository)
    }
}