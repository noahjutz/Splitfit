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

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.noahjutz.gymroutines.data.Repository
import com.noahjutz.gymroutines.data.domain.Exercise
import kotlinx.coroutines.runBlocking

class CreateExerciseViewModel @ViewModelInject constructor(
    private val repository: Repository
) : ViewModel() {
    private var _exercise: Exercise? = null
    private val _exerciseLiveData = MutableLiveData<Exercise>()
    val exerciseLiveData: LiveData<Exercise>
        get() = _exerciseLiveData

    /** [repository] access functions */
    private fun getExerciseById(id: Int): Exercise? = runBlocking { repository.getExercise(id) }

    fun updateExercise(action: Exercise.() -> Unit) {
        repository.insert(repository.getExercise(_exercise!!.exerciseId)!!.apply(action))
    }

    fun setExercise(exerciseId: Int = -1) {
        _exerciseLiveData.value = getExerciseById(exerciseId)
            ?: repository.getExercise(repository.insert(Exercise()).toInt())
        _exercise = getExerciseById(exerciseId)
    }
}
