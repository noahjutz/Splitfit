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

import com.noahjutz.gymroutines.data.dao.*
import com.noahjutz.gymroutines.data.domain.*
import com.noahjutz.gymroutines.data.domain.Set
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

class Repository @Inject constructor(
    private val exerciseDao: ExerciseDao,
    private val exerciseHolderDao: ExerciseHolderDao,
    private val exerciseImplDao: ExerciseImplDao,
    private val fullRoutineDao: FullRoutineDao,
    private val routineDao: RoutineDao,
    private val setDao: SetDao
) {
    val routines = routineDao.getRoutines()
    val exercises = exerciseDao.getExercises()
    val fullRoutines = fullRoutineDao.getFullRoutines()

    /** [FullRoutine] */
    fun insert(fullRoutine: FullRoutine) = runBlocking {
        withContext(IO) {
            val routineId = routineDao.insert(fullRoutine.routine)

            val exerciseImpls = exerciseImplDao.getExerciseImplsIn(routineId.toInt())
            for (e in exerciseImpls)
                delete(e)

            for (e in fullRoutine.exercises)
                insert(e)

            routineId
        }
    }

    fun delete(fullRoutine: FullRoutine) = runBlocking {
        withContext(IO) {
            routineDao.delete(fullRoutine.routine)

            val exerciseImpls = exerciseImplDao.getExerciseImplsIn(fullRoutine.routine.routineId)
            for (e in exerciseImpls)
                delete(e)
        }
    }

    fun getFullRoutine(routineId: Int) = runBlocking {
        withContext(IO) {
            fullRoutineDao.getFullRoutine(routineId)
        }
    }

    /** [ExerciseImpl] */
    fun insert(exerciseImpl: ExerciseImpl) = runBlocking {
        withContext(IO) {
            val exerciseId = exerciseHolderDao.insert(exerciseImpl.setGroup)

            for (set in exerciseImpl.sets)
                insert(set)

            exerciseId
        }
    }

    private fun delete(exerciseImpl: ExerciseImpl) = runBlocking {
        withContext(IO) {
            exerciseHolderDao.delete(exerciseImpl.setGroup)

            for (set in exerciseImpl.sets)
                delete(set)
        }
    }

    fun getExerciseImpl(exerciseHolderId: Int) = runBlocking {
        withContext(IO) {
            exerciseImplDao.getExerciseImpl(exerciseHolderId)
        }
    }

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

    /** [Set] */
    fun insert(set: Set) = runBlocking {
        withContext(IO) {
            setDao.insert(set)
        }
    }

    fun delete(set: Set) = runBlocking {
        withContext(IO) {
            setDao.delete(set)
        }
    }

    fun getSet(id: Int): Set? = runBlocking {
        withContext(IO) {
            setDao.getSetById(id)
        }
    }


    fun getRoutine(routineId: Int): Routine? {
        // TODO
        return Routine("TODO")
    }

    fun insert(routine: Routine): Long = runBlocking {
        routineDao.insert(routine)
    }

    fun delete(routine: Routine) {
        TODO("Not yet implemented")
    }
}
