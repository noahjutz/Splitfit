package com.noahjutz.gymroutines.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.noahjutz.gymroutines.data.domain.*
import com.noahjutz.gymroutines.data.domain.Set

@Suppress("unused")
private const val TAG = "MainDao"

/**
 * A [Dao] for the following domain model objects:
 * - Entities:
 *   - [Routine]
 *   - [Exercise]
 *   - [Set]
 * - Data classes:
 *   - [FullRoutine]
 *   - [ExerciseImpl]
 * - Associative entities
 *   - [RoutineAndExercise]
 */
@Dao
abstract class MainDao {

    /**
     * [FullRoutine]
     */

    suspend fun insert(fullRoutine: FullRoutine): Long {
        val routineId = insert(fullRoutine.routine)
        for (exerciseImpl in fullRoutine.exercises)
            insert(exerciseImpl, fullRoutine.routine.routineId)

        return routineId
    }

    suspend fun delete(fullRoutine: FullRoutine) {
        delete(fullRoutine.routine)
        for (exerciseImpl in fullRoutine.exercises)
            delete(exerciseImpl, fullRoutine.routine.routineId)
    }

    @Transaction
    @Query("SELECT * FROM routine_table")
    abstract fun getFullRoutines(): LiveData<List<FullRoutine>>

    @Transaction
    @Query("SELECT * FROM routine_table WHERE routineId == :id")
    abstract suspend fun getFullRoutine(id: Int): FullRoutine?

    /**
     * [ExerciseImpl]
     */

    private suspend fun insert(exerciseImpl: ExerciseImpl, routineId: Int) {
        insert(exerciseImpl.exercise)
        insert(RoutineAndExercise(routineId, exerciseImpl.exercise.exerciseId))
        for (set in exerciseImpl.sets) {
            insert(set)
        }
    }

    private suspend fun delete(exerciseImpl: ExerciseImpl, routineId: Int) {
        delete(exerciseImpl.exercise)
        delete(RoutineAndExercise(routineId, exerciseImpl.exercise.exerciseId))
        for (set in exerciseImpl.sets)
            delete(set)
    }

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
     * [RoutineAndExercise]
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(routineAndExercise: RoutineAndExercise): Long

    @Delete
    abstract suspend fun delete(routineAndExercise: RoutineAndExercise)

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