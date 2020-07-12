package com.noahjutz.gymroutines.data

import android.app.Application
import com.noahjutz.gymroutines.data.domain.Exercise
import com.noahjutz.gymroutines.data.domain.ExerciseImpl
import com.noahjutz.gymroutines.data.domain.FullRoutine
import com.noahjutz.gymroutines.data.domain.Set
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

@Suppress("unused")
private const val TAG = "Repository"

class Repository private constructor(application: Application) {
    private val database: AppDatabase = AppDatabase.getInstance(application)
    private val dao = database.dao

    val routines = dao.getRoutines()
    val exercises = dao.getExercises()
    val fullRoutines = dao.getFullRoutines()

    companion object {
        @Volatile
        private var INSTANCE: Repository? = null

        fun getInstance(application: Application) =
            INSTANCE ?: synchronized(this) {
                Repository(application).also { INSTANCE = it }
            }
    }

    /**
     * [FullRoutine]
     */

    fun insert(fullRoutine: FullRoutine) = runBlocking {
        withContext(IO) {
            val routineId = dao.insert(fullRoutine.routine)

            val exerciseImpls = dao.getExerciseImplsIn(routineId.toInt())
            for (e in exerciseImpls)
                delete(e)

            for (e in fullRoutine.exercises)
                insert(e)

            routineId
        }
    }

    fun delete(fullRoutine: FullRoutine) = runBlocking {
        withContext(IO) {
            dao.delete(fullRoutine.routine)

            val exerciseImpls = dao.getExerciseImplsIn(fullRoutine.routine.routineId)
            for (e in exerciseImpls)
                delete(e)
        }
    }

    fun getFullRoutine(routineId: Int) = runBlocking {
        withContext(IO) {
            dao.getFullRoutine(routineId)
        }
    }

    /**
     * [ExerciseImpl]
     */

    fun insert(exerciseImpl: ExerciseImpl) = runBlocking {
        withContext(IO) {
            val exerciseId = dao.insert(exerciseImpl.exerciseHolder)

            for (set in exerciseImpl.sets)
                insert(set)

            exerciseId
        }
    }

    private fun delete(exerciseImpl: ExerciseImpl) = runBlocking {
        withContext(IO) {
            dao.delete(exerciseImpl.exerciseHolder)

            for (set in exerciseImpl.sets)
                delete(set)
        }
    }

    fun getExerciseImpl(exerciseHolderId: Int) = runBlocking {
        withContext(IO) {
            dao.getExerciseImpl(exerciseHolderId)
        }
    }

    /**
     * [Exercise]
     */

    fun insert(exercise: Exercise) = runBlocking {
        withContext(IO) {
            dao.insert(exercise)
        }
    }

    fun getExercise(id: Int): Exercise? = runBlocking {
        withContext(IO) {
            dao.getExercise(id)
        }
    }

    /**
     * [com.noahjutz.gymroutines.data.domain.Set]
     */

    fun insert(set: Set) = runBlocking {
        withContext(IO) {
            dao.insert(set)
        }
    }

    fun delete(set: Set) = runBlocking {
        withContext(IO) {
            dao.delete(set)
        }
    }

    fun getSet(id: Int): Set? = runBlocking {
        withContext(IO) {
            dao.getSetById(id)
        }
    }
}
