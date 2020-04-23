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
    }

    private fun initViewModel() {
        viewModelFactory = ViewModelFactory(Repository(requireActivity().application))
        viewModel.getRoutines().observe(viewLifecycleOwner, Observer { routines ->
            debug_textview.text = viewModel.debugText
        })
    }

    private fun initViews() {
        // Debug
        debug_button_clear.setOnClickListener { viewModel.clearRoutines() }
        debug_button_insert.setOnClickListener { viewModel.insertRoutine(Routine("Legs")) }

        // View Listeners
        fab_add_routine.setOnClickListener { addRoutine() }

        // TODO: Populate views
    }

    private fun addRoutine() {
        findNavController().navigate(R.id.add_routine)
    }

    private fun initRecyclerView() {
        // TODO
    }
}
