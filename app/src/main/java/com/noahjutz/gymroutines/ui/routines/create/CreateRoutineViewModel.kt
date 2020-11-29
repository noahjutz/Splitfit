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

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.noahjutz.gymroutines.data.Repository
import com.noahjutz.gymroutines.data.domain.Routine
import com.noahjutz.gymroutines.data.domain.Set

class CreateRoutineViewModel @ViewModelInject constructor(
    private val repository: Repository,
) : ViewModel() {
    private var routine: Routine? = null
    var routineLiveData: LiveData<Routine?>? = null
    val initialName: String
        get() = routine?.name ?: ""

    fun getExerciseName(exerciseId: Int) = repository.getExercise(exerciseId)?.name.toString()

    fun updateRoutine(action: Routine.() -> Unit) {
        routine?.routineId?.let { id ->
            repository.insert(repository.getRoutine(id)!!.apply(action))
        }
    }

    fun addSet(exerciseId: Int) {
        updateRoutine {
            sets.apply {
                add(Set(exerciseId = exerciseId))
                sortBy { it.exerciseId }
            }
        }
    }

    fun setRoutine(routineId: Int) {
        routineLiveData = repository.getRoutineLive(routineId)
        routine = repository.getRoutine(routineId)
    }
}
