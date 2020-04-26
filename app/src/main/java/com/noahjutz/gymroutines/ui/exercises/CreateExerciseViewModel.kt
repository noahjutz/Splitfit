package com.noahjutz.gymroutines.ui.exercises

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noahjutz.gymroutines.data.Exercise
import com.noahjutz.gymroutines.data.Repository
import kotlinx.coroutines.launch

class CreateExerciseViewModel(private val repository: Repository) : ViewModel() {
    fun insert(exercise: Exercise) = viewModelScope.launch { repository.insert(exercise) }
    fun updateExercise(exercise: Exercise) = viewModelScope.launch { repository.update(exercise) }

    fun getExerciseById(id: Int): Exercise {
        var toReturn = Exercise("Error")
        viewModelScope.launch {
            repository.getExercises().value?.forEach { exercise ->
                if (exercise.exerciseId == id) {
                    toReturn = exercise
                }
            }
        }
        return toReturn
    }
}