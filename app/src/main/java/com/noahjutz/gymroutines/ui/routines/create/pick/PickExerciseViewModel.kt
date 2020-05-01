package com.noahjutz.gymroutines.ui.routines.create.pick

import androidx.lifecycle.ViewModel
import com.noahjutz.gymroutines.data.Exercise
import com.noahjutz.gymroutines.data.Repository

class PickExerciseViewModel(private val repository: Repository) : ViewModel() {
    private val _exercises: ArrayList<Exercise> = ArrayList()

    fun addExercise(exercise: Exercise) {
        _exercises.add(exercise)
    }

    fun pickExercises(routineId: Int): Boolean {
        if (_exercises.isEmpty()) return false
        repository.insertExercisesForRoutine(routineId, _exercises)
        return true
    }
}
