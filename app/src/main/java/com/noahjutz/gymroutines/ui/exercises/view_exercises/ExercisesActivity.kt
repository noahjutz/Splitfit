package com.noahjutz.gymroutines.ui.exercises.view_exercises

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.noahjutz.gymroutines.R
import kotlinx.android.synthetic.main.activity_exercises.*

private const val TAG = "ExercisesActivity"

class ExercisesActivity : AppCompatActivity(), ExerciseAdapter.OnExerciseClickListener {

    private lateinit var exerciseAdapter: ExerciseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercises)

        initRecyclerView()
        populateViews()

        fab_add_exercise.setOnClickListener { createExercise() }
    }

    private fun populateViews() {
        Log.d(TAG, "populateViews(): called")
        // TODO
    }

    private fun createExercise() {
        Log.d(TAG, "createExercise(): called")
        // TODO: Launch [CreateExerciseActivity]
    }

    private fun viewExercise(pos: Int) {
        Log.d(TAG, "viewExercise(): called at $pos")
        // TODO: Launch [ViewExerciseActivity]
    }

    private fun initRecyclerView() {
        list_exercises.apply {
            layoutManager = LinearLayoutManager(this@ExercisesActivity)
            exerciseAdapter = ExerciseAdapter(this@ExercisesActivity)
            adapter = exerciseAdapter
        }
    }

    override fun onExerciseClick(pos: Int) {
        viewExercise(pos)
    }
}
