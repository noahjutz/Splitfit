/*
 * GymRoutines
 * Copyright (C) 2020  Noah Jutz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.noahjutz.gymroutines.ui.routines.create

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.noahjutz.gymroutines.data.Repository
import com.noahjutz.gymroutines.data.domain.ExerciseImpl
import com.noahjutz.gymroutines.data.domain.FullRoutine
import com.noahjutz.gymroutines.data.domain.Routine
import com.noahjutz.gymroutines.data.domain.Set
import com.noahjutz.gymroutines.util.ARGS_ROUTINE_ID
import java.util.*
import kotlin.collections.ArrayList

class CreateRoutineViewModel @ViewModelInject constructor(
    private val repository: Repository,
    @Assisted private val args: SavedStateHandle
) : ViewModel() {
    private val _fullRoutine = MediatorLiveData<FullRoutine>()
    val fullRoutine: LiveData<FullRoutine>
        get() = _fullRoutine

    private val _exercises = MutableLiveData<ArrayList<ExerciseImpl>>()
    private val _sets = MutableLiveData<ArrayList<Set>>()

    /** Data binding fields */
    val name = MutableLiveData<String>()
    val description = MutableLiveData<String>()

    init {
        initFullRoutine()
        initBinding()
    }

    private fun initBinding() {
        name.value = fullRoutine.value!!.routine.name
        description.value = fullRoutine.value!!.routine.description
    }

    private fun initFullRoutine() {
        _fullRoutine.run {
            // TODO: Get routine as dependency, don't instantiate it (?)
            value = repository.getFullRoutine(args[ARGS_ROUTINE_ID] ?: -1)
                ?: repository.getFullRoutine(
                    repository.insert(
                        FullRoutine(
                            Routine(""),
                            listOf()
                        )
                    ).toInt()
                )!!

            _exercises.value = (value?.exercises as ArrayList<ExerciseImpl>).apply {
                sortBy { it.exerciseHolder.position }
            }

            _sets.value = ArrayList()
            value?.let {
                for (exercise in it.exercises) {
                    for (set in exercise.sets) {
                        addSet(set)
                    }
                }
            }

            for (e in value!!.exercises)
                for (s in e.sets)
                    addSet(s)

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

            addSource(_sets) { sets ->
                value = FullRoutine(
                    value!!.routine,
                    value!!.exercises.apply {
                        for (e in this) {
                            e.sets =
                                sets.filter { it.exerciseHolderId == e.exerciseHolder.exerciseHolderId }
                        }
                    }
                )
            }
        }
    }

    override fun onCleared() {
        save()
    }

    private fun save() {
        for (i in 0 until _exercises.value!!.size)
            _exercises.value!![i].exerciseHolder.position = i

        repository.insert(fullRoutine.value!!)
    }

    fun addExercise(exerciseImpl: ExerciseImpl) {
        val id = repository.insert(exerciseImpl).toInt()
        val exercise = repository.getExerciseImpl(id)
        _exercises.value = _exercises.value!!.apply { add(exercise!!) }

        addSet(Set(id))
    }

    fun removeExercise(exerciseImpl: ExerciseImpl) {
        _exercises.value = _exercises.value!!.apply { remove(exerciseImpl) }
    }

    fun swapExercises(i: Int, j: Int) {
        _exercises.value = _exercises.value!!.apply {
            Collections.swap(this, i, j)
        }
    }

    fun addSet(set: Set) {
        val setId = repository.insert(set)
        val newSet = repository.getSet(setId.toInt())
        _sets.value = _sets.value!!.apply {
            if (!contains(newSet))
                add(newSet!!)
        }
    }

    fun removeSet(set: Set) {
        _sets.value = _sets.value!!.apply { remove(set) }
        repository.delete(set)
    }
}
