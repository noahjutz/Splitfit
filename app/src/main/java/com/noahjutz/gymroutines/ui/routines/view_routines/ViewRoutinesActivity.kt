package com.noahjutz.gymroutines.ui.routines.view_routines

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.datasets.RoutineDataSource
import com.noahjutz.gymroutines.models.Routine
import kotlinx.android.synthetic.main.activity_view_routines.*

private const val TAG = "ViewRoutinesActivity"

class MainActivity : AppCompatActivity(),
    RoutineRecyclerAdapter.OnRoutineClickListener {

    private lateinit var routineAdapter: RoutineRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_routines)

        initRecyclerView()
        populateViews()

        fab_add_routine.setOnClickListener { createRoutine() }
    }

    private fun populateViews() {
        Log.d(TAG, "populateViews(): called")
        // TODO
    }

    private fun createRoutine() {
        Log.d(TAG, "createRoutine(): called")
        // TODO
    }

    private fun viewRoutine(pos: Int) {
        Log.d(TAG, "viewRoutine(): called at $pos")
        // TODO
    }

    private fun deleteRoutine(pos: Int) {
        Log.d(TAG, "deleteRoutine(): called at $pos")
        // TODO
    }

    private fun editRoutine(pos: Int) {
        Log.d(TAG, "editRoutine: called at $pos")
        // TODO: Launch [CreateRoutineActivity]
    }

    private fun initRecyclerView() {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            routineAdapter =
                RoutineRecyclerAdapter(
                    this@MainActivity
                )
            adapter = routineAdapter
        }
    }

    override fun onRoutineClick(pos: Int) {
        viewRoutine(pos)
    }
}
