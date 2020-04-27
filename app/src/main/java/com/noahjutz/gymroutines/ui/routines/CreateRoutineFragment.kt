package com.noahjutz.gymroutines.ui.routines

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navArgs
import com.noahjutz.gymroutines.InjectorUtils
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.ViewModelFactory
import com.noahjutz.gymroutines.data.Routine
import com.noahjutz.gymroutines.databinding.FragmentCreateRoutineBinding
import kotlinx.android.synthetic.main.fragment_create_routine.*

class CreateRoutineFragment : Fragment() {

    private lateinit var binding: FragmentCreateRoutineBinding

    private val args: CreateRoutineFragmentArgs by navArgs()
    private val viewModel: CreateRoutineViewModel by viewModels { viewModelFactory }
    private val viewModelFactory: ViewModelFactory by lazy {
        InjectorUtils.provideViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_create_routine,
            container,
            false
        )
        binding.fragment = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        requireActivity().apply {
            actionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
            title = if (args.routineId == -1) "Create Routine" else "Edit Routine"
        }

        if (args.routineId != -1) {
            val routine = viewModel.getRoutineById(args.routineId)
            edit_name.setText(routine.name)
            edit_description.setText(routine.description)
        }
    }

    fun saveRoutine() {
        if (edit_name.text.trim().isEmpty()) return

        if (args.routineId != -1) {
            val routine = viewModel.getRoutineById(args.routineId).apply {
                name = edit_name.text.toString().trim()
                description = edit_description.text.toString().trim()
            }
            viewModel.updateRoutine(routine)
        } else {
            val routine = Routine(
                edit_name.text.toString().trim(),
                edit_description.text.toString().trim()
            )
            viewModel.insertRoutine(routine)
        }
        val action = CreateRoutineFragmentDirections.saveRoutine()
        findNavController().navigate(action)
    }
}
