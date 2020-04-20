package com.noahjutz.gymroutines.ui.routines.view_routine

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.noahjutz.gymroutines.R
import kotlinx.android.synthetic.main.activity_view_routine.*

private const val TAG = "ViewRoutineActivity"

class ViewRoutineActivity : AppCompatActivity(),
    ExerciseRecyclerAdapter.OnExerciseClickListener {

    private lateinit var exerciseAdapter: ExerciseRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_routine)

        initRecyclerView()
        populateViews()
    }

    private fun viewExercise(pos: Int) {
        Log.d(TAG, "viewExercise(): called at $pos")
        // TODO: Launch [ViewExerciseActivity]
    }

    private fun editRoutine() {
        Log.d(TAG, "editRoutine(): called")
        // TODO
    }

    private fun deleteRoutine() {
        Log.d(TAG, "deleteRoutine: called")
        // TODO
    }

    private fun populateViews() {
        Log.d(TAG, "populateViews: called")
        // TODO
    }

    private fun initRecyclerView() {
        recycler_view_exercises.apply {
            layoutManager = LinearLayoutManager(this@ViewRoutineActivity)
            exerciseAdapter = ExerciseRecyclerAdapter(this@ViewRoutineActivity)
            adapter = exerciseAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_view_routine, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_routine -> {
                deleteRoutine()
                true
            }
            R.id.edit_routine -> {
                editRoutine()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onExerciseClick(pos: Int) {
        viewExercise(pos)
    }
}
