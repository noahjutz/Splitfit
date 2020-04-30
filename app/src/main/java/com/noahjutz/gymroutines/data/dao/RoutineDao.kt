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
    suspend fun assignExercisesToRoutine(routineId: Int, exercises: List<Exercise>) {
        getRoutineExerciseCrossRefs().forEach { crossRef ->
            if (crossRef.routineId == routineId) delete(crossRef)
        }

        exercises.forEach { exercise ->
            val crossRef = RoutineExerciseCrossRef(
                routineId,
                exercise.exerciseId
            )
            insert(crossRef)
            Log.d(TAG, "CrossRefs: $crossRef")
        }
    }

    @Query("SELECT * from routineexercisecrossref")
    abstract suspend fun getRoutineExerciseCrossRefs(): List<RoutineExerciseCrossRef>

    @Delete
    abstract suspend fun delete(routineExerciseCrossRef: RoutineExerciseCrossRef)

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
    abstract suspend fun getRoutineWithExercisesById(id: Int): RoutineWithExercises

    @Query("SELECT MAX(routineId) FROM routine_table")
    abstract suspend fun getMaxId(): Int
}