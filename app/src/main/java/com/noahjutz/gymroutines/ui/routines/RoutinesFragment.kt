package com.noahjutz.gymroutines.ui.routines

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.mvvmtutorial.viewmodel.ViewModelFactory
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.data.Repository
import com.noahjutz.gymroutines.data.Routine
import com.noahjutz.gymroutines.databinding.FragmentRoutinesBinding
import kotlinx.android.synthetic.main.fragment_routines.*

private const val TAG = "RoutinesFragment"

class RoutinesFragment : Fragment() {

    private val viewModel: RoutinesViewModel by viewModels { viewModelFactory }
    private lateinit var viewModelFactory: ViewModelFactory

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
        initViews()
        initViewModel()

        requireActivity().title = "View Routines"

        fab_add_routine.setOnClickListener { createRoutine() }
    }

    private fun initViewModel() {
        viewModelFactory = ViewModelFactory(Repository(requireActivity().application))
        viewModel.getAllRoutines().observe(viewLifecycleOwner, Observer { routines ->
            if (routines.isEmpty()) {
                debug_textview.text = "Empty List :("
            } else {
                val sb = StringBuilder()
                for (routine: Routine in routines) {
                    sb.append(routine)
                        .append("\n")
                }
                debug_textview.text = sb.toString()
            }
        })
    }

    private fun initViews() {
        // View Listeners
        debug_button_clear.setOnClickListener { viewModel.clearRoutines() }
        debug_button_insert.setOnClickListener { viewModel.insertRoutine(Routine("Legs")) }
        // TODO: Populate views
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
