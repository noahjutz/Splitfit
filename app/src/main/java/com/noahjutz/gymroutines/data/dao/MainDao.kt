package com.noahjutz.gymroutines.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.noahjutz.gymroutines.data.domain.*
import com.noahjutz.gymroutines.data.domain.Set

@Suppress("unused")
private const val TAG = "MainDao"

@Dao
abstract class MainDao {

    /**
     * [FullRoutine]
     */

    suspend fun insert(fullRoutine: FullRoutine): Long {
        val routineId = insert(fullRoutine.routine)

        val exerciseImpls = getExerciseImplsIn(routineId.toInt())
        for (e in exerciseImpls)
            delete(e)

        for (e in fullRoutine.exercises)
            insert(e)

        return routineId
    }

    suspend fun delete(fullRoutine: FullRoutine) {
        delete(fullRoutine.routine)

        val exerciseImpls = getExerciseImplsIn(fullRoutine.routine.routineId)
        for (e in exerciseImpls)
            delete(e)
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

    suspend fun insert(exerciseImpl: ExerciseImpl): Long {
        val exerciseId = insert(exerciseImpl.exerciseHolder)

        for (set in exerciseImpl.sets)
            insert(set)

        return exerciseId
    }

    private suspend fun delete(exerciseImpl: ExerciseImpl) {
        delete(exerciseImpl.exerciseHolder)

        for (set in exerciseImpl.sets)
            delete(set)
    }

    @Query("SELECT * FROM exercise_holder_table WHERE routineId == :routineId")
    abstract suspend fun getExerciseImplsIn(routineId: Int): List<ExerciseImpl>

    @Query("SELECT * FROM exercise_holder_table WHERE exerciseHolderId == :exerciseHolderId")
    abstract suspend fun getExerciseImpl(exerciseHolderId: Int): ExerciseImpl?

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
     * [Set]
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(set: Set): Long

    @Delete
    abstract suspend fun delete(set: Set)

    @Query("SELECT * FROM set_table WHERE setId == :id")
    abstract suspend fun getSetById(id: Int): Set

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

    /**
     * [ExerciseHolder]
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(exerciseHolder: ExerciseHolder): Long

    @Delete
    abstract suspend fun delete(exerciseHolder: ExerciseHolder)

    @Query("SELECT * FROM exercise_holder_table")
    abstract fun getExerciseHolders(): LiveData<List<ExerciseHolder>>
}
