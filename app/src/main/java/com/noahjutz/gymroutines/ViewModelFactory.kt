package com.example.mvvmtutorial.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.noahjutz.gymroutines.data.Repository
import com.noahjutz.gymroutines.ui.exercises.ExercisesViewModel
import com.noahjutz.gymroutines.ui.routines.RoutinesViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val repository: Repository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RoutinesViewModel::class.java) -> {
                RoutinesViewModel(
                    repository
                ) as T
            }
            modelClass.isAssignableFrom(ExercisesViewModel::class.java) -> {
                ExercisesViewModel(
                    repository
                ) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
