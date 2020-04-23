package com.noahjutz.gymroutines.ui.exercises

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.noahjutz.gymroutines.R
import kotlinx.android.synthetic.main.activity_create_exercise.*

private const val TAG = "CreateExerciseActivity"

class CreateExerciseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_exercise)

        fab_save_exercise.setOnClickListener { saveExercise() }
    }

    private fun saveExercise() {
        // TODO: Save to database
        finish()
    }
}
