package com.noahjutz.gymroutines.ui.routines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Switch
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.noahjutz.gymroutines.InjectorUtils
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.ViewModelFactory
import com.noahjutz.gymroutines.data.Routine
import com.noahjutz.gymroutines.data.RoutineWithExercises
import com.noahjutz.gymroutines.databinding.FragmentRoutinesBinding
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
    private lateinit var adapter: RweAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_routines, container, false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initActivity()
        initRecyclerView()
        initViewModel()
        initBinding()
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
                val rwe = adapter.getRweAt(viewHolder.adapterPosition)
                viewModel.delete(rwe)
                Snackbar.make(recycler_view, "Deleted ${rwe.routine.name}", Snackbar.LENGTH_SHORT)
                    .setAction("Undo") {
                        viewModel.insert(rwe)
                    }
                    .setAnchorView(fab_add_routine)
                    .show()
            }
        }

        val onItemClickListener = object : RweAdapter.OnItemClickListener {
            override fun onRoutineClick(rwe: RoutineWithExercises) {
                val action = RoutinesFragmentDirections.addRoutine(rwe.routine.routineId)
                findNavController().navigate(action)
            }

            override fun onRoutineLongClick(rwe: RoutineWithExercises) {
                // TODO
            }
        }

        adapter = RweAdapter(onItemClickListener)

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
        viewModel.routinesWithExercises.observe(viewLifecycleOwner, Observer { rwe ->
            viewModel.updateDebugText()
            adapter.submitList(rwe)
        })
    }

    private fun initBinding() {
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.fragment = this
    }

    fun debugShow(view: View) {
        val isVisible = if ((view as Switch).isChecked) VISIBLE else GONE
        debug_button_insert.visibility = isVisible
        debug_button_clear.visibility = isVisible
        debug_textview.visibility = isVisible
    }

    fun debugInsertRoutine() {
        val names = listOf("Push", "Pull", "Legs")
        val descriptions = listOf("", "Very cool routine", "My new routine")

        val routine = Routine(
            names.shuffled().first(),
            descriptions.shuffled().first()
        )

        val rwe = RoutineWithExercises(
            routine,
            listOf()
        )

        viewModel.insert(rwe)
    }

    fun addRoutine() {
        val action = RoutinesFragmentDirections.addRoutine(-1)
        findNavController().navigate(action)
    }
}
