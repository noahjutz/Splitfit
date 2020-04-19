package com.noahjutz.gymroutines.datasets

import com.noahjutz.gymroutines.models.Exercise

class ExerciseDataSource {
    companion object {
        fun createDataSet(): ArrayList<Exercise> {
            val list = ArrayList<Exercise>()
            list.add(
                Exercise(
                    123123,
                    "Jumping Jacks"
                )
            )

            list.add(
                Exercise(
                    123123,
                    "Lunges"
                )
            )
            list.add(
                Exercise(
                    123123,
                    "Push Ups"
                )
            )
            list.add(
                Exercise(
                    123123,
                    "Decline Push Ups"
                )
            )
            list.add(
                Exercise(
                    123123,
                    "Squats"
                )
            )
            list.add(
                Exercise(
                    123123,
                    "Leg Press"
                )
            )
            return list
        }
    }
}