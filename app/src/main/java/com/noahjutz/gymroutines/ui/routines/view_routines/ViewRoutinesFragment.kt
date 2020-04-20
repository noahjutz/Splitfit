package com.noahjutz.gymroutines.ui.routines.view_routines

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.databinding.FragmentViewRoutinesBinding
import kotlinx.android.synthetic.main.fragment_view_routines.*

private const val TAG = "ViewRoutinesActivity"

class ViewRoutinesFragment : Fragment(),
    RoutineRecyclerAdapter.OnRoutineClickListener {

    private lateinit var routineAdapter: RoutineRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentViewRoutinesBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_view_routines, container, false
        )

        // initRecyclerView()
        populateViews()

        // fab_add_routine.setOnClickListener { createRoutine() }
        return binding.root
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
            layoutManager = LinearLayoutManager(this@ViewRoutinesFragment.context)
            routineAdapter =
                RoutineRecyclerAdapter(
                    this@ViewRoutinesFragment
                )
            adapter = routineAdapter
        }
    }

    override fun onRoutineClick(pos: Int) {
        viewRoutine(pos)
    }
}
