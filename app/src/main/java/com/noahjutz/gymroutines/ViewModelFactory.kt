package com.noahjutz.gymroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.noahjutz.gymroutines.data.Repository
import com.noahjutz.gymroutines.ui.exercises.CreateExerciseViewModel
import com.noahjutz.gymroutines.ui.exercises.ExercisesViewModel
import com.noahjutz.gymroutines.ui.routines.CreateRoutineViewModel
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
            modelClass.isAssignableFrom(CreateRoutineViewModel::class.java) -> {
                CreateRoutineViewModel(
                    repository
                ) as T
            }
            modelClass.isAssignableFrom(CreateExerciseViewModel::class.java) -> {
                CreateExerciseViewModel(
                    repository
                ) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
