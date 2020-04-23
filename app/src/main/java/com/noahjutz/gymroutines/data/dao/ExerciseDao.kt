package com.noahjutz.gymroutines.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.noahjutz.gymroutines.data.Exercise
import com.noahjutz.gymroutines.data.Routine

@Dao
interface ExerciseDao {
    @Insert
    fun insert(exercise: Exercise)

    @Query("DELETE FROM exercise_table")
    fun clearExercises()

    @Query("SELECT * FROM exercise_table")
    fun getExercises(): LiveData<List<Exercise>>
}