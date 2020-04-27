package com.noahjutz.gymroutines.ui.exercises

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.noahjutz.gymroutines.InjectorUtils
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.ViewModelFactory
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
        initViews()
    }

    private fun initActivity() {
        requireActivity().apply {
            (this as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
            title = if (args.exerciseId == -1) "Create Exercise" else "Edit Exercise"
            bottom_nav.visibility = GONE
        }
    }

    private fun initViews() {
        if (args.exerciseId != -1) {
            val exercise = viewModel.getExerciseById(args.exerciseId)
            viewModel.name.value = exercise?.name
            viewModel.description.value = exercise?.description
        }

        edit_name.editText?.doOnTextChanged { text, _, _, _ ->
            if (text?.trim().toString().isNotEmpty()) {
                edit_name.error = null
            }
        }
    }

    private fun initBinding() {
        binding.fragment = this
        binding.viewmodel = viewModel
    }

    fun saveExercise() {
        if (edit_name.editText?.text.toString().trim().length > 20) return
        if (edit_description.editText?.text.toString().trim().length > 500) return
        if (edit_name.editText?.text.toString().trim().isEmpty()) {
            edit_name.error = "Please enter a name"
            return
        }

        if (args.exerciseId != -1) {
            viewModel.update(args.exerciseId)
        } else {
            viewModel.insert()
        }
        val action = CreateExerciseFragmentDirections.saveExercise()
        findNavController().navigate(action)
    }
}
