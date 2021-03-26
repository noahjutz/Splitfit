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

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noahjutz.splitfit.data.WorkoutRepository
import com.noahjutz.splitfit.data.domain.Workout
import com.noahjutz.splitfit.util.DatastoreKeys
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class WorkoutInsightsViewModel(
    private val repository: WorkoutRepository,
    private val preferences: DataStore<Preferences>,
) : ViewModel() {
    val presenter = Presenter()
    val editor = Editor()

    inner class Presenter {
        private var preferencesData: Preferences? = null

        init {
            viewModelScope.launch { preferences.data.collect { preferencesData = it } }
        }

        val workouts = repository.getWorkouts().map {
            it.filter {
                preferencesData?.get(DatastoreKeys.currentWorkout) != it.workoutId
            }
        }
    }

    inner class Editor {
        fun delete(workout: Workout) = viewModelScope.launch { repository.delete(workout) }
    }
}
