package com.noahjutz.gymroutines.ui.routines.create.pick

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.google.android.material.card.MaterialCardView
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.data.domain.Exercise
import com.noahjutz.gymroutines.databinding.FragmentPickExerciseBinding
import com.noahjutz.gymroutines.ui.exercises.ExercisesViewModel
import com.noahjutz.gymroutines.util.MarginItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_pick_exercise.*

@Suppress("unused")
private const val TAG = "PickExerciseFragment"

@AndroidEntryPoint
class PickExerciseFragment : Fragment(), ExercisesAdapter.ExerciseListener {

    private val exercisesViewModel: ExercisesViewModel by viewModels()
    private val sharedExerciseViewModel: SharedExerciseViewModel by activityViewModels()

    // TODO: Field injection
    private val adapter = ExercisesAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentPickExerciseBinding>(
        inflater, R.layout.fragment_pick_exercise, container, false
    ).apply { fragment = this@PickExerciseFragment }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initActivity()
        initViewModel()
    }

    private fun initViewModel() {
        exercisesViewModel.exercises.observe(
            viewLifecycleOwner,
            Observer { exercises ->
                val e = exercises.filter { !it.hidden }
                adapter.submitList(e)

                val v = if (e.isEmpty()) VISIBLE else GONE
                showEmptyScreen(v)
            }
        )
    }

    private fun initActivity() {
        requireActivity().apply {
            title = "Pick Exercise"
        }
    }

    private fun initRecyclerView() {
        recycler_view.apply {
            adapter = this@PickExerciseFragment.adapter
            layoutManager = LinearLayoutManager(this@PickExerciseFragment.requireContext())
            setHasFixedSize(true)
            addItemDecoration(
                MarginItemDecoration(
                    resources.getDimension(R.dimen.any_margin_default).toInt()
                )
            )
        }
    }

    override fun onExerciseClick(exercise: Exercise, card: MaterialCardView) {
        card.isChecked = (!card.isChecked)
        if (card.isChecked) sharedExerciseViewModel.addExercise(exercise)
        else sharedExerciseViewModel.removeExercise(exercise)
    }

    private fun showEmptyScreen(visibility: Int) {
        TransitionManager.beginDelayedTransition(pick_exercise_root, AutoTransition())
        pick_exercise_empty.visibility = visibility
    }
}
