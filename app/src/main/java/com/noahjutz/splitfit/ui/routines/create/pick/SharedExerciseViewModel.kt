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

package com.noahjutz.splitfit.ui.routines.create.pick

import androidx.lifecycle.ViewModel
import com.noahjutz.splitfit.data.domain.Exercise
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Used to pass back exercises from PickExercise to CreateRoutine
 */
class SharedExerciseViewModel : ViewModel() {
    private val _exercises = MutableStateFlow<MutableList<Exercise>>(mutableListOf())
    val exercises = _exercises.asStateFlow()

    fun add(exercise: Exercise) {
        _exercises.value = _exercises.value.toMutableList().also { it.add(exercise) }
    }

    fun remove(exercise: Exercise) {
        _exercises.value = _exercises.value.toMutableList().also { it.remove(exercise) }
    }

    fun clear() {
        _exercises.value = mutableListOf()
    }
}
