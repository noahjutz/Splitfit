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

package com.noahjutz.splitfit.ui.workout

import androidx.lifecycle.ViewModel
import com.noahjutz.splitfit.data.Repository
import com.noahjutz.splitfit.data.WorkoutRepository
import com.noahjutz.splitfit.data.domain.Workout
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class WorkoutViewModel(
    private val repository: WorkoutRepository,
    workoutId: Int
) : ViewModel() {
    private val _workout = MutableStateFlow(Workout("TODO retrieve from db"))
    val presenter = Presenter()
    val editor = Editor()

    inner class Editor {
        fun setName(name: String) {
            _workout.value = _workout.value.copy(name = name)
        }
    }

    inner class Presenter {
        val workout = _workout.asStateFlow()
    }
}
