package com.noahjutz.gymroutines.data

import com.noahjutz.gymroutines.data.dao.*
import com.noahjutz.gymroutines.data.domain.Exercise
import com.noahjutz.gymroutines.data.domain.ExerciseImpl
import com.noahjutz.gymroutines.data.domain.FullRoutine
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

    @Deprecated("InjectorUtils dependency")
    companion object {
        @Volatile
        private var INSTANCE: Repository? = null

        fun getInstance(
            exerciseDao: ExerciseDao,
            exerciseHolderDao: ExerciseHolderDao,
            exerciseImplDao: ExerciseImplDao,
            fullRoutineDao: FullRoutineDao,
            routineDao: RoutineDao,
            setDao: SetDao
        ) =
            INSTANCE ?: synchronized(this) {
                Repository(
                    exerciseDao,
                    exerciseHolderDao,
                    exerciseImplDao,
                    fullRoutineDao,
                    routineDao,
                    setDao
                ).also { INSTANCE = it }
            }
    }

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
}
