package com.noahjutz.gymroutines.ui.routines

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.databinding.FragmentCreateRoutineBinding
import kotlinx.android.synthetic.main.fragment_create_routine.*

private const val TAG = "CreateRoutineFragment"

class CreateRoutineFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentCreateRoutineBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_create_routine, container, false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initRecyclerView()
        populateViews()

        activity?.title = "Create Routine"

        button_add_exercise.setOnClickListener { addExercise() }
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
        // findNavController().navigate(R.id.save_routine)

    }

    private fun viewExercise(pos: Int) {
        Log.d(TAG, "viewExercise(): called at $pos")
        // TODO: Launch [ViewExerciseActivity]
    }

    private fun initRecyclerView() {
        // TODO
    }
}
