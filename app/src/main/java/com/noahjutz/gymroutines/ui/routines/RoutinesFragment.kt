package com.noahjutz.gymroutines.ui.routines

import android.app.ProgressDialog.show
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Switch
import android.widget.Toast
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
import com.noahjutz.gymroutines.databinding.FragmentRoutinesBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_routines.*
import java.util.*

private const val TAG = "RoutinesFragment"

class RoutinesFragment : Fragment() {

    private val viewModel: RoutinesViewModel by viewModels { viewModelFactory }
    private val viewModelFactory: ViewModelFactory by lazy {
        InjectorUtils.provideViewModelFactory(requireActivity().application)
    }

    private lateinit var binding: FragmentRoutinesBinding
    private lateinit var adapter: RoutinesAdapter

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

        initRecyclerView()
        initViewModel()
        initBinding()

        requireActivity().title = "View Routines"
        requireActivity().bottom_nav.visibility = VISIBLE
    }

    private fun initRecyclerView() {
        adapter = RoutinesAdapter(object : RoutinesAdapter.OnItemClickListener {
            override fun onItemClick(routine: Routine) {
                val action = RoutinesFragmentDirections.addRoutine(routine.routineId)
                findNavController().navigate(action)
            }

            override fun onItemLongClick(routine: Routine) {
                // TODO
            }

        })
        recycler_view.let {
            it.layoutManager = LinearLayoutManager(requireContext())
            it.setHasFixedSize(true)
            it.adapter = adapter
            it.addItemDecoration(
                MarginItemDecoration(
                    resources.getDimension(R.dimen.default_padding).toInt()
                )
            )
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
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
                    val routine = adapter.getRoutineAt(viewHolder.adapterPosition)
                    viewModel.delete(routine)
                    Snackbar.make(recycler_view, "Deleted ${routine.name}", Snackbar.LENGTH_SHORT)
                        .setAction("Undo") {
                            viewModel.insert(routine)
                        }
                        .setAnchorView(fab_add_routine)
                        .show()
                }

            }).attachToRecyclerView(it)
        }
    }

    private fun initViewModel() {
        viewModel.routines.observe(viewLifecycleOwner, Observer { routines ->
            viewModel.updateDebugText()
            adapter.submitList(routines)
        })
    }

    private fun initBinding() {
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.fragment = this
    }

    /**
     * Data Binding click listeners
     */
    fun showDebug(view: View) {
        if ((view as Switch).isChecked) {
            debug_button_insert.visibility = VISIBLE
            debug_button_clear.visibility = VISIBLE
            debug_textview.visibility = VISIBLE
        } else {
            debug_button_insert.visibility = GONE
            debug_button_clear.visibility = GONE
            debug_textview.visibility = GONE
        }
    }

    fun debugInsertRoutine() {
        // Generate random routine and insert it
        val i1 = (0..2).shuffled().first()
        val i2 = (0..2).shuffled().first()
        val names = ArrayList<String>().apply {
            add("Legs")
            add("Push")
            add("Pull")
        }
        val descriptions = ArrayList<String>().apply {
            add("")
            add("Very cool routine")
            add("My new routine")
        }
        viewModel.insert(
            Routine(
                names[i1],
                descriptions[i2]
            )
        )
    }

    fun addRoutine() {
        val action = RoutinesFragmentDirections.addRoutine(-1)
        findNavController().navigate(action)
    }
}
