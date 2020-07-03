package com.noahjutz.gymroutines.ui.exercises

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.google.android.material.snackbar.Snackbar
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.data.domain.Exercise
import com.noahjutz.gymroutines.databinding.FragmentExercisesBinding
import com.noahjutz.gymroutines.util.InjectorUtils
import com.noahjutz.gymroutines.util.MarginItemDecoration
import com.noahjutz.gymroutines.util.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_exercises.*
import kotlinx.android.synthetic.main.fragment_routines.*
import kotlinx.android.synthetic.main.fragment_routines.fab_pick_exercises
import kotlinx.android.synthetic.main.fragment_routines.recycler_view

@Suppress("unused")
private const val TAG = "ExercisesFragment"

class ExercisesFragment : Fragment() {

    private val viewModel: ExercisesViewModel by viewModels { viewModelFactory }
    private val viewModelFactory: ViewModelFactory by lazy {
        InjectorUtils.provideViewModelFactory(requireActivity().application)
    }

    private lateinit var binding: FragmentExercisesBinding
    private lateinit var adapter: ExercisesAdapter

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

        initActivity()
        initRecyclerView()
        initViewModel()
        initBinding()
    }

    private fun initActivity() {
        requireActivity().apply {
            title = "Exercises"
            bottom_nav.visibility = VISIBLE
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
                val exercise = adapter.getExerciseAt(viewHolder.adapterPosition)
                viewModel.hide(exercise, true)
                Snackbar.make(recycler_view, "Deleted ${exercise.name}", Snackbar.LENGTH_SHORT)
                    .setAction("Undo") {
                        viewModel.hide(exercise, false)
                    }
                    .setAnchorView(fab_pick_exercises)
                    .show()
            }
        }

        val onItemClickListener = object : ExercisesAdapter.OnExerciseClickListener {
            override fun onExerciseClick(exercise: Exercise) {
                val action = ExercisesFragmentDirections.addExercise(exercise.exerciseId)
                findNavController().navigate(action)
            }

            override fun onExerciseLongClick(exercise: Exercise) {}
        }

        adapter = ExercisesAdapter(onItemClickListener)

        recycler_view.apply {
            adapter = this@ExercisesFragment.adapter
            layoutManager = LinearLayoutManager(this@ExercisesFragment.requireContext())
            setHasFixedSize(true)
            addItemDecoration(
                MarginItemDecoration(
                    resources.getDimension(R.dimen.any_margin_default).toInt()
                )
            )
            ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(this)
        }
    }

    private fun initViewModel() {
        viewModel.exercises.observe(
            viewLifecycleOwner,
            Observer { exercises ->
                val e = exercises.filter { !it.hidden }
                adapter.submitList(e)

                val v = if (e.isEmpty()) VISIBLE else GONE
                showEmptyScreen(v)
            }
        )
    }

    private fun showEmptyScreen(visibility: Int) {
        TransitionManager.beginDelayedTransition(exercises_root, AutoTransition())
        exercises_empty.visibility = visibility
    }

    private fun initBinding() {
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.fragment = this
    }

    fun addExercise() {
        val action = ExercisesFragmentDirections.addExercise(-1)
        findNavController().navigate(action)
    }
}
