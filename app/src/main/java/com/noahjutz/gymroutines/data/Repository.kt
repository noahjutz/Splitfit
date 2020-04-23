package com.noahjutz.gymroutines.data

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class Repository(application: Application) {
    private val database: AppDatabase = AppDatabase.getInstance(application)

    private val exerciseDao = database.exerciseDao
    private val routineDao = database.routineDao

    private val allRoutines = routineDao.getAllRoutines()

    fun insert(routine: Routine) = CoroutineScope(IO).launch { routineDao.insert(routine) }

    fun clearRoutines() = CoroutineScope(IO).launch { routineDao.clearRoutines() }

    fun getAllRoutines(): LiveData<List<Routine>> = allRoutines
}