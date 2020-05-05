package com.noahjutz.gymroutines.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.noahjutz.gymroutines.data.Repository
import com.noahjutz.gymroutines.ui.exercises.create.CreateExerciseViewModel
import com.noahjutz.gymroutines.ui.routines.create.CreateRoutineViewModel

@Suppress("UNCHECKED_CAST")
class CreateViewModelFactory(
    private val repository: Repository,
    private val id: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(CreateRoutineViewModel::class.java) ->
                CreateRoutineViewModel(repository, id) as T
            modelClass.isAssignableFrom(CreateExerciseViewModel::class.java) ->
                CreateExerciseViewModel(repository, id) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
