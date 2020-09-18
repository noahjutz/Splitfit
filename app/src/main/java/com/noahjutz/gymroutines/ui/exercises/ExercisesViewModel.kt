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

package com.noahjutz.gymroutines.ui.exercises

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noahjutz.gymroutines.data.Repository
import com.noahjutz.gymroutines.data.domain.Exercise
import kotlinx.coroutines.launch

class ExercisesViewModel @ViewModelInject constructor(
    private val repository: Repository
) : ViewModel() {
    val exercises: LiveData<List<Exercise>>
        get() = repository.exercises

    fun hide(exercise: Exercise, hide: Boolean) =
        viewModelScope.launch { repository.insert(exercise.apply { hidden = hide }) }
}
