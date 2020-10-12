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

package com.noahjutz.gymroutines.ui.routines.create

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.noahjutz.gymroutines.data.Repository
import com.noahjutz.gymroutines.data.domain.Routine
import com.noahjutz.gymroutines.util.ARGS_ROUTINE_ID

class CreateRoutineViewModel @ViewModelInject constructor(
    private val repository: Repository,
    @Assisted private val args: SavedStateHandle
) : ViewModel() {
    private val _routine = MutableLiveData<Routine>()
    val routine: LiveData<Routine>
        get() = _routine

    init {
        _routine.value = repository.getRoutine(args[ARGS_ROUTINE_ID] ?: -1)
            ?: repository.getRoutine(
                repository.insert(
                    Routine("")
                ).toInt()
            )!!
    }

    fun updateRoutine(action: Routine.() -> Unit) {
        _routine.value = routine.value!!.apply(action)
    }

    override fun onCleared() {
        super.onCleared()
        repository.insert(routine.value!!)
    }
}
