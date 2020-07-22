package com.noahjutz.gymroutines.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.noahjutz.gymroutines.data.dao.*
import com.noahjutz.gymroutines.data.domain.Exercise
import com.noahjutz.gymroutines.data.domain.ExerciseHolder
import com.noahjutz.gymroutines.data.domain.Routine
import com.noahjutz.gymroutines.data.domain.Set

@Database(
    entities = [
        Exercise::class,
        Routine::class,
        ExerciseHolder::class,
        Set::class
    ],
    version = 20
)
abstract class AppDatabase : RoomDatabase() {
    abstract val exerciseDao: ExerciseDao
    abstract val exerciseHolderDao: ExerciseHolderDao
    abstract val exerciseImplDao: ExerciseImplDao
    abstract val fullRoutineDao: FullRoutineDao
    abstract val routineDao: RoutineDao
    abstract val setDao: SetDao
}
