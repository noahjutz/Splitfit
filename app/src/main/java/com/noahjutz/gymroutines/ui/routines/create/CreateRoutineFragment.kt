package com.noahjutz.gymroutines.ui.routines.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.data.Exercise
import com.noahjutz.gymroutines.data.ExerciseWrapper
import com.noahjutz.gymroutines.databinding.FragmentCreateRoutineBinding
import com.noahjutz.gymroutines.ui.routines.create.pick.SharedExerciseViewModel
import com.noahjutz.gymroutines.util.CreateViewModelFactory
import com.noahjutz.gymroutines.util.InjectorUtils
import com.noahjutz.gymroutines.util.MarginItemDecoration
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_create_routine.*

@Suppress("unused")
private const val TAG = "CreateRoutineFragment"

class CreateRoutineFragment : Fragment() {

    private val sharedExerciseViewModel: SharedExerciseViewModel by activityViewModels()
    private val viewModel: CreateRoutineViewModel by viewModels { viewModelFactory }
    private val viewModelFactory: CreateViewModelFactory by lazy {
        InjectorUtils.provideCreateViewModelFactory(requireActivity().application, args.routineId)
    }
    private val args: CreateRoutineFragmentArgs by navArgs()

    private lateinit var binding: FragmentCreateRoutineBinding
    private lateinit var adapter: ExercisesAdapter

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
        initBinding()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initActivity()
        initRecyclerView()
        initViewModel()
    }

    private fun initBinding() {
        binding.apply {
            fragment = this@CreateRoutineFragment
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
        }
    }

    private fun initActivity() {
        requireActivity().apply {
            title = if (args.routineId == -1) "Create Routine" else "Edit Routine"
            bottom_nav.visibility = GONE
        }
    }

    private fun initRecyclerView() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val exerciseWrapper = adapter.getExerciseWrapperAt(viewHolder.adapterPosition)
                viewModel.removeExercise(exerciseWrapper)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
                Snackbar.make(
                    recycler_view,
                    "Deleted ${exerciseWrapper.exerciseId}",
                    Snackbar.LENGTH_SHORT
                ) // TODO: Replace id with name
                    .setAction("Undo") {
                        viewModel.addExercise(exerciseWrapper)
                        adapter.notifyItemInserted(adapter.itemCount)
                    }
                    .show()
            }
        }

        val onItemClickListener = object : ExercisesAdapter.OnExerciseClickListener {
            override fun onExerciseClick(exerciseWrapper: ExerciseWrapper) {}
            override fun onExerciseLongClick(exerciseWrapper: ExerciseWrapper) {}
        }

        adapter = ExercisesAdapter(onItemClickListener)

        recycler_view.apply {
            adapter = this@CreateRoutineFragment.adapter
            layoutManager = LinearLayoutManager(this@CreateRoutineFragment.requireContext())
            addItemDecoration(
                MarginItemDecoration(resources.getDimension(R.dimen.any_margin_default).toInt())
            )
            isNestedScrollingEnabled = false
            ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(this)
        }
    }

    private fun initViewModel() {
        viewModel.rwe.observe(viewLifecycleOwner, Observer { rwe ->
            adapter.submitList(rwe.exerciseWrappers)
        })

        sharedExerciseViewModel.exercises.observe(viewLifecycleOwner, Observer { exercises ->
            val ewList = ArrayList<ExerciseWrapper>()
            for (e in exercises) ewList.add(ExerciseWrapper(e.exerciseId))
            viewModel.addExercises(ewList)

            if (exercises.isNotEmpty()) sharedExerciseViewModel.clearExercises()
        })
    }

    fun addExercise() {
        val action = CreateRoutineFragmentDirections.addExercise()
        findNavController().navigate(action)
    }
}
