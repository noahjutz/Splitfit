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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.data.domain.ExerciseImpl
import com.noahjutz.gymroutines.data.domain.Set
import com.noahjutz.gymroutines.databinding.FragmentCreateRoutineBinding
import com.noahjutz.gymroutines.ui.routines.create.pick.SharedExerciseViewModel
import com.noahjutz.gymroutines.util.ExerciseImplBuilder
import com.noahjutz.gymroutines.util.ItemTouchHelperBuilder
import com.noahjutz.gymroutines.util.MarginItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_create_routine.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main

@AndroidEntryPoint
class CreateRoutineFragment : Fragment() {

    private val sharedExerciseViewModel: SharedExerciseViewModel by activityViewModels()
    private val viewModel: CreateRoutineViewModel by viewModels()
    private val args: CreateRoutineFragmentArgs by navArgs()
    private val adapter = ExerciseAdapter()

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
            bottom_nav.visibility = GONE
        }
    }

    private fun initRecyclerView() {
        val itemTouchHelper = ItemTouchHelperBuilder(
            dragDirs = ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            onMoveCall = { _, viewHolder, target ->
                swapExercises(viewHolder.adapterPosition, target.adapterPosition)
            }
        ).build()

        recycler_view.apply {
            adapter = this@CreateRoutineFragment.adapter
            layoutManager = LinearLayoutManager(this@CreateRoutineFragment.requireContext())
            addItemDecoration(
                MarginItemDecoration(resources.getDimension(R.dimen.any_margin_default).toInt())
            )
            isNestedScrollingEnabled = false
            itemTouchHelper.attachToRecyclerView(this)
        }
    }

    private fun initViewModel() {
        viewModel.fullRoutine.observe(viewLifecycleOwner) { fullRoutine ->
            adapter.submitList(fullRoutine.exercises)

            for (e in fullRoutine.exercises) {
                CoroutineScope(Dispatchers.Default).launch {
                    // TODO: Don't wait a hardcoded amount of time, instead wait for viewHolders
                    //  to be bound
                    delay(20)
                    withContext(Main) {
                        TransitionManager.beginDelayedTransition(
                            create_routine_root,
                            AutoTransition()
                        )
                        adapter.mAdapters[e.setGroup.setGroupId]?.submitList(e.sets)
                    }
                }
            }
        }

        sharedExerciseViewModel.exercises.observe(viewLifecycleOwner) { exercises ->
            for (e in exercises) {
                val exerciseImpl = ExerciseImplBuilder(
                    exercise = e,
                    routine = viewModel.fullRoutine.value!!.routine
                ).build()
                viewModel.addExercise(exerciseImpl)
            }

            if (exercises.isNotEmpty()) sharedExerciseViewModel.clearExercises()
        }
    }

    fun addExercise() {
        val action = CreateRoutineFragmentDirections.addExercise()
        findNavController().navigate(action)
    }

    private fun swapExercises(fromPos: Int, toPos: Int): Boolean {
        adapter.notifyItemMoved(fromPos, toPos)
        viewModel.swapExercises(fromPos, toPos)
        return true
    }
}
