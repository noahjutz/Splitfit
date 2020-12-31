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

package com.noahjutz.splitfit.ui.exercises.create

import androidx.lifecycle.ViewModel
import com.noahjutz.splitfit.data.ExerciseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CreateExerciseViewModel(
    private val repository: ExerciseRepository,
    exerciseId: Int,
) : ViewModel() {
    private val _exercise = MutableStateFlow(repository.getExercise(exerciseId)!!)

    inner class Editor {
        fun updateExercise(
            name: String = _exercise.value.name,
            logReps: Boolean = _exercise.value.logReps,
            logWeight: Boolean = _exercise.value.logWeight,
            logTime: Boolean = _exercise.value.logTime,
            logDistance: Boolean = _exercise.value.logDistance,
        ) {
            _exercise.value = _exercise.value.copy(
                name = name,
                logReps = logReps,
                logWeight = logWeight,
                logTime = logTime,
                logDistance = logDistance
            )
        }

        fun close() {
            repository.insert(_exercise.value)
        }
    }

    inner class Presenter {
        val exercise = _exercise.asStateFlow()
    }
}
