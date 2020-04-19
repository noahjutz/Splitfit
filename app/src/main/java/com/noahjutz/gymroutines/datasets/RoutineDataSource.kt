package com.noahjutz.gymroutines.datasets

import com.noahjutz.gymroutines.models.Routine

class RoutineDataSource {
    companion object {
        fun createDataSet(): ArrayList<Routine> {
            val list = ArrayList<Routine>()
            list.add(
                Routine(
                    "Push",
                    "Push Exercises (Triceps, Chest, etc)"
                )
            )

            list.add(
                Routine(
                    "Pull",
                    "Pull Exercises (Back, Biceps, etc)"
                )
            )
            list.add(
                Routine(
                    "Legs",
                    "Leg Exercises (Glutes, Hamstrings, Quads)"
                )
            )
            list.add(
                Routine(
                    "Upper",
                    "Part of Upper/Lower Body Split"
                )
            )
            list.add(
                Routine(
                    "Lower",
                    "Part of Upper/Lower Body Split"
                )
            )
            list.add(
                Routine(
                    "Whole Body",
                    "3x / Week"
                )
            )
            return list
        }
    }
}