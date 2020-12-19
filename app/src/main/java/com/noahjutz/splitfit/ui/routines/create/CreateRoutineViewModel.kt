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

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.noahjutz.splitfit.data.Repository
import com.noahjutz.splitfit.data.domain.Routine
import com.noahjutz.splitfit.data.domain.Set
import com.noahjutz.splitfit.data.domain.SetGroup
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

// TODO adopt new CreateRoutineEditor/Presenter
class CreateRoutineController(
    private val repository: Repository,
    routineId: Int,
) {
    private val _routine = MutableStateFlow(repository.getRoutine(routineId)!!)

    inner class Editor {
        private fun updateRoutine(action: Routine.() -> Unit) {
            _routine.value = _routine.value.copy().apply(action)
        }

        fun setName(newName: String) {
            updateRoutine {
                name = newName
            }
        }

        fun addSetTo(setGroupIndex: Int) {
            updateRoutine {
                setGroups[setGroupIndex].sets.add(Set())
            }
        }

        fun deleteSetFrom(setGroupIndex: Int, setIndex: Int) {
            updateRoutine {
                setGroups[setGroupIndex].sets.removeAt(setIndex)
                setGroups.removeAll { it.sets.isEmpty() }
            }
        }

        fun addSetGroup(exerciseId: Int) {
            updateRoutine {
                val setGroup = SetGroup(exerciseId)
                if (setGroup !in setGroups) {
                    setGroups.add(setGroup)
                }
            }
        }

        fun swapSetGroups(i1: Int, i2: Int) {
            if (i1 < 0 || i2 < 0) return
            updateRoutine {
                if (setGroups.lastIndex < i1 || setGroups.lastIndex < i2) return@updateRoutine
                setGroups[i1] = setGroups[i2].also { setGroups[i2] = setGroups[i1] }
            }
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

@Deprecated("Use new CreateRoutineVM")
class CreateRoutineViewModel @ViewModelInject constructor(
    private val repository: Repository,
) : ViewModel() {
    private var initialRoutine: Routine? = null
    var routineLiveData: LiveData<Routine?>? = null
    val initialName: String
        get() = initialRoutine?.name ?: ""

    fun getExerciseName(exerciseId: Int) = repository.getExercise(exerciseId)?.name.toString()

    fun getExercise(exerciseId: Int) = repository.getExercise(exerciseId)

    fun updateRoutine(action: Routine.() -> Unit) {
        initialRoutine?.routineId?.let { id ->
            repository.insert(repository.getRoutine(id)!!.apply(action))
        }
    }

    fun addSet(exerciseId: Int) {
        updateRoutine {
            setGroups.first { it.exerciseId == exerciseId }.sets.add(Set())
        }
    }

    fun appendSets(exerciseIds: List<Int>) {
        updateRoutine {
            val setGroups = exerciseIds.map { exerciseId ->
                SetGroup(exerciseId)
            }.filter { it.exerciseId !in setGroups.map { it.exerciseId } }
            this.setGroups.addAll(setGroups)
        }
    }

    fun setRoutine(routineId: Int) {
        routineLiveData = repository.getRoutineLive(routineId)
        initialRoutine = repository.getRoutine(routineId)
    }

    fun getSetGroup(index: Int) = routineLiveData?.value?.setGroups?.getOrNull(index)

    fun swapSetGroups(i1: Int, i2: Int) {
        updateRoutine {
            setGroups.apply {
                val tmp = this[i1]
                this[i1] = this[i2]
                this[i2] = tmp
            }
        }
    }
}
