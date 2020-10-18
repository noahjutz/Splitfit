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

package com.noahjutz.gymroutines.ui.routines.create.pick

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.noahjutz.gymroutines.data.domain.Exercise
import com.noahjutz.gymroutines.ui.routines.create.CreateRoutineFragment
import com.noahjutz.gymroutines.ui.routines.create.CreateRoutineEditor

@Suppress("unused")
private const val TAG = "PickExerciseViewModel"

/**
 * Shared [ViewModel] between [PickExerciseFragment] and [CreateRoutineFragment]
 *
 * [PickExerciseFragment] uses [addExercise] and [removeExercise] to edit [exercises]
 * [CreateRoutineFragment] observes [exercises] to update [CreateRoutineEditor]
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
