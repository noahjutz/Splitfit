package com.noahjutz.gymroutines.ui.routines.create_routine

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.models.Exercise
import com.noahjutz.gymroutines.models.ExerciseReference
import kotlinx.android.synthetic.main.activity_create_routine.*

private const val TAG = "CreateRoutineActivity"

class CreateRoutineActivity : AppCompatActivity(),
    ExerciseRecyclerAdapter.OnExerciseClickListener {

    private lateinit var exerciseAdapter: ExerciseRecyclerAdapter
    private lateinit var allExercisesList: ArrayList<Exercise>
    private lateinit var exerciseRefList: ArrayList<ExerciseReference>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_routine)

        initRecyclerView()
        populateViews()

        button_add_exercise.setOnClickListener { addExercise() }
        fab_save_routine.setOnClickListener { saveRoutine() }
    }

    private fun populateViews() {
        Log.d(TAG, "populateViews(): called")
        // TODO: Populate views if called as edit activity
    }

    private fun addExercise() {
        Log.d(TAG, "addExercise(): called")
        // TODO: Create a pick exercise activity
        // TODO: Launch [PickExerciseActivity]
    }

    private fun saveRoutine() {
        Log.d(TAG, "saveRoutine(): called")
        // TODO: Save to Database
    }

    private fun viewExercise(pos: Int) {
        Log.d(TAG, "viewExercise(): called at $pos")
        // TODO: Launch [ViewExerciseActivity]
    }

    private fun initRecyclerView() {
        list_added_exercises.apply {
            layoutManager = LinearLayoutManager(this@CreateRoutineActivity)
            exerciseAdapter =
                ExerciseRecyclerAdapter(this@CreateRoutineActivity)
            adapter = exerciseAdapter
        }
    }

    override fun onExerciseClick(pos: Int) {
        viewExercise(pos)
    }
}
