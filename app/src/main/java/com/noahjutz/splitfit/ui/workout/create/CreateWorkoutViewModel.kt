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

package com.noahjutz.splitfit.ui.workout.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noahjutz.splitfit.data.ExerciseRepository
import com.noahjutz.splitfit.data.RoutineRepository
import com.noahjutz.splitfit.data.WorkoutRepository
import com.noahjutz.splitfit.data.domain.*
import com.noahjutz.splitfit.data.domain.Set
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CreateWorkoutViewModel(
    private val workoutRepository: WorkoutRepository,
    private val routineRepository: RoutineRepository,
    private val exerciseRepository: ExerciseRepository,
    workoutId: Int,
    routineId: Int,
) : ViewModel() {
    private val _workout = MutableStateFlow(
        runBlocking {
            workoutRepository.getWorkout(workoutId)
                ?: routineRepository.getRoutine(routineId)?.let {
                    workoutRepository.getWorkout(workoutRepository.insert(it.toWorkout()).toInt())
                }
                ?: workoutRepository.insert(Workout()).let {
                    workoutRepository.getWorkout(it.toInt())
                }!!
        }
    )
    val presenter = Presenter()
    val editor = Editor()

    init {
        viewModelScope.launch {
            _workout.collectLatest {
                workoutRepository.insert(_workout.value)
            }
        }
    }

    inner class Editor {
        fun setName(name: String) {
            _workout.value = _workout.value.copy(name = name)
        }

        fun addSetTo(setGroup: SetGroup) {
            val setGroups = _workout.value.setGroups.toMutableList().also {
                val i = it.indexOf(setGroup)
                val sets = it[i].sets + Set()
                it[i] = it[i].copy(sets = sets)
            }
            _workout.value = _workout.value.copy(setGroups = setGroups)
        }

        fun deleteSetFrom(setGroup: SetGroup, setIndex: Int) {
            val setGroups = _workout.value.setGroups.toMutableList().also {
                val i = it.indexOf(setGroup)
                val sets = it[i].sets.toMutableList().apply { removeAt(setIndex) }
                it[i] = it[i].copy(sets = sets)
                it.removeAll { it.sets.isEmpty() }
            }
            _workout.value = _workout.value.copy(setGroups = setGroups)
        }

        fun addExercises(exercises: List<Exercise>) {
            val setGroups = _workout.value.setGroups + exercises.map { SetGroup(it.exerciseId) }
                .filter { it.exerciseId !in _workout.value.setGroups.map { it.exerciseId } }
            _workout.value = _workout.value.copy(setGroups = setGroups)
        }

        fun swapSetGroups(i1: Int, i2: Int) {
            if (i1 < 0 || i2 < 0) return
            val setGroups = _workout.value.setGroups.toMutableList()
                .apply {
                    if (!(lastIndex < i1 || lastIndex < i2)) {
                        this[i1] = this[i2].also { this[i2] = this[i1] }
                    }
                }
            _workout.value = _workout.value.copy(setGroups = setGroups)
        }

        fun updateSet(
            setGroupIndex: Int,
            setIndex: Int,
            reps: Int? = _workout.value.setGroups[setGroupIndex].sets[setIndex].reps,
            weight: Double? = _workout.value.setGroups[setGroupIndex].sets[setIndex].weight,
            time: Int? = _workout.value.setGroups[setGroupIndex].sets[setIndex].time,
            distance: Double? = _workout.value.setGroups[setGroupIndex].sets[setIndex].distance,
        ) {
            val setGroups = _workout.value.setGroups.toMutableList().apply {
                this[setGroupIndex] = this[setGroupIndex].copy(
                    sets = this[setGroupIndex].sets.toMutableList().apply {
                        this[setIndex] = this[setIndex].copy(
                            reps = reps,
                            weight = weight,
                            time = time,
                            distance = distance,
                        )
                    }
                )
            }
            _workout.value = _workout.value.copy(setGroups = setGroups)
        }
    }

    inner class Presenter {
        val workout = _workout.asStateFlow()

        fun getExercise(exerciseId: Int) = exerciseRepository.getExercise(exerciseId)
    }
}
