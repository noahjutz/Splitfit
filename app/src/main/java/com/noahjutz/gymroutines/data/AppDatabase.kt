package com.noahjutz.gymroutines.data

import android.content.Context
import androidx.room.Database
import androidx.room.R
import androidx.room.Room
import androidx.room.RoomDatabase
import com.noahjutz.gymroutines.data.dao.*

@Database(
    entities = [
        Exercise::class,
        Routine::class,
        RoutineExerciseCrossRef::class,
        ExerciseWrapper::class,
        Set::class
    ],
    version = 6
)
abstract class AppDatabase : RoomDatabase() {
    abstract val exerciseDao: ExerciseDao
    abstract val routineDao: RoutineDao
    abstract val exerciseWrapperDao: ExerciseWrapperDao
    abstract val setDao: SetDao
    abstract val rweDao: RweDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "workout_routines_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}