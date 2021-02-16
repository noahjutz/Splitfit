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

package com.noahjutz.splitfit.ui.exercises

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noahjutz.splitfit.data.ExerciseRepository
import com.noahjutz.splitfit.data.domain.Exercise
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.*

class ExerciseListViewModel(
    private val repository: ExerciseRepository,
) : ViewModel() {
    private val nameFilter = MutableStateFlow("")
    fun search(name: String) {
        nameFilter.value = name
    }

    val exercises = repository.exercises.combine(nameFilter) { exercises, nameFilter ->
        exercises.filter {
            it.name.toLowerCase(Locale.getDefault())
                .contains(nameFilter.toLowerCase(Locale.getDefault()))
        }
    }

    fun hide(exercise: Exercise, hide: Boolean) =
        viewModelScope.launch { repository.insert(exercise.apply { hidden = hide }) }

    fun addExercise(): Long = repository.insert(Exercise())
}
