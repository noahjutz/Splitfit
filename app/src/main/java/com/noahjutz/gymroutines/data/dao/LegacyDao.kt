package com.noahjutz.gymroutines.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.noahjutz.gymroutines.data.domain.ExerciseWrapper
import com.noahjutz.gymroutines.data.domain.Routine
import com.noahjutz.gymroutines.data.domain.RoutineExerciseCrossRef
import com.noahjutz.gymroutines.data.domain.RwE

/**
 * Dao for the old data structure, only temporary
 */
@Dao
abstract class LegacyDao {
    /**
     * [RoutineExerciseCrossRef]
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(routineExerciseCrossRef: RoutineExerciseCrossRef): Long

    @Delete
    abstract suspend fun delete(routineExerciseCrossRef: RoutineExerciseCrossRef)

    /**
     * [ExerciseWrapper]
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(exerciseWrapper: ExerciseWrapper): Long

    @Delete
    abstract suspend fun delete(exerciseWrapper: ExerciseWrapper)

    @Query("SELECT * FROM exercise_wrapper_table WHERE exerciseWrapperId == :id")
    abstract suspend fun getExerciseWrapperById(id: Int): ExerciseWrapper

    /**
     * [RwE]
     */

    suspend fun insert(rwe: RwE) {
        insert(rwe.routine)
        for (e in rwe.exerciseWrappers) {
            insert(e)
            insert(
                RoutineExerciseCrossRef(
                    rwe.routine.routineId,
                    e.exerciseWrapperId
                )
            )
        }
    }

    @Transaction
    @Query("SELECT * FROM routine_table")
    abstract fun getRwEs(): LiveData<List<RwE>>

    @Transaction
    @Query("SELECT * FROM routine_table WHERE routineId == :id")
    abstract suspend fun getRwEById(id: Int): RwE

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(routine: Routine): Long
}