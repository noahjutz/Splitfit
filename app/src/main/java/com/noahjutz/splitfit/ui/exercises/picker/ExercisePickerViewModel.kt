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

package com.noahjutz.splitfit.ui.exercises.picker

import androidx.lifecycle.ViewModel
import com.noahjutz.splitfit.data.ExerciseRepository
import com.noahjutz.splitfit.data.domain.Exercise
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.util.*

class ExercisePickerViewModel(
    private val exerciseRepository: ExerciseRepository,
) : ViewModel() {
    private val _nameFilter = MutableStateFlow("")
    private val exercises = exerciseRepository.exercises
    private val _selectedExercises = MutableStateFlow(emptyList<Exercise>())

    val presenter = Presenter()
    val editor = Editor()

    inner class Editor {
        fun search(name: String) {
            _nameFilter.value = name
        }

        fun addExercise(exercise: Exercise) {
            _selectedExercises.value =
                _selectedExercises.value.toMutableList().apply { add(exercise) }
        }

        fun removeExercise(exercise: Exercise) {
            _selectedExercises.value =
                _selectedExercises.value.toMutableList().apply { remove(exercise) }
        }

        fun clearExercises() {
            _selectedExercises.value = emptyList()
        }
    }

    inner class Presenter {
        val nameFilter = _nameFilter.asStateFlow()

        val allExercises = exercises.combine(_nameFilter) { exercises, nameFilter ->
            exercises.filter {
                it.name.lowercase(Locale.getDefault())
                    .contains(nameFilter.lowercase(Locale.getDefault()))
            }
        }

        val selectedExercises = _selectedExercises.asStateFlow()

        fun exercisesContains(exercise: Exercise) = selectedExercises.map { it.contains(exercise) }
    }
}
