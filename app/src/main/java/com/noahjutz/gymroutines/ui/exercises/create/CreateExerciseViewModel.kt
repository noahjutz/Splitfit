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

package com.noahjutz.gymroutines.ui.exercises.create

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.noahjutz.gymroutines.data.Repository
import com.noahjutz.gymroutines.data.domain.Exercise
import com.noahjutz.gymroutines.util.ARGS_EXERCISE_ID
import com.noahjutz.gymroutines.util.ARGS_ROUTINE_ID
import kotlinx.coroutines.runBlocking

class CreateExerciseViewModel @ViewModelInject constructor(
    private val repository: Repository,
    @Assisted private val args: SavedStateHandle
) : ViewModel() {
    private val _exercise = MediatorLiveData<Exercise>()
    val exercise: LiveData<Exercise>
        get() = _exercise

    init {
        initExercise()
    }

    private fun initExercise() {
        _exercise.run {
            value = getExerciseById(args[ARGS_EXERCISE_ID] ?: -1)
                ?: repository.getExercise(repository.insert(Exercise()).toInt())
        }
    }

    /** [repository] access functions */
    private fun getExerciseById(id: Int): Exercise? = runBlocking { repository.getExercise(id) }

    fun updateExercise(action: Exercise.() -> Unit) {
        repository.insert(repository.getExercise(args[ARGS_EXERCISE_ID]!!)!!.apply(action))
    }
}
