package com.noahjutz.gymroutines.ui.exercises

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.noahjutz.gymroutines.InjectorUtils
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.ViewModelFactory
import com.noahjutz.gymroutines.data.Exercise
import com.noahjutz.gymroutines.databinding.FragmentCreateExerciseBinding
import kotlinx.android.synthetic.main.fragment_create_routine.*

class CreateExerciseFragment : Fragment() {

    private lateinit var binding: FragmentCreateExerciseBinding

    private val args: CreateExerciseFragmentArgs by navArgs()
    private val viewModel: CreateExerciseViewModel by viewModels { viewModelFactory }
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
            R.layout.fragment_create_exercise,
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
            title = if (args.exerciseId == -1) "Create Exercise" else "Edit Exercise"
        }

        if (args.exerciseId != -1) {
            val exercise = viewModel.getExerciseById(args.exerciseId)
            edit_name.setText(exercise.name)
            edit_description.setText(exercise.description)
        }
    }

    fun saveExercise() {
        if (edit_name.text.trim().isEmpty()) return

        if (args.exerciseId != -1) {
            val exercise = viewModel.getExerciseById(args.exerciseId).apply {
                name = edit_name.text.toString().trim()
                description = edit_description.text.toString().trim()
            }
            viewModel.updateExercise(exercise)
        } else {
            val exercise = Exercise(
                edit_name.text.toString().trim(),
                edit_description.text.toString().trim()
            )
            viewModel.insert(exercise)
        }
        val action = CreateExerciseFragmentDirections.saveExercise()
        findNavController().navigate(action)
    }
}
