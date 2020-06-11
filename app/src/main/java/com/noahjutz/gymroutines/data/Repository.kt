package com.noahjutz.gymroutines.data

import android.app.Application
import com.noahjutz.gymroutines.data.domain.Exercise
import com.noahjutz.gymroutines.data.domain.ExerciseWrapper
import com.noahjutz.gymroutines.data.domain.Routine
import com.noahjutz.gymroutines.data.domain.RwE
import com.noahjutz.gymroutines.data.domain.Set
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

@Suppress("unused")
private const val TAG = "Repository"

class Repository private constructor(application: Application) {
    private val database: AppDatabase = AppDatabase.getInstance(application)
    private val dao = database.dao
    private val legacyDao = database.legacyDao

    val rwes = legacyDao.getRwEs()
    val routines = dao.getRoutines()
    val exercises = dao.getExercises()

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

    // fun insert(rwews: RwEwS) {
    //     CoroutineScope(IO).launch {
    //         dao.insert(rwews)
    //     }
    // }

    /**
     * [RwE] TODO: Move to bottom
     */

    fun insert(rwe: RwE) = runBlocking {
        withContext(IO) {
            legacyDao.insert(rwe)
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
     * [RwE] TODO: Move up to other RwE function
     */

    fun getRweById(routineId: Int): RwE? = runBlocking {
        withContext(IO) {
            legacyDao.getRwEById(routineId)
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
     * [ExerciseWrapper] TODO: move to bottom
     */

    fun insert(exerciseWrapper: ExerciseWrapper): Long = runBlocking {
        withContext(IO) {
            legacyDao.insert(exerciseWrapper)
        }
    }

    fun delete(exerciseWrapper: ExerciseWrapper) = runBlocking {
        withContext(IO) {
            legacyDao.delete(exerciseWrapper)
        }
    }

    fun getExerciseWrapperById(id: Int): ExerciseWrapper? = runBlocking {
        withContext(IO) {
            legacyDao.getExerciseWrapperById(id)
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