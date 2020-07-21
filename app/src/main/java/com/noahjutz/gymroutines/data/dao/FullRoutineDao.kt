package com.noahjutz.gymroutines.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.noahjutz.gymroutines.data.domain.FullRoutine

@Dao
interface FullRoutineDao {
    @Transaction
    @Query("SELECT * FROM routine_table")
    fun getFullRoutines(): LiveData<List<FullRoutine>>

    @Transaction
    @Query("SELECT * FROM routine_table WHERE routineId == :id")
    suspend fun getFullRoutine(id: Int): FullRoutine?
}
