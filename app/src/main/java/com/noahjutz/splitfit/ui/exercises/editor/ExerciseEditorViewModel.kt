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

package com.noahjutz.splitfit.ui.exercises.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noahjutz.splitfit.data.ExerciseRepository
import com.noahjutz.splitfit.data.domain.Exercise
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ExerciseEditorViewModel(
    private val repository: ExerciseRepository,
    exerciseId: Int,
) : ViewModel() {
    private val _exercise = MutableStateFlow(
        repository.getExercise(exerciseId) ?: repository.getExercise(
            repository.insert(Exercise()).toInt()
        )!!
    )

    init {
        viewModelScope.launch {
            _exercise.collectLatest { repository.insert(it) }
        }
    }

    inner class Editor {
        fun updateExercise(
            name: String = _exercise.value.name,
            notes: String = _exercise.value.notes,
            logReps: Boolean = _exercise.value.logReps,
            logWeight: Boolean = _exercise.value.logWeight,
            logTime: Boolean = _exercise.value.logTime,
            logDistance: Boolean = _exercise.value.logDistance,
        ) {
            _exercise.value = _exercise.value.copy(
                name = name,
                notes = notes,
                logReps = logReps,
                logWeight = logWeight,
                logTime = logTime,
                logDistance = logDistance
            )
        }
    }

    inner class Presenter {
        val exercise = _exercise.asStateFlow()
    }
}
