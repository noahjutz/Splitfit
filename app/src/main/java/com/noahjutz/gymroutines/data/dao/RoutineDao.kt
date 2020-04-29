package com.noahjutz.gymroutines.data.dao

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.*
import com.noahjutz.gymroutines.data.Exercise
import com.noahjutz.gymroutines.data.Routine
import com.noahjutz.gymroutines.data.RoutineExerciseCrossRef
import com.noahjutz.gymroutines.data.RoutineWithExercises

private const val TAG = "RoutineDao"

@Dao
abstract class RoutineDao {
    suspend fun insertExercisesForRoutine(routine: Routine, exercises: List<Exercise>) {
        exercises.forEach() { exercise ->
            val crossRef = RoutineExerciseCrossRef(
                routine.routineId,
                exercise.exerciseId
            )
            insert(crossRef)
            Log.d(TAG, "CrossRefs: $crossRef")
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(routineExerciseCrossRef: RoutineExerciseCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(routine: Routine)

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
    abstract fun getRoutinesWithExercises(): List<RoutineWithExercises>

    @Transaction
    @Query("SELECT * FROM routine_table WHERE routineId == :id")
    abstract fun getRoutineWithExercisesById(id: Int): RoutineWithExercises
}