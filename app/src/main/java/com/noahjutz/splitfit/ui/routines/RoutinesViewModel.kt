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

package com.noahjutz.splitfit.ui.routines

import androidx.lifecycle.ViewModel
import com.noahjutz.splitfit.data.RoutineRepository
import com.noahjutz.splitfit.data.domain.Routine
import kotlinx.coroutines.flow.Flow

class RoutinesViewModel(
    private val repository: RoutineRepository,
) : ViewModel() {
    val routines: Flow<List<Routine>>
        get() = repository.routines

    fun deleteRoutine(routineId: Int) {
        repository.getRoutine(routineId)?.let { repository.delete(it) }
    }

    fun addRoutine(): Long = repository.insert(Routine())
}
