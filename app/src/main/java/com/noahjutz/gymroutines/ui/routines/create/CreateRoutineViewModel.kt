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
import androidx.lifecycle.*
import com.noahjutz.gymroutines.data.Repository
import com.noahjutz.gymroutines.data.domain.ExerciseImpl
import com.noahjutz.gymroutines.data.domain.FullRoutine
import com.noahjutz.gymroutines.data.domain.Routine
import com.noahjutz.gymroutines.data.domain.Set
import com.noahjutz.gymroutines.util.ARGS_ROUTINE_ID
import java.util.*
import kotlin.collections.ArrayList

class CreateRoutineViewModel @ViewModelInject constructor(
    private val repository: Repository,
    @Assisted private val args: SavedStateHandle
) : ViewModel() {
    private val _fullRoutine = MediatorLiveData<FullRoutine>()
    val fullRoutine: LiveData<FullRoutine>
        get() = _fullRoutine

    init {
        initFullRoutine()
    }

    private fun initFullRoutine() {
        _fullRoutine.run {
            // TODO: Get routine as dependency, don't instantiate it (?)
            value = repository.getFullRoutine(args[ARGS_ROUTINE_ID] ?: -1)
                ?: repository.getFullRoutine(
                    repository.insert(
                        FullRoutine(
                            Routine(""),
                            listOf()
                        )
                    ).toInt()
                )!!
        }
    }

    override fun onCleared() {
        save()
    }

    private fun save() {
        repository.insert(fullRoutine.value!!)
    }
}
