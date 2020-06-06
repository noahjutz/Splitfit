package com.noahjutz.gymroutines.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
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
     * [RwEwS]
     */
    suspend fun insert(rwews: RwEwS) {
        // Insert the [Routine]
        insert(rwews.routine)

        for (ews in rwews.ews)
            insert(ews, rwews.routine.routineId)
    }

    suspend fun delete(rwews: RwEwS) {
        delete(rwews.routine)
        for (ews in rwews.ews)
            delete(ews, rwews.routine.routineId)
    }

    /**
     * [EwS]
     */

    suspend fun insert(ews: EwS, routineId: Int) {
        // Insert the [ExerciseWrapper]s
        insert(ews.exerciseWrapper)
        // Insert the [RoutineExerciseCrossRef]s
        insert(
            RoutineExerciseCrossRef(
                routineId,
                ews.exerciseWrapper.exerciseWrapperId
            )
        )
        // Insert the [Set]s
        for (set in ews.sets) {
            insert(Set(ews.exerciseWrapper.exerciseWrapperId))
        }
    }

    suspend fun delete(ews: EwS, routineId: Int) {
        delete(ews.exerciseWrapper)
        delete(RoutineExerciseCrossRef(routineId, ews.exerciseWrapper.exerciseWrapperId))
        for (set in ews.sets)
            delete(set)
    }

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

    // TODO: Migrate from [ExerciseDao] here.
}