package com.noahjutz.gymroutines.ui.exercises.create_exercise

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.noahjutz.gymroutines.R
import kotlinx.android.synthetic.main.activity_create_exercise.*

private const val TAG = "CreateRoutineActivity"

class CreateExerciseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_exercise)

        populateViews()

        fab_save_exercise.setOnClickListener { saveExercise() }
    }

    private fun populateViews() {
        Log.d(TAG, "populateViews(): called")
        // TODO: Populate views if called as edit exercise activity
    }

    private fun saveExercise() {
        Log.d(TAG, "saveExercise(): called")
        // TODO: Save to database
    }
}
