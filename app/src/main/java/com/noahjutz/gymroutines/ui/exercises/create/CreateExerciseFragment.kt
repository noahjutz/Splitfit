package com.noahjutz.gymroutines.ui.exercises.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.databinding.FragmentCreateExerciseBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

@AndroidEntryPoint
class CreateExerciseFragment : Fragment() {

    private val viewModel: CreateExerciseViewModel by viewModels()

    private val args: CreateExerciseFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentCreateExerciseBinding>(
            inflater,
            R.layout.fragment_create_exercise,
            container,
            false
        ).apply {
            fragment = this@CreateExerciseFragment
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initActivity()
        initViewModel()
    }

    private fun initActivity() {
        requireActivity().apply {
            title = if (args.exerciseId == -1) "Create Exercise" else "Edit Exercise"
            bottom_nav.visibility = GONE
        }
    }

    private fun initViewModel() {
        viewModel.exercise.observe(viewLifecycleOwner, Observer {})
    }
}
