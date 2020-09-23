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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.databinding.FragmentCreateRoutineBinding
import com.noahjutz.gymroutines.ui.routines.create.pick.SharedExerciseViewModel
import com.noahjutz.gymroutines.util.MarginItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateRoutineFragment : Fragment() {

    private val sharedExerciseViewModel: SharedExerciseViewModel by activityViewModels()
    private val viewModel: CreateRoutineViewModel by viewModels()
    private val args: CreateRoutineFragmentArgs by navArgs()
    private val adapter = SetAdapter()

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentCreateRoutineBinding>(
        inflater, R.layout.fragment_create_routine, container, false
    ).apply {
        fragment = this@CreateRoutineFragment
        lifecycleOwner = viewLifecycleOwner
        viewmodel = viewModel
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initActivity()
        initRecyclerView()
        initViewModel()
    }

    private fun initActivity() {
        requireActivity().apply {
            title = if (args.routineId == -1) "Create Routine" else "Edit Routine"
            findViewById<BottomNavigationView>(R.id.bottom_nav).visibility = GONE

            recyclerView = findViewById(R.id.recycler_view)
        }
    }

    private fun initRecyclerView() {

        recyclerView.apply {
            adapter = this@CreateRoutineFragment.adapter
            layoutManager = LinearLayoutManager(this@CreateRoutineFragment.requireContext())
            addItemDecoration(
                MarginItemDecoration(resources.getDimension(R.dimen.any_margin_default).toInt())
            )
            isNestedScrollingEnabled = false
        }
    }

    private fun initViewModel() {
        viewModel.routine.observe(viewLifecycleOwner) { routine ->
            adapter.items = routine.sets
        }

        sharedExerciseViewModel.exercises.observe(viewLifecycleOwner) { exercises ->
            // for (e in exercises) {
            //     viewModel.addExercise(Set(/* e.id? */))
            // }

            if (exercises.isNotEmpty()) sharedExerciseViewModel.clearExercises()
        }
    }

    fun addExercise() {
        val action = CreateRoutineFragmentDirections.addExercise()
        findNavController().navigate(action)
    }
}
