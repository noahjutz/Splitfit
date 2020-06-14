package com.noahjutz.gymroutines.data.domain

import androidx.room.*

/**
 * Utility classes for using the class hierarchy in code.
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
 *
 */

/**
 * [ExerciseHolder] with [Exercise] and [Set]s
 */
data class ExerciseImpl(
    @Embedded val exerciseHolder: ExerciseHolder,
    @Relation(
        entity = Exercise::class,
        parentColumn = "exerciseId",
        entityColumn = "exerciseId"
    ) val exercise: Exercise,
    @Relation(
        entity = Set::class,
        parentColumn = "exerciseHolderId",
        entityColumn = "exerciseHolderId"
    ) var sets: List<Set>
)

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
)