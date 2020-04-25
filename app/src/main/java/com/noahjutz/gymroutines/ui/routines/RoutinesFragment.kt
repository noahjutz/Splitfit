package com.noahjutz.gymroutines.ui.routines

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.noahjutz.gymroutines.ViewModelFactory
import com.noahjutz.gymroutines.InjectorUtils
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.data.Routine
import com.noahjutz.gymroutines.databinding.FragmentRoutinesBinding
import kotlinx.android.synthetic.main.fragment_routines.*

private const val TAG = "RoutinesFragment"

class RoutinesFragment : Fragment() {

    private val viewModel: RoutinesViewModel by viewModels { viewModelFactory }
    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var binding: FragmentRoutinesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
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

    private fun initViews() {
        // Debug
        debug_button_clear.setOnClickListener { viewModel.clearRoutines() }
        debug_button_insert.setOnClickListener { viewModel.insertRoutine(Routine("Legs")) }

        fab_add_routine.setOnClickListener { addRoutine() }

        // TODO: Populate views
    }

    private fun initViewModel() {
        viewModelFactory = InjectorUtils.provideViewModelFactory(requireActivity().application)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.routines.observe(viewLifecycleOwner, Observer { routines ->
            Log.d(TAG, "Routines: $routines")
            viewModel.updateDebugText()
        })
    }

    private fun addRoutine() {
        findNavController().navigate(R.id.add_routine)
    }

    private fun initRecyclerView() {
        // TODO
    }
}
