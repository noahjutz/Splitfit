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
        entity = ExerciseHolder::class,
        parentColumn = "routineId",
        entityColumn = "routineId"
    ) val exercises: List<ExerciseImpl>
) : Equatable
