package com.noahjutz.gymroutines.ui.routines.create.pick

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.noahjutz.gymroutines.data.Exercise
import com.noahjutz.gymroutines.ui.routines.create.CreateRoutineFragment
import com.noahjutz.gymroutines.ui.routines.create.CreateRoutineViewModel

@Suppress("unused")
private const val TAG = "PickExerciseViewModel"

/**
 * Shared [ViewModel] between [PickExerciseFragment] and [CreateRoutineFragment]
 *
 * [PickExerciseFragment] uses [addExercise] and [removeExercise] to edit [exercises]
 * [CreateRoutineFragment] observes [exercises] to update [CreateRoutineViewModel]
 */
class SharedExerciseViewModel : ViewModel() {
    private val _exercises: MutableLiveData<List<Exercise>> = MutableLiveData()
    val exercises: LiveData<List<Exercise>>
        get() = _exercises

    init {
        _exercises.value = ArrayList()
    }

    fun addExercise(exercise: Exercise) {
        _exercises.value = (exercises.value as ArrayList).apply {
            if (!contains(exercise)) add(exercise)
        }
    }

    fun removeExercise(exercise: Exercise) {
        _exercises.value = (exercises.value as ArrayList).apply { remove(exercise) }
    }

    fun clearExercises() {
        _exercises.value = ArrayList()
    }
}
