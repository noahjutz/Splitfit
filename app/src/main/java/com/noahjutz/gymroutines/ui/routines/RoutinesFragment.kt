package com.noahjutz.gymroutines.ui.routines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
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
import com.google.android.material.snackbar.Snackbar
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.data.domain.RwE
import com.noahjutz.gymroutines.databinding.FragmentRoutinesBinding
import com.noahjutz.gymroutines.util.InjectorUtils
import com.noahjutz.gymroutines.util.MarginItemDecoration
import com.noahjutz.gymroutines.util.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_routines.*

@Suppress("unused")
private const val TAG = "RoutinesFragment"

class RoutinesFragment : Fragment() {

    private val viewModel: RoutinesViewModel by viewModels { viewModelFactory }
    private val viewModelFactory: ViewModelFactory by lazy {
        InjectorUtils.provideViewModelFactory(requireActivity().application)
    }

    private lateinit var binding: FragmentRoutinesBinding
    private lateinit var adapterLegacy: RweAdapter
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
        val itemTouchHelperCallbackLegacy = object : ItemTouchHelper.SimpleCallback(
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
                val rwe = adapterLegacy.getRweAt(viewHolder.adapterPosition)
                viewModel.delete(rwe)
                Snackbar.make(recycler_view, "Deleted ${rwe.routine.name}", Snackbar.LENGTH_SHORT)
                    .setAction("Undo") { viewModel.insert(rwe) }
                    .setAnchorView(fab_pick_exercises)
                    .show()
            }
        }
        // TODO: Replace ^ with v
        // val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
        //     0,
        //     ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        // ) {
        //     override fun onMove(
        //         recyclerView: RecyclerView,
        //         viewHolder: RecyclerView.ViewHolder,
        //         target: RecyclerView.ViewHolder
        //     ): Boolean {
        //         return false
        //     }

        //     override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        //         val routine = adapter.getRoutine(viewHolder.adapterPosition)
        //         viewModel.delete(routine)
        //         Snackbar.make(recycler_view, "Deleted ${routine.routine.name}", Snackbar.LENGTH_SHORT)
        //             .setAction("Undo") { viewModel.insert(routine) }
        //             .setAnchorView(fab_pick_exercises)
        //             .show()
        //     }
        // }

        val onItemClickListenerLegacy = object : RweAdapter.OnRoutineClickListener {
            override fun onRoutineClick(rwe: RwE) {
                val action = RoutinesFragmentDirections.addRoutine(rwe.routine.routineId)
                findNavController().navigate(action)
            }

            override fun onRoutineLongClick(rwe: RwE) {}
        }
        // TODO: Replace ^ with v
        // val onItemClickListener = object : RoutineAdapter.OnRoutineClickListener {
        //     override fun onRoutineClick(fullRoutine: FullRoutine) {
        //         val action = RoutinesFragmentDirections.addRoutine(fullRoutine.routine.routineId)
        //         findNavController().navigate(action)
        //     }

        //      override fun onRoutineLongClick(fullRoutine: FullRoutine) {}
        //  }

        adapterLegacy = RweAdapter(onItemClickListenerLegacy)
        // TODO: Replace ^ with v
        //  adapter = RoutineAdapter(onItemClickListener)

        recycler_view.apply {
            adapter = this@RoutinesFragment.adapterLegacy
            // TODO: ^ with v
            // adapter = this@RoutinesFragment.adapter
            layoutManager = LinearLayoutManager(this@RoutinesFragment.requireContext())
            setHasFixedSize(true)
            addItemDecoration(
                MarginItemDecoration(
                    resources.getDimension(R.dimen.any_margin_default).toInt()
                )
            )
            ItemTouchHelper(itemTouchHelperCallbackLegacy).attachToRecyclerView(this)
            // TODO: Replace ^ with v
            // ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(this)
        }
    }

    private fun initViewModel() {
        viewModel.routinesWithExercises.observe(viewLifecycleOwner, Observer { rwe ->
            adapterLegacy.submitList(rwe)
        })
        // TODO: Replace ^ with v
        // viewModel.fullRoutines.observe(viewLifecycleOwner, Observer { fullRoutines ->
        //     adapter.submitList(fullRoutines)
        // })
    }

    fun addRoutine() {
        val action = RoutinesFragmentDirections.addRoutine()
        findNavController().navigate(action)
    }
}
