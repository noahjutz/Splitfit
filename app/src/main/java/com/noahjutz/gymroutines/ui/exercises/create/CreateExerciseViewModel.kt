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
import kotlinx.coroutines.runBlocking

class CreateExerciseViewModel @ViewModelInject constructor(
    private val repository: Repository,
    @Assisted private val args: SavedStateHandle
) : ViewModel() {
    /**
     * The [Exercise] object that is being created/edited
     * @see initExercise: adds [name] and [description] as source
     * @see save
     */
    private val _exercise = MediatorLiveData<Exercise>()
    val exercise: LiveData<Exercise>
        get() = _exercise

    /**
     * Data binding fields
     * [MediatorLiveData] sources for [exercise]
     * @see initBinding
     */
    val name = MutableLiveData<String>()
    val logWeight = MutableLiveData<Boolean>()
    val logReps = MutableLiveData<Boolean>()
    val logTime = MutableLiveData<Boolean>()
    val logDistance = MutableLiveData<Boolean>()

    init {
        initExercise()
        initBinding()
    }

    private fun initBinding() {
        name.value = exercise.value!!.name
        logWeight.value = exercise.value!!.logWeight
        logReps.value = exercise.value!!.logReps
        logTime.value = exercise.value!!.logTime
        logDistance.value = exercise.value!!.logDistance
    }

    private fun initExercise() {
        _exercise.run {
            value = getExerciseById(args[ARGS_EXERCISE_ID] ?: -1)
                ?: repository.getExercise(repository.insert(Exercise()).toInt())

            addSource(name) { source ->
                _exercise.value = _exercise.value!!.apply { name = source.trim() }
            }

            addSource(logWeight) { source ->
                _exercise.value = _exercise.value!!.apply { logWeight = source }
            }

            addSource(logReps) { source ->
                _exercise.value = _exercise.value!!.apply { logReps = source }
            }

            addSource(logTime) { source ->
                _exercise.value = _exercise.value!!.apply { logTime = source }
            }

            addSource(logDistance) { source ->
                _exercise.value = _exercise.value!!.apply { logDistance = source }
            }
        }
    }

    override fun onCleared() {
        save()
    }

    private fun save() {
        repository.insert(exercise.value!!)
    }

    /** [repository] access functions */
    private fun getExerciseById(id: Int): Exercise? = runBlocking { repository.getExercise(id) }
}
