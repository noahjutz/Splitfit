package com.noahjutz.gymroutines.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.noahjutz.gymroutines.data.Repository
import com.noahjutz.gymroutines.ui.exercises.create.CreateExerciseViewModel
import com.noahjutz.gymroutines.ui.exercises.ExercisesViewModel
import com.noahjutz.gymroutines.ui.routines.RoutinesViewModel
import com.noahjutz.gymroutines.ui.routines.create.pick.PickExerciseViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val repository: Repository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RoutinesViewModel::class.java) ->
                RoutinesViewModel(repository) as T
            modelClass.isAssignableFrom(ExercisesViewModel::class.java) ->
                ExercisesViewModel(repository) as T
            modelClass.isAssignableFrom(PickExerciseViewModel::class.java) ->
                PickExerciseViewModel(repository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
