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

package com.noahjutz.gymroutines.data

import androidx.lifecycle.LiveData
import com.noahjutz.gymroutines.data.dao.ExerciseDao
import com.noahjutz.gymroutines.data.dao.RoutineDao
import com.noahjutz.gymroutines.data.domain.Exercise
import com.noahjutz.gymroutines.data.domain.Routine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

class Repository @Inject constructor(
    private val exerciseDao: ExerciseDao,
    private val routineDao: RoutineDao,
) {
    val routines = routineDao.getRoutines()
    val exercises = exerciseDao.getExercises()

    /** [Exercise] */
    fun insert(exercise: Exercise) = runBlocking {
        withContext(IO) {
            exerciseDao.insert(exercise)
        }
    }

    fun getExercise(id: Int): Exercise? = runBlocking {
        withContext(IO) {
            exerciseDao.getExercise(id)
        }
    }

    /** [Routine] */
    fun getRoutine(routineId: Int): Routine? = runBlocking {
        withContext(IO) {
            routineDao.getRoutine(routineId)
        }
    }

    fun getRoutineLive(routineId: Int): LiveData<Routine?>? = runBlocking {
        withContext(IO) {
            routineDao.getRoutineLive(routineId)
        }
    }

    fun insert(routine: Routine): Long = runBlocking {
        routineDao.insert(routine)
    }

    fun delete(routine: Routine) {
        CoroutineScope(IO).launch {
            routineDao.delete(routine)
        }
    }
}
