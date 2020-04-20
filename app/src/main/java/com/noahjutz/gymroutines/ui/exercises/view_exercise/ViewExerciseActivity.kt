package com.noahjutz.gymroutines.ui.exercises.view_exercise

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.noahjutz.gymroutines.R

private const val TAG = "ViewExercisesActivity"

class ViewExerciseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_exercise)

        populateViews()
    }

    private fun populateViews() {
        Log.d(TAG, "populateViews(): called")
        // TODO
    }
}
