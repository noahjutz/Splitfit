package com.noahjutz.gymroutines.data

import com.noahjutz.gymroutines.data.domain.Exercise
import com.noahjutz.gymroutines.data.domain.ExerciseImpl
import com.noahjutz.gymroutines.data.domain.FullRoutine
import com.noahjutz.gymroutines.data.domain.Set
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

// TODO: Instead of passing database as dependency, pass each dao as dependency.
class Repository @Inject constructor(database: AppDatabase) {
    private val exerciseDao = database.exerciseDao
    private val exerciseHolderDao = database.exerciseHolderDao
    private val exerciseImplDao = database.exerciseImplDao
    private val fullRoutineDao = database.fullRoutineDao
    private val routineDao = database.routineDao
    private val setDao = database.setDao

    val routines = routineDao.getRoutines()
    val exercises = exerciseDao.getExercises()
    val fullRoutines = fullRoutineDao.getFullRoutines()

    @Deprecated("InjectorUtils dependency")
    companion object {
        @Volatile
        private var INSTANCE: Repository? = null

        fun getInstance(database: AppDatabase) =
            INSTANCE ?: synchronized(this) {
                Repository(database).also { INSTANCE = it }
            }
    }

    /**
     * [FullRoutine]
     */

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

    /**
     * [ExerciseImpl]
     */

    fun insert(exerciseImpl: ExerciseImpl) = runBlocking {
        withContext(IO) {
            val exerciseId = exerciseHolderDao.insert(exerciseImpl.exerciseHolder)

            for (set in exerciseImpl.sets)
                insert(set)

            exerciseId
        }
    }

    private fun delete(exerciseImpl: ExerciseImpl) = runBlocking {
        withContext(IO) {
            exerciseHolderDao.delete(exerciseImpl.exerciseHolder)

            for (set in exerciseImpl.sets)
                delete(set)
        }
    }

    fun getExerciseImpl(exerciseHolderId: Int) = runBlocking {
        withContext(IO) {
            exerciseImplDao.getExerciseImpl(exerciseHolderId)
        }
    }

    /**
     * [Exercise]
     */

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

    /**
     * [com.noahjutz.gymroutines.data.domain.Set]
     */

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
}
