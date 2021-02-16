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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import java.util.*

class ExercisePickerViewModel(
    private val exerciseRepository: ExerciseRepository,
) : ViewModel() {
    private val nameFilter = MutableStateFlow("")
    fun search(name: String) {
        nameFilter.value = name
    }

    val exercises = exerciseRepository.exercises.combine(nameFilter) { exercises, nameFilter ->
        exercises.filter {
            it.name.toLowerCase(Locale.getDefault())
                .contains(nameFilter.toLowerCase(Locale.getDefault()))
        }
    }
}