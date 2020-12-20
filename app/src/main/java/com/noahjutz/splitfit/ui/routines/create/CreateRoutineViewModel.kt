/*
 * Splitfit
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

package com.noahjutz.splitfit.ui.routines.create

import com.noahjutz.splitfit.data.Repository
import com.noahjutz.splitfit.data.domain.Exercise
import com.noahjutz.splitfit.data.domain.Routine
import com.noahjutz.splitfit.data.domain.Set
import com.noahjutz.splitfit.data.domain.SetGroup
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CreateRoutineController(
    private val repository: Repository,
    routineId: Int,
) {
    private val _routine = MutableStateFlow(repository.getRoutine(routineId)!!)

    inner class Editor {
        fun setName(name: String) {
            _routine.value = _routine.value.copy(name = name)
        }

        fun addSetTo(setGroup: SetGroup) {
            val setGroups = _routine.value.setGroups.toMutableList().also {
                val i = it.indexOf(setGroup)
                val sets = it[i].sets + Set()
                it[i] = it[i].copy(sets = sets)
            }
            _routine.value = _routine.value.copy(setGroups = setGroups)
        }

        fun deleteSetFrom(setGroup: SetGroup, setIndex: Int) {
            val setGroups = _routine.value.setGroups.toMutableList().also {
                val i = it.indexOf(setGroup)
                val sets = it[i].sets.toMutableList().apply { removeAt(setIndex) }
                it[i] = it[i].copy(sets = sets)
                it.removeAll { it.sets.isEmpty() }
            }
            _routine.value = _routine.value.copy(setGroups = setGroups)
        }

        fun addExercises(exercises: List<Exercise>) {
            val setGroups = _routine.value.setGroups + exercises.map { SetGroup(it.exerciseId) }
                .filter { it.exerciseId !in _routine.value.setGroups.map { it.exerciseId } }
            _routine.value = _routine.value.copy(setGroups = setGroups)
        }

        fun swapSetGroups(i1: Int, i2: Int) {
            if (i1 < 0 || i2 < 0) return
            val setGroups = _routine.value.setGroups.toMutableList()
                .apply {
                    if (!(lastIndex < i1 || lastIndex < i2)) {
                        this[i1] = this[i2].also { this[i2] = this[i1] }
                    }
                }
            _routine.value = _routine.value.copy(setGroups = setGroups)
        }

        fun close() {
            repository.insert(_routine.value)
        }
    }

    inner class Presenter {
        val routine = _routine.asStateFlow()

        fun getExercise(exerciseId: Int) = repository.getExercise(exerciseId)
    }
}

fun Routine.isEmpty() = name.isBlank() && setGroups.isEmpty()
