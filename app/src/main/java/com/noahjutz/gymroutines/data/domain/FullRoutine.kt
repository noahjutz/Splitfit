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

package com.noahjutz.gymroutines.data.domain

import androidx.room.Embedded
import androidx.room.Relation
import com.noahjutz.gymroutines.util.Equatable

/**
 * Utility class hierarchy:
 *
 *                   +-------------+
 *               1 +-+ FullRoutine +-+ 1
 *                 | +-------------+ |
 *               1 |                 | n
 * +---------------+---+      +------+-------+
 * | @Embedded Routine |  1 +-+ ExerciseImpl +-+ 1
 * +-------------------+    | +------+-------+ |
 *                          |      1 |         |
 *                        1 |      n |         | 1
 * +------------------------+-+  +---+---+  +--+-------+
 * | @Embedded ExerciseHolder |  |  Set  |  | Exercise |
 * +--------------------------+  +-------+  +----------+
 *
 * Database hierarchy:
 *
 *      +---------+
 *      | Routine +--+ 1
 *      +---------+  |
 *                   | n
 *      +------------+---+
 *  n +-+ ExerciseHolder +-+ 1
 *    | +----------------+ |
 *  1 |                    | n
 * +--+-------+         +--+--+
 * | Exercise |         | Set |
 * +----------+         +-----+
 */

/**
 * [Routine] with [ExerciseImpl]s
 */
data class FullRoutine(
    @Embedded val routine: Routine,
    @Relation(
        entity = SetGroup::class,
        parentColumn = "routineId",
        entityColumn = "routineId"
    ) val exercises: List<ExerciseImpl>
) : Equatable
