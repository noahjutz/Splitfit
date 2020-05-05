package com.noahjutz.gymroutines.ui.exercises.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.databinding.FragmentCreateExerciseBinding
import com.noahjutz.gymroutines.util.CreateViewModelFactory
import com.noahjutz.gymroutines.util.InjectorUtils
import com.noahjutz.gymroutines.util.ViewModelFactory
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
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initActivity()
        initBinding()
    }

    private fun initActivity() {
        requireActivity().apply {
            title = if (args.exerciseId == -1) "Create Exercise" else "Edit Exercise"
            bottom_nav.visibility = GONE
        }
    }

    private fun initBinding() {
        binding.fragment = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel
    }
}
