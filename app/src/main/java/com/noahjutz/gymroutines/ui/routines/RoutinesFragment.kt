package com.noahjutz.gymroutines.ui.routines

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
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
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.data.domain.FullRoutine
import com.noahjutz.gymroutines.databinding.FragmentRoutinesBinding
import com.noahjutz.gymroutines.util.InjectorUtils
import com.noahjutz.gymroutines.util.MarginItemDecoration
import com.noahjutz.gymroutines.util.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_routines.*
import kotlinx.android.synthetic.main.listitem_routine.*
import kotlinx.android.synthetic.main.listitem_routine.view.*

@Suppress("unused")
private const val TAG = "RoutinesFragment"

class RoutinesFragment : Fragment() {

    private val viewModel: RoutinesViewModel by viewModels { viewModelFactory }
    private val viewModelFactory: ViewModelFactory by lazy {
        InjectorUtils.provideViewModelFactory(requireActivity().application)
    }

    private lateinit var binding: FragmentRoutinesBinding
    private lateinit var adapter: RoutineAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_routines, container, false
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
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
            fragment = this@RoutinesFragment
        }
    }

    private fun initActivity() {
        requireActivity().apply {
            title = "View Routines"
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
                val routine = adapter.getRoutine(viewHolder.adapterPosition)
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
        }

        val onItemClickListener = object : RoutineAdapter.OnRoutineClickListener {
            override fun onRoutineClick(card: MaterialCardView) {
                TransitionManager.beginDelayedTransition(recycler_view, AutoTransition())
                val v = if (card.description.visibility == GONE) VISIBLE else GONE
                card.apply {
                    description.visibility = v
                    exercises.visibility = v
                    buttons.visibility = v
                    divider.visibility = v
                }
            }

            override fun onEditClick(fullRoutine: FullRoutine) {
                val action = RoutinesFragmentDirections.addRoutine(fullRoutine.routine.routineId)
                findNavController().navigate(action)
            }

            override fun onLaunchClick(fullRoutine: FullRoutine) {
                Snackbar.make(recycler_view, "Not yet implemented", Snackbar.LENGTH_SHORT)
                    .setAnchorView(fab_pick_exercises)
                    .show()
                // TODO
            }
        }

        adapter = RoutineAdapter(onItemClickListener)

        recycler_view.apply {
            adapter = this@RoutinesFragment.adapter
            layoutManager = LinearLayoutManager(this@RoutinesFragment.requireContext())
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
        viewModel.fullRoutines.observe(viewLifecycleOwner, Observer { fullRoutines ->
            adapter.submitList(fullRoutines)

            TransitionManager.beginDelayedTransition(scene_root, AutoTransition())
            if (fullRoutines.isEmpty()) {
                text_blank.visibility = VISIBLE
                icon_blank.visibility = VISIBLE
            } else {
                text_blank.visibility = GONE
                icon_blank.visibility = GONE
            }
        })
    }

    fun addRoutine() {
        val action = RoutinesFragmentDirections.addRoutine()
        findNavController().navigate(action)
    }
}
