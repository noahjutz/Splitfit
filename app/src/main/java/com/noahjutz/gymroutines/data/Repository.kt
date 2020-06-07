package com.noahjutz.gymroutines.data

import android.app.Application
import com.noahjutz.gymroutines.data.dao.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

@Suppress("unused")
private const val TAG = "Repository"

class Repository private constructor(application: Application) {
    private val database: AppDatabase = AppDatabase.getInstance(application)

    private val exerciseDao = database.exerciseDao
    private val routineDao = database.routineDao
    private val exerciseWrapperDao = database.exerciseWrapperDao
    private val setDao = database.setDao
    private val rweDao = database.rweDao

    // TODO: Remove all of the above
    private val dao = database.dao

    // val routines = routineDao.getRoutines()
    // val exercises = exerciseDao.getExercises()
    val exerciseWrappers = exerciseWrapperDao.getExerciseWrappers()
    val sets = setDao.getSets()
    val routinesWithExercises = rweDao.getRoutinesWithExercises()
    // TODO: Remove the above

    val routines = dao.getRoutines()
    val exercises = dao.getExercises()
    val rwews = dao.getRwEwS()

    companion object {
        @Volatile
        private var INSTANCE: Repository? = null

        fun getInstance(application: Application) =
            INSTANCE ?: synchronized(this) {
                Repository(application).also { INSTANCE = it }
            }
    }

    /**
     * [RwEwS]
     */

    fun insert(rwews: RwEwS) {
        CoroutineScope(IO).launch {
            dao.insert(rwews)
        }
    }

    /**
     * [RwE]
     */

    fun insert(rwe: RwE) {
        val routineId = insert(rwe.routine).toInt()
        for (ewId in rwe.exerciseWrappers.map { it.exerciseWrapperId })
            assignEW(routineId, ewId)
    }

    /**
     * [RoutineDao]
     */

    fun insert(routine: Routine): Long = runBlocking {
        withContext(IO) {
            routineDao.insert(routine)
        }
    }

    fun delete(routine: Routine) = runBlocking {
        withContext(IO) {
            routineDao.delete(routine)
        }
    }


    /**
     * [RweDao]
     */

    fun getRweById(routineId: Int): RwE? = runBlocking {
        withContext(IO) {
            rweDao.getRweById(routineId)
        }
    }

    private fun assignEW(routineId: Int, exerciseWrapperId: Int) = runBlocking {
        CoroutineScope(IO).launch {
            rweDao.assignEW(routineId, exerciseWrapperId)
        }
    }

    /**
     * [ExerciseDao]
     */

    fun insert(exercise: Exercise) = runBlocking {
        withContext(IO) {
            exerciseDao.insert(exercise)
        }
    }

    fun delete(exercise: Exercise) = runBlocking {
        withContext(IO) {
            exerciseDao.delete(exercise)
        }
    }

    fun getExerciseById(id: Int): Exercise? = runBlocking {
        withContext(IO) {
            exerciseDao.getExerciseById(id)
        }
    }

    /**
     * [ExerciseWrapperDao]
     */

    fun insert(exerciseWrapper: ExerciseWrapper): Long = runBlocking {
        withContext(IO) {
            exerciseWrapperDao.insert(exerciseWrapper)
        }
    }

    fun delete(exerciseWrapper: ExerciseWrapper) = runBlocking {
        withContext(IO) {
            exerciseWrapperDao.delete(exerciseWrapper)
        }
    }

    fun getExerciseWrapperById(id: Int): ExerciseWrapper? = runBlocking {
        withContext(IO) {
            exerciseWrapperDao.getExerciseWrapperById(id)
        }
    }

    /**
     * [SetDao]
     */

    fun insert(set: Set): Long = runBlocking {
        withContext(IO) {
            setDao.insert(set)
        }
    }

    fun delete(set: Set) = runBlocking {
        withContext(IO) {
            setDao.delete(set)
        }
    }

    fun getSetById(id: Int): Set? = runBlocking {
        withContext(IO) {
            setDao.getSetById(id)
        }
    }

    fun getSetsById(ewId: Int) = runBlocking {
        withContext(IO) {
            setDao.getSetsById(ewId)
        }
    }

}