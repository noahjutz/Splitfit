package com.noahjutz.gymroutines.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.noahjutz.gymroutines.data.*
import com.noahjutz.gymroutines.data.Set

@Suppress("unused")
private const val TAG = "RwEwSDao"

@Dao
abstract class RwEwSDao {
    suspend fun insert(rwews: RwEwS) {
        // Insert the [Routine]
        insert(rwews.routine)

        for (ews in rwews.ews) {
            // Insert the [ExerciseWrapper]s
            insert(ews.exerciseWrapper)
            // Insert the [RoutineExerciseCrossRef]s
            insert(
                RoutineExerciseCrossRef(
                    rwews.routine.routineId,
                    ews.exerciseWrapper.exerciseWrapperId
                )
            )
            // Insert the [Set]s
            for (set in ews.sets) {
                insert(Set(ews.exerciseWrapper.exerciseWrapperId))
            }
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(exerciseWrapper: ExerciseWrapper)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(routine: Routine)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(routineExerciseCrossRef: RoutineExerciseCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(set: Set)
}