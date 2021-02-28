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
import com.noahjutz.splitfit.data.domain.Exercise
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform

/**
 * Used to pass exercises back from ExercisePicker
 */
class SharedExercisePickerViewModel : ViewModel() {
    private val _exercises = MutableStateFlow<List<Exercise>>(mutableListOf())
    val exercises = _exercises.asStateFlow()

    fun add(exercise: Exercise) {
        _exercises.value = _exercises.value.toMutableList().also { it.add(exercise) }
    }

    fun remove(exercise: Exercise) {
        _exercises.value = _exercises.value.toMutableList().also { it.remove(exercise) }
    }

    fun clear() {
        _exercises.value = emptyList()
    }

    fun contains(exercise: Exercise) = exercises.map { it.contains(exercise) }
}
