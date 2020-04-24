package com.noahjutz.gymroutines.ui.exercises

import android.os.Bundle
import com.noahjutz.gymroutines.databinding.*
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
import com.noahjutz.gymroutines.InjectorUtils
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.data.Exercise
import com.noahjutz.gymroutines.data.Repository
import kotlinx.android.synthetic.main.fragment_exercises.*
import kotlinx.android.synthetic.main.fragment_exercises.debug_button_clear
import kotlinx.android.synthetic.main.fragment_exercises.debug_button_insert
import kotlinx.android.synthetic.main.fragment_exercises.debug_textview

private const val TAG = "ExercisesActivity"

class ExercisesFragment : Fragment() {

    private val viewModel: ExercisesViewModel by viewModels { viewModelFactory }
    private lateinit var viewModelFactory: ViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentExercisesBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_exercises, container, false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initRecyclerView()
        initViews()
        initViewModel()

        activity?.title = "View Exercises"
    }

    private fun initViewModel() {
        viewModelFactory = InjectorUtils.provideViewModelFactory(requireActivity().application)
        viewModel.getExercises().observe(viewLifecycleOwner, Observer { exercises ->
            debug_textview.text = viewModel.debugText
        })
    }

    private fun initViews() {
        fab_add_exercise.setOnClickListener { addExercise() }
        debug_button_insert.setOnClickListener { viewModel.insertExercise(Exercise("Lunge")) }
        debug_button_clear.setOnClickListener { viewModel.clearExercises() }

        // TODO: Populate Views
    }

    private fun addExercise() {
        findNavController().navigate(R.id.add_exercise)
    }

    private fun initRecyclerView() {
        // TODO
    }
}
