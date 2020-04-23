package com.noahjutz.gymroutines.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.noahjutz.gymroutines.data.Routine

@Dao
interface RoutineDao {
    @Insert
    fun insert(routine: Routine)

    @Query("DELETE FROM routine_table")
    fun deleteAllRoutines()

    @Query("SELECT * FROM routine_table ORDER BY name DESC")
    fun getAllRoutines(): LiveData<List<Routine>>
}