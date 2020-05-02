package com.noahjutz.gymroutines.ui.exercises

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.noahjutz.gymroutines.util.InjectorUtils
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.util.ViewModelFactory
import com.noahjutz.gymroutines.databinding.FragmentCreateExerciseBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_create_exercise.*

@Suppress("unused")
private const val TAG = "CreateExerciseFragment"

class CreateExerciseFragment : Fragment() {

    private val viewModel: CreateExerciseViewModel by viewModels { viewModelFactory }
    private val viewModelFactory: ViewModelFactory by lazy {
        InjectorUtils.provideViewModelFactory(requireActivity().application)
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
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initActivity()
        initBinding()
        initViewModel()
        initViews()
    }

    private fun initViewModel() {
        viewModel.init(args.exerciseId)
    }

    private fun initActivity() {
        requireActivity().apply {
            (this as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
            title = if (args.exerciseId == -1) "Create Exercise" else "Edit Exercise"
            bottom_nav.visibility = GONE
        }
    }

    private fun initViews() {
        // TODO: Remove this and put it in ViewModel
        // if (args.exerciseId != -1) {
        //     val exercise = viewModel.getExerciseById(args.exerciseId)
        //     viewModel.name.value = exercise?.name
        //     viewModel.description.value = exercise?.description
        // }

        edit_name.editText?.doOnTextChanged { text, _, _, _ ->
            if (text?.trim().toString().isNotEmpty()) {
                edit_name.error = null
            }
        }
    }

    private fun initBinding() {
        binding.fragment = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel
    }

    fun debugShow(view: View) {
        val isVisible = if ((view as Switch).isChecked) VISIBLE else GONE
        debug_textview.visibility = isVisible
    }

    fun saveExercise() {
        if (viewModel.save()) {
            val action = CreateExerciseFragmentDirections.saveExercise()
            findNavController().navigate(action)
        } else if (viewModel.exercise.value?.name.toString().isEmpty())
            edit_name.error = "Please enter a name"
    }
}
