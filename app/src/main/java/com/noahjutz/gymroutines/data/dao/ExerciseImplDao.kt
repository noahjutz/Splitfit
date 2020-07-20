package com.noahjutz.gymroutines.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.noahjutz.gymroutines.data.domain.ExerciseImpl

@Dao
interface ExerciseImplDao {
    @Query("SELECT * FROM exercise_holder_table WHERE routineId == :routineId")
    suspend fun getExerciseImplsIn(routineId: Int): List<ExerciseImpl>

    @Query("SELECT * FROM exercise_holder_table WHERE exerciseHolderId == :exerciseHolderId")
    suspend fun getExerciseImpl(exerciseHolderId: Int): ExerciseImpl?
}