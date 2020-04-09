package com.noahjutz.gymroutines.datasets

import com.noahjutz.gymroutines.models.Exercise

class ExerciseDataSource {
    companion object {
        fun createDataSet(): ArrayList<Exercise> {
            val list = ArrayList<Exercise>()
            list.add(
                Exercise(
                    "Jumping Jacks"
                )
            )

            list.add(
                Exercise(
                    "Lunges"
                )
            )
            list.add(
                Exercise(
                    "Push Ups"
                )
            )
            list.add(
                Exercise(
                    "Decline Push Ups"
                )
            )
            list.add(
                Exercise(
                    "Squats"
                )
            )
            list.add(
                Exercise(
                    "Leg Press"
                )
            )
            return list
        }
    }
}