package com.noahjutz.gymroutines.ui.exercises

import android.os.Bundle
import com.noahjutz.gymroutines.databinding.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.noahjutz.gymroutines.ViewModelFactory
import com.noahjutz.gymroutines.InjectorUtils
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.data.Exercise
import kotlinx.android.synthetic.main.fragment_exercises.*
import kotlinx.android.synthetic.main.fragment_exercises.debug_button_clear
import kotlinx.android.synthetic.main.fragment_exercises.debug_button_insert
import kotlinx.android.synthetic.main.fragment_exercises.debug_textview

private const val TAG = "ExercisesActivity"

class ExercisesFragment : Fragment() {

    private val viewModel: ExercisesViewModel by viewModels { viewModelFactory }
    private val viewModelFactory: ViewModelFactory by lazy {
        InjectorUtils.provideViewModelFactory(requireActivity().application)
    }
    private lateinit var binding: FragmentExercisesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_exercises, container, false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initRecyclerView()
        initViewModel()
        initBinding()

        requireActivity().title = "View Exercises"
    }

    private fun initRecyclerView() {
        // TODO
    }

    private fun initViewModel() {
        viewModel.exercises.observe(viewLifecycleOwner, Observer { exercises ->
            viewModel.updateDebugText()
        })
    }

    private fun initBinding() {
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.fragment = this
    }

    /**
     * Data binding click listeners
     */
    fun debugInsertExercise() {
        viewModel.insertExercise(Exercise("Lunge"))
    }

    fun addExercise() {
        findNavController().navigate(R.id.add_exercise)
    }
}
