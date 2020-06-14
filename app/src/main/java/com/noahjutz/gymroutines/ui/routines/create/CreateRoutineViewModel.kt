package com.noahjutz.gymroutines.ui.routines.create

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.noahjutz.gymroutines.data.Repository
import com.noahjutz.gymroutines.data.domain.ExerciseImpl
import com.noahjutz.gymroutines.data.domain.FullRoutine
import com.noahjutz.gymroutines.data.domain.Routine
import java.util.*
import kotlin.collections.ArrayList

@Suppress("unused")
private const val TAG = "CreateRoutineViewModel"

class CreateRoutineViewModel(
    private val repository: Repository,
    private var routineId: Int
) : ViewModel() {
    private val _fullRoutine = MediatorLiveData<FullRoutine>()
    val fullRoutine: LiveData<FullRoutine>
        get() = _fullRoutine

    /**
     * Data binding fields
     * [MediatorLiveData] Sources for [fullRoutine]
     * @see initBinding
     */
    val name = MutableLiveData<String>()
    val description = MutableLiveData<String>()

    private val _exercises = MutableLiveData<ArrayList<ExerciseImpl>>()

    init {
        initFullRoutine()
        initBinding()
    }

    private fun initBinding() {
        name.value = fullRoutine.value!!.routine.name
        description.value = fullRoutine.value!!.routine.description
    }

    /**
     * Initializes [_fullRoutine]
     * Adds [name] and [description] and [_exercises] as source
     */
    private fun initFullRoutine() {
        _fullRoutine.run {
            /**
             * Either edit the routine with [routineId] or create a new one.
             */
            value = repository.getFullRoutine(routineId)
                ?: repository.getFullRoutine(
                    repository.insert(
                        FullRoutine(
                            Routine(""),
                            listOf()
                        )
                    ).toInt()
                )!!

            _exercises.value = value?.exercises as ArrayList<ExerciseImpl>

            addSource(name) { name ->
                value = value!!.apply {
                    routine.name = name.trim()
                }
            }

            addSource(description) { description ->
                value = value!!.apply {
                    routine.description = description.trim()
                }
            }

            addSource(_exercises) { exercises ->
                value = FullRoutine(
                    value!!.routine,
                    exercises
                )
            }
        }
    }

    fun save() {
        Log.d(TAG, "List: ${_exercises.value!!.map { it.exercise.name }}")
        for (i in 0 until _exercises.value!!.size) {
            _exercises.value!![i].exerciseHolder.position = i
            Log.d(TAG, "index: ${_exercises.value!![i].exerciseHolder.position} | i: $i | name: ${_exercises.value!![i].exercise.name}")
        }
        Log.d(TAG, "--")
        repository.insert(fullRoutine.value!!)
    }

    fun addExercise(exerciseImpl: ExerciseImpl) {
        _exercises.value = _exercises.value!!.apply { add(exerciseImpl) }
    }

    fun removeExercise(exerciseImpl: ExerciseImpl) {
        _exercises.value = _exercises.value!!.apply { remove(exerciseImpl) }
    }

    fun swapExercises(i: Int, j: Int) {
        _exercises.value = _exercises.value!!.apply {
            Collections.swap(this, i, j)
        }
    }
}
