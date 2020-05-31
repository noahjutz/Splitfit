package com.noahjutz.gymroutines.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.noahjutz.gymroutines.data.RoutineExerciseCrossRef
import com.noahjutz.gymroutines.data.Rwe

@Suppress("unused")
private const val TAG = "RoutineDao"

@Dao
abstract class RweDao {
    suspend fun assignEW(routineId: Int, exerciseWrapperId: Int) {
        val crossRef = RoutineExerciseCrossRef(routineId, exerciseWrapperId)
        insert(crossRef)
    }

    @Delete
    abstract suspend fun delete(routineExerciseCrossRef: RoutineExerciseCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(routineExerciseCrossRef: RoutineExerciseCrossRef)

    @Transaction
    @Query("SELECT * FROM routine_table")
    abstract fun getRoutinesWithExercises(): LiveData<List<Rwe>>

    @Transaction
    @Query("SELECT * FROM routine_table WHERE routineId == :id")
    abstract suspend fun getRweById(id: Int): Rwe
}