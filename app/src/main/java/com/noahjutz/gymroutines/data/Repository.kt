package com.noahjutz.gymroutines.data

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class Repository private constructor(application: Application) {
    private val database: AppDatabase = AppDatabase.getInstance(application)

    private val exerciseDao = database.exerciseDao
    private val routineDao = database.routineDao

    private val routines = routineDao.getRoutines()
    private val exercises = exerciseDao.getExercises()

    /**
     * Singleton instantiation with constructor parameter
     */
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
    fun insert(routine: Routine) = CoroutineScope(IO).launch { routineDao.insert(routine) }
    fun clearRoutines() = CoroutineScope(IO).launch { routineDao.clearRoutines() }
    fun getRoutines(): LiveData<List<Routine>> = routines

    /**
     * Exercises
     */
    fun insert(exercise: Exercise) = CoroutineScope(IO).launch { exerciseDao.insert(exercise) }
    fun clearExercises() = CoroutineScope(IO).launch { exerciseDao.clearExercises() }
    fun getExercises(): LiveData<List<Exercise>> = exercises
}