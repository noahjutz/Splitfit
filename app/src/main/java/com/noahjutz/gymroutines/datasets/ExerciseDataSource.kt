package com.noahjutz.gymroutines.datasets

import com.noahjutz.gymroutines.models.Exercise

class ExerciseDataSource {
    companion object {
        fun createDataSet(): ArrayList<Exercise> {
            val list = ArrayList<Exercise>()
            list.add(
                Exercise(
                    "Jumping Jacks",
                    123123
                )
            )

            list.add(
                Exercise(
                    "Lunges",
                    123123
                )
            )
            list.add(
                Exercise(
                    "Push Ups",
                    123123
                )
            )
            list.add(
                Exercise(
                    "Decline Push Ups",
                    123123
                )
            )
            list.add(
                Exercise(
                    "Squats",
                    123123
                )
            )
            list.add(
                Exercise(
                    "Leg Press",
                    123123
                )
            )
            return list
        }
    }
}