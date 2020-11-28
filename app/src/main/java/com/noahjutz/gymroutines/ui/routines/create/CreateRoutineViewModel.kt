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
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.noahjutz.gymroutines.data.Repository
import com.noahjutz.gymroutines.data.domain.Routine
import com.noahjutz.gymroutines.data.domain.Set

class CreateRoutineViewModel @ViewModelInject constructor(
    private val repository: Repository,
) : ViewModel() {
    var routineId = 0

    // TODO: Add sets value
    val initialName: String
        get() = repository.getRoutine(routineId)?.name.toString()

    fun getExerciseName(exerciseId: Int) = repository.getExercise(exerciseId)?.name.toString()

    fun updateRoutine(action: Routine.() -> Unit) {
        repository.insert(repository.getRoutine(routineId)!!.apply(action))
    }

    fun addSet(exerciseId: Int) {
        updateRoutine {
            sets.apply {
                add(Set(exerciseId = exerciseId))
                sortBy { it.exerciseId }
            }
        }
    }
}
