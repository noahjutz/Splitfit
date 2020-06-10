package com.noahjutz.gymroutines.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.noahjutz.gymroutines.data.*
import com.noahjutz.gymroutines.data.Set

@Suppress("unused")
private const val TAG = "RwEwSDao"

/**
 * A [Dao] for the following domain model objects:
 * [RwEwS] [RwE] [EwS] [Routine] [Exercise] [ExerciseWrapper] [Set]
 */
@Dao
abstract class RwEwSDao {

    /**
     * [RwE]
     */

    suspend fun insert(rwe: RwE) {
        insert(rwe.routine)
        for (e in rwe.exerciseWrappers) {
            insert(e)
            insert(RoutineExerciseCrossRef(rwe.routine.routineId, e.exerciseWrapperId))
        }
    }

    /**
     * [RwEwS]
     */

    // suspend fun insert(rwews: RwEwS) {
    //     insert(rwews.routine)
    //     for (ews in rwews.ews)
    //         insert(ews, rwews.routine.routineId)
    // }

    // suspend fun delete(rwews: RwEwS) {
    //     delete(rwews.routine)
    //     for (ews in rwews.ews)
    //         delete(ews, rwews.routine.routineId)
    // }

    // @Query("SELECT * FROM routine_table")
    // abstract suspend fun getRwEwS(): LiveData<List<RwEwS>>

    /**
     * [EwS]
     */

    // suspend fun insert(ews: EwS, routineId: Int) {
    //     insert(ews.exerciseWrapper)
    //     insert(RoutineExerciseCrossRef(routineId, ews.exerciseWrapper.exerciseWrapperId))
    //     for (set in ews.sets) {
    //         insert(Set(ews.exerciseWrapper.exerciseWrapperId))
    //     }
    // }

    // suspend fun delete(ews: EwS, routineId: Int) {
    //     delete(ews.exerciseWrapper)
    //     delete(RoutineExerciseCrossRef(routineId, ews.exerciseWrapper.exerciseWrapperId))
    //     for (set in ews.sets)
    //         delete(set)
    // }

    /**
     * [ExerciseWrapper]
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(exerciseWrapper: ExerciseWrapper)

    @Delete
    abstract suspend fun delete(exerciseWrapper: ExerciseWrapper)

    /**
     * [Routine]
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(routine: Routine)

    @Delete
    abstract suspend fun delete(routine: Routine)

    @Query("SELECT * FROM routine_table")
    abstract fun getRoutines(): LiveData<List<Routine>>

    /**
     * [RoutineExerciseCrossRef]
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(routineExerciseCrossRef: RoutineExerciseCrossRef)

    @Delete
    abstract suspend fun delete(routineExerciseCrossRef: RoutineExerciseCrossRef)

    /**
     * [Set]
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(set: Set)

    @Delete
    abstract suspend fun delete(set: Set)

    /**
     * [Exercise]
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(exercise: Exercise)

    @Delete
    abstract suspend fun delete(exercise: Exercise)

    @Query("SELECT * FROM exercise_table")
    abstract fun getExercises(): LiveData<List<Exercise>>

    @Query("SELECT * FROM exercise_table WHERE exerciseId == :id")
    abstract fun getExercise(id: Int): Exercise?
}