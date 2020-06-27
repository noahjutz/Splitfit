package com.noahjutz.gymroutines.ui.exercises.create

import android.os.Bundle
import android.util.Log
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
import com.noahjutz.gymroutines.util.CreateViewModelFactory
import com.noahjutz.gymroutines.util.InjectorUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_create_exercise.*

@Suppress("unused")
private const val TAG = "CreateExerciseFragment"

class CreateExerciseFragment : Fragment() {

    private val viewModel: CreateExerciseViewModel by viewModels { viewModelFactory }
    private val viewModelFactory: CreateViewModelFactory by lazy {
        InjectorUtils.provideCreateViewModelFactory(requireActivity().application, args.exerciseId)
    }
    private val args: CreateExerciseFragmentArgs by navArgs()

    private lateinit var binding: FragmentCreateExerciseBinding

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
        initBinding()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initActivity()
        initViewModel()
        initPickers()
    }

    private fun initPickers() {
        group_base.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radio_reps -> Log.d(TAG, "Reps")
                R.id.radio_duration -> Log.d(TAG, "Duration")
            }
        }

        group_type.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radio_weight -> Log.d(TAG, "Weight")
                R.id.radio_band -> Log.d(TAG, "Band")
                R.id.radio_none -> Log.d(TAG, "None")
            }
        }

        group_resisted.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radio_assisted -> Log.d(TAG, "Assisted")
                R.id.radio_resisted -> Log.d(TAG, "Resisted")
            }
        }
    }

    private fun initBinding() {
        binding.fragment = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel
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
