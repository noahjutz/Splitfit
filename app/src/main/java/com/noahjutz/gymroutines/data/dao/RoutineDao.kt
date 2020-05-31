package com.noahjutz.gymroutines.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.noahjutz.gymroutines.data.Routine
import com.noahjutz.gymroutines.data.Rwe

@Suppress("unused")
private const val TAG = "RoutineDao"

@Dao
abstract class RoutineDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(routine: Routine): Long

    @Delete
    abstract suspend fun delete(routine: Routine)

    @Query("DELETE FROM routine_table")
    abstract suspend fun clearRoutines()

    @Query("SELECT * FROM routine_table ORDER BY name DESC")
    abstract fun getRoutines(): LiveData<List<Routine>>

    @Query("SELECT * FROM routine_table WHERE routineId = :id")
    abstract suspend fun getRoutineById(id: Int): Routine
}