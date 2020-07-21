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
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.google.android.material.snackbar.Snackbar
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.data.domain.FullRoutine
import com.noahjutz.gymroutines.databinding.FragmentRoutinesBinding
import com.noahjutz.gymroutines.util.ItemTouchHelperBuilder
import com.noahjutz.gymroutines.util.MarginItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_routines.*

@AndroidEntryPoint
class RoutinesFragment : Fragment(), RoutineAdapter.RoutineListener {

    private val viewModel: RoutinesViewModel by viewModels()

    // TODO: Field injection
    private lateinit var adapter: RoutineAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentRoutinesBinding>(
            inflater, R.layout.fragment_routines, container, false
        ).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
            fragment = this@RoutinesFragment
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
            title = "Routines"
            bottom_nav.visibility = VISIBLE
        }
    }

    private fun initRecyclerView() {
        val itemTouchHelper = ItemTouchHelperBuilder(
            swipeDirs = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
            onSwipedCall = { viewHolder, _ -> deleteRoutine(viewHolder.adapterPosition) }
        ).build()

        adapter = RoutineAdapter(this)

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
        viewModel.fullRoutines.observe(
            viewLifecycleOwner,
            Observer { fullRoutines ->
                adapter.submitList(fullRoutines)

                val v = if (fullRoutines.isEmpty()) VISIBLE else GONE
                showEmptyScreen(v)
            }
        )
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
        val routine = adapter.getRoutine(position)
        viewModel.delete(routine)
        Snackbar.make(
            recycler_view,
            "Deleted ${routine.routine.name}",
            Snackbar.LENGTH_SHORT
        )
            .setAction("Undo") { viewModel.insert(routine) }
            .setAnchorView(fab_pick_exercises)
            .show()
    }

    override fun onEditClick(fullRoutine: FullRoutine) {
        val action = RoutinesFragmentDirections.addRoutine(fullRoutine.routine.routineId)
        findNavController().navigate(action)
    }
}
