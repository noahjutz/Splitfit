package com.noahjutz.gymroutines

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.noahjutz.gymroutines.models.Exercise
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

        val sharedPrefs = getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        nextId = sharedPrefs.getInt(PREF_NEXT_ID, 0)

        val exercise = intent.getParcelableExtra<Exercise>(EXTRA_EXERCISE)
        val pos = intent.getIntExtra(EXTRA_POS, -1)
        edit_exercise_name.setText(exercise?.name ?: "")

        fab_save_exercise.setOnClickListener {
            val exerciseToPass = Exercise(edit_exercise_name.text.toString(), exercise?.id ?: nextId)
            intent = Intent().apply {
                putExtra(EXTRA_EXERCISE, exerciseToPass)
                putExtra(EXTRA_POS, pos)
            }
            if (exercise == null) {
                nextId++
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

    }

    override fun onPause() {
        super.onPause()
        val sharedPrefs = getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        sharedPrefs.edit()
            .putInt(PREF_NEXT_ID, nextId)
            .apply()
    }
}
