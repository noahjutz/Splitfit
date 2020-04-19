package com.noahjutz.gymroutines.ui.exercises.create_exercise

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.noahjutz.gymroutines.ui.exercises.view_exercises.EXTRA_EXERCISE
import com.noahjutz.gymroutines.ui.routines.view_routines.EXTRA_POS
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.models.Exercise
import com.noahjutz.gymroutines.ui.exercises.view_exercises.EXTRA_EXERCISE_ID
import com.noahjutz.gymroutines.ui.exercises.view_exercises.EXTRA_EXERCISE_NAME
import kotlinx.android.synthetic.main.activity_create_exercise.*
import kotlin.properties.Delegates

private const val TAG = "CreateRoutineActivity"

private const val PREFS = "NEXT_ID_PREFS"
private const val PREF_NEXT_ID = "NEXT_ID"

class CreateExerciseActivity : AppCompatActivity() {

    private var nextId by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_exercise)

        val exerciseName = intent.getStringExtra(EXTRA_EXERCISE_NAME)
        val pos = intent.getIntExtra(EXTRA_POS, -1)
        edit_exercise_name.setText(exerciseName ?: "")

        fab_save_exercise.setOnClickListener {
            intent = Intent().apply {
                putExtra(EXTRA_EXERCISE_ID, nextId)
                putExtra(EXTRA_EXERCISE_NAME, edit_exercise_name.text.toString())
                putExtra(EXTRA_POS, pos)
            }
            if (exerciseName == null) {
                nextId++
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

    }
}
