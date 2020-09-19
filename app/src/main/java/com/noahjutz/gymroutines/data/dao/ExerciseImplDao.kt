/*
 * GymRoutines
 * Copyright (C) 2020  Noah Jutz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.noahjutz.gymroutines.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.noahjutz.gymroutines.data.domain.ExerciseImpl

@Deprecated("See ExerciseImpl")
@Dao
interface ExerciseImplDao {
    @Query("SELECT * FROM exercise_holder_table WHERE routineId == :routineId")
    suspend fun getExerciseImplsIn(routineId: Int): List<ExerciseImpl>

    @Query("SELECT * FROM exercise_holder_table WHERE setGroupId == :exerciseHolderId")
    suspend fun getExerciseImpl(exerciseHolderId: Int): ExerciseImpl?
}
