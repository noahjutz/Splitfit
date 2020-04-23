package com.noahjutz.gymroutines.ui.routines

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
import kotlinx.android.synthetic.main.fragment_routines.*

private const val TAG = "RoutinesFragment"

class RoutinesFragment : Fragment() {


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

        activity?.title = "View Routines"

        fab_add_routine.setOnClickListener { createRoutine() }
    }

    private fun populateViews() {
        // TODO
    }

    private fun createRoutine() {
        findNavController().navigate(R.id.add_routine)
    }

    private fun viewRoutine(pos: Int) {
        findNavController().navigate(R.id.add_routine)
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
        // TODO
    }
}
