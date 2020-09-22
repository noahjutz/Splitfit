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

package com.noahjutz.gymroutines.ui.routines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.google.android.material.snackbar.Snackbar
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.data.domain.Routine
import com.noahjutz.gymroutines.databinding.FragmentRoutinesBinding
import com.noahjutz.gymroutines.util.ItemTouchHelperBuilder
import com.noahjutz.gymroutines.util.MarginItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_routines.*

@AndroidEntryPoint
class RoutinesFragment : Fragment(), RoutineAdapter.RoutineListener {

    private val viewModel: RoutinesViewModel by viewModels()
    private val adapter = RoutineAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentRoutinesBinding>(
        inflater, R.layout.fragment_routines, container, false
    ).apply {
        viewmodel = viewModel
        lifecycleOwner = viewLifecycleOwner
        fragment = this@RoutinesFragment
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initActivity()
        initRecyclerView()
        initViewModel()
    }

    private fun initActivity() {
        requireActivity().apply {
            title = "Routines"
            bottom_nav?.visibility = VISIBLE
        }
    }

    private fun initRecyclerView() {
        val itemTouchHelper = ItemTouchHelperBuilder(
            swipeDirs = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
            onSwipedCall = { viewHolder, _ -> deleteRoutine(viewHolder.adapterPosition) }
        ).build()

        recycler_view.apply {
            adapter = this@RoutinesFragment.adapter
            layoutManager = LinearLayoutManager(this@RoutinesFragment.requireContext())
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
        viewModel.routines.observe(viewLifecycleOwner) { routines ->
            adapter.items = routines

            val v = if (routines.isEmpty()) VISIBLE else GONE
            showEmptyScreen(v)
        }
    }

    private fun showEmptyScreen(visibility: Int) {
        TransitionManager.beginDelayedTransition(routines_root, AutoTransition())
        routines_empty.visibility = visibility
    }

    fun addRoutine() {
        val action = RoutinesFragmentDirections.addRoutine()
        findNavController().navigate(action)
    }

    private fun deleteRoutine(position: Int) {
        val routine = adapter.items[position]
        viewModel.deleteRoutine(routine.routineId)
        Snackbar.make(
            recycler_view,
            "Deleted ${routine.name}",
            Snackbar.LENGTH_SHORT
        )
    }

    override fun onRoutineClick(routine: Routine) {
        val action = RoutinesFragmentDirections.addRoutine(routine.routineId)
        findNavController().navigate(action)
    }
}
