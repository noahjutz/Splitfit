package com.noahjutz.gymroutines.ui.routines.view_routines

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.databinding.FragmentRoutinesBinding
import com.noahjutz.gymroutines.datasets.RoutineDataSource
import kotlinx.android.synthetic.main.fragment_routines.*

private const val TAG = "RoutinesFragment"

class RoutinesFragment : Fragment(),
    RoutineAdapter.OnRoutineClickListener {

    private lateinit var routineAdapter: RoutineAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentRoutinesBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_routines, container, false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initRecyclerView()
        populateViews()

        activity?.title = "Gym Routines"

        fab_add_routine.setOnClickListener { createRoutine() }
    }

    private fun populateViews() {
        // TEMPORARY get from dataset
        val list = RoutineDataSource.createDataSet()
        routineAdapter.submitList(list)
    }

    private fun createRoutine() {
        findNavController().navigate(R.id.action_viewRoutinesFragment_to_createRoutineFragment)
    }

    private fun viewRoutine(pos: Int) {
        findNavController().navigate(R.id.action_viewRoutinesFragment_to_viewRoutineFragment)
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
            layoutManager = LinearLayoutManager(this@RoutinesFragment.context)
            routineAdapter =
                RoutineAdapter(
                    this@RoutinesFragment
                )
            adapter = routineAdapter
        }
    }

    override fun onRoutineClick(pos: Int) {
        viewRoutine(pos)
    }
}
