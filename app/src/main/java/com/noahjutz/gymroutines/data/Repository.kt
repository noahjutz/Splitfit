package com.noahjutz.gymroutines.data

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private const val TAG = "Repository"

class Repository private constructor(application: Application) {
    private val database: AppDatabase = AppDatabase.getInstance(application)

    private val exerciseDao = database.exerciseDao
    private val routineDao = database.routineDao

    val routines = routineDao.getRoutines()
    val exercises = exerciseDao.getExercises()

    val routinesWithExercises = routineDao.getRoutinesWithExercises()

    companion object {
        @Volatile
        private var INSTANCE: Repository? = null

        fun getInstance(application: Application) =
            INSTANCE ?: synchronized(this) {
                Repository(application).also { INSTANCE = it }
            }
    }

    /**
     * Routines
     */
    fun insert(routine: Routine): Long = runBlocking { routineDao.insert(routine) }
    fun delete(routine: Routine) = CoroutineScope(IO).launch { routineDao.delete(routine) }
    fun clearRoutines() = CoroutineScope(IO).launch { routineDao.clearRoutines() }
    fun getRoutineById(id: Int): Routine? = runBlocking { routineDao.getRoutineById(id) }
    fun insertExercisesForRoutine(routineId: Int, exercises: List<Exercise>) =
        CoroutineScope(IO).launch { routineDao.assignExercisesToRoutine(routineId, exercises) }

    fun getRoutineWithExercisesById(routineId: Int): RoutineWithExercises? = runBlocking {
        routineDao.getRoutineWithExercisesById(routineId)
    }

    /**
     * Exercises
     */
    fun insert(exercise: Exercise) = CoroutineScope(IO).launch { exerciseDao.insert(exercise) }
    fun delete(exercise: Exercise) = CoroutineScope(IO).launch { exerciseDao.delete(exercise) }
    fun clearExercises() = CoroutineScope(IO).launch { exerciseDao.clearExercises() }
    fun getExerciseById(id: Int): Exercise? = runBlocking { exerciseDao.getExerciseById(id) }
}