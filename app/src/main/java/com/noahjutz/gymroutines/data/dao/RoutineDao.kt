package com.noahjutz.gymroutines.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.noahjutz.gymroutines.data.Routine
import com.noahjutz.gymroutines.data.RoutineExerciseCrossRef
import com.noahjutz.gymroutines.data.RoutineWithExercises

@Suppress("unused")
private const val TAG = "RoutineDao"

@Dao
abstract class RoutineDao {
    suspend fun assignExercisesToRoutine(routineId: Int, exerciseIds: List<Int>) {
        unassignExercisesFromRoutine(routineId)
        for (exerciseId in exerciseIds) {
            val crossRef = RoutineExerciseCrossRef(routineId, exerciseId)
            insert(crossRef)
        }
    }

    suspend fun unassignExercisesFromRoutine(routineId: Int) {
        for (crossRef in getRoutineExerciseCrossRefs()) {
            if (crossRef.routineId == routineId) delete(crossRef)
        }
    }

    @Query("SELECT * from routineexercisecrossref")
    abstract suspend fun getRoutineExerciseCrossRefs(): List<RoutineExerciseCrossRef>

    suspend fun getExerciseIdsByRoutineId(routineId: Int): List<Int> {
        return getRoutineExerciseCrossRefs().filter { it.routineId == routineId }.map { it.exerciseId }
    }

    @Delete
    abstract suspend fun delete(routineExerciseCrossRef: RoutineExerciseCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(routineExerciseCrossRef: RoutineExerciseCrossRef)

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

    @Transaction
    @Query("SELECT * FROM routine_table")
    abstract fun getRoutinesWithExercises(): LiveData<List<RoutineWithExercises>>

    @Transaction
    @Query("SELECT * FROM routine_table WHERE routineId == :id")
    abstract suspend fun getRweById(id: Int): RoutineWithExercises
}