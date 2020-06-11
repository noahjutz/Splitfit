package com.noahjutz.gymroutines.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.noahjutz.gymroutines.data.domain.*
import com.noahjutz.gymroutines.data.domain.Set

@Suppress("unused")
private const val TAG = "RwEwSDao"

/**
 * A [Dao] for the following domain model objects:
 * [FullRoutine] [RwE] [ExerciseImpl] [Routine] [Exercise] [ExerciseWrapper] [Set]
 */
@Dao
abstract class MainDao {

    /**
     * [RwE]
     */
    // TODO: Remove RwE, replace with RwEwS

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

    // /**
    //  * [FullRoutine]
    //  */

    // suspend fun insert(rwews: FullRoutine) {
    //     insert(rwews.routine)
    //     for (ews in rwews.ews)
    //         insert(ews, rwews.routine.routineId)
    // }

    // suspend fun delete(rwews: FullRoutine) {
    //     delete(rwews.routine)
    //     for (ews in rwews.ews)
    //         delete(ews, rwews.routine.routineId)
    // }

    // @Transaction
    // @Query("SELECT * FROM routine_table")
    // abstract fun getRwEwS(): LiveData<List<FullRoutine>>

    // /**
    //  * [ExerciseImpl]
    //  */

    // private suspend fun insert(ews: ExerciseImpl, routineId: Int) {
    //     insert(ews.exercise)
    //     insert(
    //         RoutineExerciseCrossRef(
    //             routineId,
    //             ews.exercise.exerciseWrapperId
    //         )
    //     )
    //     for (set in ews.sets) {
    //         insert(Set(ews.exercise.exerciseWrapperId))
    //     }
    // }

    // private suspend fun delete(ews: ExerciseImpl, routineId: Int) {
    //     delete(ews.exercise)
    //     delete(
    //         RoutineExerciseCrossRef(
    //             routineId,
    //             ews.exercise.exerciseWrapperId
    //         )
    //     )
    //     for (set in ews.sets)
    //         delete(set)
    // }

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
     * [Routine]
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(routine: Routine): Long

    @Delete
    abstract suspend fun delete(routine: Routine)

    @Query("SELECT * FROM routine_table")
    abstract fun getRoutines(): LiveData<List<Routine>>

    /**
     * [RoutineExerciseCrossRef]
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(routineExerciseCrossRef: RoutineExerciseCrossRef): Long

    @Delete
    abstract suspend fun delete(routineExerciseCrossRef: RoutineExerciseCrossRef)

    /**
     * [Set]
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(set: Set): Long

    @Delete
    abstract suspend fun delete(set: Set)

    @Query("SELECT * FROM set_table WHERE setId == :id")
    abstract suspend fun getSetById(id: Int): Set

    @Query("SELECT * FROM set_table WHERE exerciseWrapperId == :id")
    abstract suspend fun getSetsById(id: Int): List<Set>

    /**
     * [Exercise]
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(exercise: Exercise): Long

    @Delete
    abstract suspend fun delete(exercise: Exercise)

    @Query("SELECT * FROM exercise_table")
    abstract fun getExercises(): LiveData<List<Exercise>>

    @Query("SELECT * FROM exercise_table WHERE exerciseId == :id")
    abstract fun getExercise(id: Int): Exercise?
}