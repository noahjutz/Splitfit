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

package com.noahjutz.gymroutines.util

import com.noahjutz.gymroutines.data.domain.*
import com.noahjutz.gymroutines.data.domain.Set

/**
 * Builders for classes in [com.noahjutz.gymroutines.data.domain]
 */
data class FullRoutineBuilder(
    val routine: Routine = Routine(),
    val exercises: List<ExerciseImpl> = listOf()
) {
    fun build() = FullRoutine(routine, exercises)
}

data class ExerciseImplBuilder(
    val routine: Routine = Routine(),
    val exercise: Exercise = Exercise(),
    val setGroup: SetGroup = SetGroup(exercise.exerciseId, routine.routineId),
    val sets: List<Set> = listOf()
) {
    fun build() = ExerciseImpl(setGroup, exercise, sets)
}
