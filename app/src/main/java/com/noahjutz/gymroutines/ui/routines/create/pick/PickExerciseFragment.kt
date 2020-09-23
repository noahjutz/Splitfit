/*
 * GymRoutines
 * Copyright (C) 2020  Noah Jutz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.noahjutz.gymroutines.ui.routines.create.pick

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.data.domain.Exercise
import com.noahjutz.gymroutines.databinding.FragmentPickExerciseBinding
import com.noahjutz.gymroutines.ui.exercises.ExercisesViewModel
import com.noahjutz.gymroutines.util.MarginItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@Suppress("unused")
private const val TAG = "PickExerciseFragment"

@AndroidEntryPoint
class PickExerciseFragment : Fragment(), ExercisesAdapter.ExerciseListener {

    private val exercisesViewModel: ExercisesViewModel by viewModels()
    private val sharedExerciseViewModel: SharedExerciseViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView

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
        initActivity()
        initRecyclerView()
        initViewModel()
    }

    private fun initViewModel() {
        exercisesViewModel.exercises.observe(viewLifecycleOwner) { exercises ->
            adapter.items = exercises.filter { !it.hidden }
        }
    }

    private fun initActivity() {
        requireActivity().apply {
            title = "Pick Exercise"

            recyclerView = findViewById(R.id.recycler_view)
        }
    }

    private fun initRecyclerView() {
        recyclerView.apply {
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
}
