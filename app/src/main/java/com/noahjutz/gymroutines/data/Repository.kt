package com.noahjutz.gymroutines.data

import android.app.Application
import com.noahjutz.gymroutines.data.dao.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
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

    val routines = routineDao.getRoutines()
    val exercises = exerciseDao.getExercises()
    val exerciseWrappers = exerciseWrapperDao.getExerciseWrappers()
    val sets = setDao.getSets()
    val routinesWithExercises = rweDao.getRoutinesWithExercises()

    companion object {
        @Volatile
        private var INSTANCE: Repository? = null

        fun getInstance(application: Application) =
            INSTANCE ?: synchronized(this) {
                Repository(application).also { INSTANCE = it }
            }
    }

    /**
     * Utility functions
     */

    fun insert(rwe: Rwe) {
        val routineId = insert(rwe.routine).toInt()
        for (ewId in rwe.exerciseWrappers.map { it.exerciseWrapperId })
            assignEW(routineId, ewId)
    }

    /**
     * Direct Dao access functions
     */

    /**
     * [RoutineDao]
     */

    fun insert(routine: Routine): Long = runBlocking { routineDao.insert(routine) }
    fun delete(routine: Routine) = CoroutineScope(IO).launch { routineDao.delete(routine) }


    /**
     * [RweDao]
     */

    fun getRweById(routineId: Int): Rwe? = runBlocking {
        rweDao.getRweById(routineId)
    }

    fun assignEW(routineId: Int, exerciseWrapperId: Int) {
        CoroutineScope(IO).launch {
            rweDao.assignEW(routineId, exerciseWrapperId)
        }
    }

    /**
     * [ExerciseDao]
     */

    fun insert(exercise: Exercise) = CoroutineScope(IO).launch { exerciseDao.insert(exercise) }
    fun delete(exercise: Exercise) = CoroutineScope(IO).launch { exerciseDao.delete(exercise) }
    fun getExerciseById(id: Int): Exercise? = runBlocking { exerciseDao.getExerciseById(id) }

    /**
     * [ExerciseWrapperDao]
     */

    fun insert(exerciseWrapper: ExerciseWrapper): Long = runBlocking {
        exerciseWrapperDao.insert(exerciseWrapper)
    }

    fun delete(exerciseWrapper: ExerciseWrapper) {
        CoroutineScope(IO).launch { exerciseWrapperDao.delete(exerciseWrapper) }
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
        setDao.insert(set)
    }

    fun delete(set: Set) {
        CoroutineScope(IO).launch { setDao.delete(set) }
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