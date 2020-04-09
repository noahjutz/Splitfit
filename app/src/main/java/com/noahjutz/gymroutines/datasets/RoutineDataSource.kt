package com.noahjutz.gymroutines.datasets

import com.noahjutz.gymroutines.models.Routine

class RoutineDataSource {
    companion object {
        fun createDataSet(): ArrayList<Routine> {
            val list = ArrayList<Routine>()
            list.add(
                Routine(
                    "Push",
                    "Push Exercises (Triceps, Chest, etc)",
                    "[{\"name\":\"Push Ups\"}]"
                )
            )

            list.add(
                Routine(
                    "Pull",
                    "Pull Exercises (Back, Biceps, etc)",
                    "[{\"name\":\"Dips\"},{\"name\":\"Squats\"}]"
                )
            )
            list.add(
                Routine(
                    "Legs",
                    "Leg Exercises (Glutes, Hamstrings, Quads)",
                    "[{\"name\":\"Pull Ups\"}]"
                )
            )
            list.add(
                Routine(
                    "Upper",
                    "Part of Upper/Lower Body Split",
                    "[{\"name\":\"Leg Press\"}]"
                )
            )
            list.add(
                Routine(
                    "Lower",
                    "Part of Upper/Lower Body Split",
                    "[{\"name\":\"Lateral Raise\"}]"
                )
            )
            list.add(
                Routine(
                    "Whole Body",
                    "3x / Week",
                    "[{\"name\":\"Pike Push Ups\"}]"
                )
            )
            return list
        }
    }
}