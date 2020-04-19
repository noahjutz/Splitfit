package com.noahjutz.gymroutines.ui.exercises.view_exercise

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.noahjutz.gymroutines.ui.routines.view_routine.EXTRA_EXERCISE_TO_VIEW
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.models.Exercise
import kotlinx.android.synthetic.main.activity_view_exercise.*

private const val TAG = "ViewExercisesActivity"

class ViewExerciseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_exercise)

        val exercise = intent.getParcelableExtra<Exercise>(EXTRA_EXERCISE_TO_VIEW)
        view_exercise_name.text = exercise?.name ?: "Error"
    }
}
