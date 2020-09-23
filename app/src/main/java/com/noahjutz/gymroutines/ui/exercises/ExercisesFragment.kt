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

package com.noahjutz.gymroutines.ui.exercises

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.data.domain.Exercise
import com.noahjutz.gymroutines.databinding.FragmentExercisesBinding
import com.noahjutz.gymroutines.util.ItemTouchHelperBuilder
import com.noahjutz.gymroutines.util.MarginItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExercisesFragment : Fragment(), ExercisesAdapter.ExerciseListener {

    private val viewModel: ExercisesViewModel by viewModels()
    private val adapter = ExercisesAdapter(this)

    private lateinit var recyclerView: RecyclerView
    private lateinit var fabPickExercises: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentExercisesBinding>(
        inflater, R.layout.fragment_exercises, container, false
    ).apply {
        viewmodel = viewModel
        lifecycleOwner = viewLifecycleOwner
        fragment = this@ExercisesFragment
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initActivity()
        initRecyclerView()
        initViewModel()
    }

    private fun initActivity() {
        requireActivity().apply {
            title = "Exercises"
            findViewById<BottomNavigationView>(R.id.bottom_nav).visibility = VISIBLE

            recyclerView = findViewById(R.id.recycler_view)
            fabPickExercises = findViewById(R.id.fab_pick_exercises)
        }
    }

    private fun initRecyclerView() {
        val itemTouchHelper = ItemTouchHelperBuilder(
            swipeDirs = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
            onSwipedCall = { viewHolder, _ -> deleteExercise(viewHolder.adapterPosition) }
        ).build()

        recyclerView.apply {
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
        viewModel.exercises.observe(viewLifecycleOwner) { exercises ->
            adapter.items = exercises.filter { !it.hidden }
        }
    }

    fun addExercise() {
        val action = ExercisesFragmentDirections.addExercise(-1)
        findNavController().navigate(action)
    }

    private fun deleteExercise(position: Int) {
        val exercise = adapter.items[position]
        viewModel.hide(exercise, true)
        Snackbar.make(recyclerView, "Deleted ${exercise.name}", Snackbar.LENGTH_SHORT)
            .setAction("Undo") {
                viewModel.hide(exercise, false)
            }
            .setAnchorView(fabPickExercises)
            .show()
    }

    override fun onExerciseClick(exercise: Exercise) {
        val action = ExercisesFragmentDirections.addExercise(exercise.exerciseId)
        findNavController().navigate(action)
    }
}
