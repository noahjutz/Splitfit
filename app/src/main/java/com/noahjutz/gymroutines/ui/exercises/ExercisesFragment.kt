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
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.google.android.material.snackbar.Snackbar
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.data.domain.Exercise
import com.noahjutz.gymroutines.databinding.FragmentExercisesBinding
import com.noahjutz.gymroutines.util.InjectorUtils
import com.noahjutz.gymroutines.util.ItemTouchHelperBuilder
import com.noahjutz.gymroutines.util.MarginItemDecoration
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_exercises.*
import kotlinx.android.synthetic.main.fragment_routines.fab_pick_exercises
import kotlinx.android.synthetic.main.fragment_routines.recycler_view

@Suppress("unused")
private const val TAG = "ExercisesFragment"

class ExercisesFragment : Fragment(), ExercisesAdapter.OnExerciseClickListener {

    private val viewModel: ExercisesViewModel by viewModels {
        InjectorUtils.provideViewModelFactory(requireActivity().application)
    }

    private lateinit var adapter: ExercisesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentExercisesBinding>(
            inflater, R.layout.fragment_exercises, container, false
        ).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
            fragment = this@ExercisesFragment
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initActivity()
        initRecyclerView()
        initViewModel()
    }

    private fun initActivity() {
        requireActivity().apply {
            title = "Exercises"
            bottom_nav.visibility = VISIBLE
        }
    }

    private fun initRecyclerView() {
        val itemTouchHelper = ItemTouchHelperBuilder(
            swipeDirs = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
            onSwipedCall = { viewHolder, _, _ -> deleteExercise(viewHolder.adapterPosition) }
        ).build()

        adapter = ExercisesAdapter(this)

        recycler_view.apply {
            adapter = this@ExercisesFragment.adapter
            layoutManager = LinearLayoutManager(this@ExercisesFragment.requireContext())
            setHasFixedSize(true)
            addItemDecoration(
                MarginItemDecoration(
                    resources.getDimension(R.dimen.any_margin_default).toInt()
                )
            )
            itemTouchHelper.attachToRecyclerView(this)
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

    fun addExercise() {
        val action = ExercisesFragmentDirections.addExercise(-1)
        findNavController().navigate(action)
    }

    private fun deleteExercise(position: Int) {
        val exercise = adapter.getExerciseAt(position)
        viewModel.hide(exercise, true)
        Snackbar.make(recycler_view, "Deleted ${exercise.name}", Snackbar.LENGTH_SHORT)
            .setAction("Undo") {
                viewModel.hide(exercise, false)
            }
            .setAnchorView(fab_pick_exercises)
            .show()
    }

    override fun onExerciseClick(exercise: Exercise) {
        val action = ExercisesFragmentDirections.addExercise(exercise.exerciseId)
        findNavController().navigate(action)
    }
}
