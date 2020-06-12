package com.noahjutz.gymroutines.data

import android.app.Application
import com.noahjutz.gymroutines.data.domain.Exercise
import com.noahjutz.gymroutines.data.domain.FullRoutine
import com.noahjutz.gymroutines.data.domain.Routine
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
            dao.insert(fullRoutine)
        }
    }

    fun delete(fullRoutine: FullRoutine) = runBlocking {
        withContext(IO) {
            dao.delete(fullRoutine)
        }
    }

    fun getFullRoutine(routineId: Int) = runBlocking {
        withContext(IO) {
            dao.getFullRoutine(routineId)
        }
    }

    /**
     * [Routine]
     */

    fun insert(routine: Routine): Long = runBlocking {
        withContext(IO) {
            dao.insert(routine)
        }
    }

    fun delete(routine: Routine) = runBlocking {
        withContext(IO) {
            dao.delete(routine)
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

    fun delete(exercise: Exercise) = runBlocking {
        withContext(IO) {
            dao.delete(exercise)
        }
    }

    fun getExercise(id: Int): Exercise? = runBlocking {
        withContext(IO) {
            dao.getExercise(id)
        }
    }

    /**
     * [Set]
     */

    fun insert(set: Set): Long = runBlocking {
        withContext(IO) {
            dao.insert(set)
        }
    }

    fun delete(set: Set) = runBlocking {
        withContext(IO) {
            dao.delete(set)
        }
    }

    fun getSetById(id: Int): Set? = runBlocking {
        withContext(IO) {
            dao.getSetById(id)
        }
    }

    fun getSetsById(ewId: Int) = runBlocking {
        withContext(IO) {
            dao.getSetsById(ewId)
        }
    }
}