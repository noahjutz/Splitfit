package com.noahjutz.gymroutines

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.noahjutz.gymroutines.models.Exercise
import kotlinx.android.synthetic.main.activity_create_exercise.*
import kotlinx.android.synthetic.main.activity_create_routine.*

class CreateExerciseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_exercise)

        val exercise = intent.getParcelableExtra<Exercise>(EXTRA_EXERCISE)
        val pos = intent.getIntExtra(EXTRA_POS, -1)
        edit_exercise_name.setText(exercise?.name ?: "")

        fab_save_exercise.setOnClickListener {
            intent = Intent().apply {
                putExtra(EXTRA_EXERCISE, Exercise(edit_exercise_name.text.toString()))
                putExtra(EXTRA_POS, pos)
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}
