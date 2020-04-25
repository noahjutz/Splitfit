package com.noahjutz.gymroutines.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.noahjutz.gymroutines.data.Routine

@Dao
interface RoutineDao {
    @Insert
    fun insert(routine: Routine)

    @Delete
    fun delete(routine: Routine)

    @Update
    fun update(routine: Routine)

    @Query("DELETE FROM routine_table")
    fun clearRoutines()

    @Query("SELECT * FROM routine_table ORDER BY name DESC")
    fun getRoutines(): LiveData<List<Routine>>
}