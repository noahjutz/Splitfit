package com.noahjutz.gymroutines.ui.routines

import android.os.Bundle
import android.util.Log
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.noahjutz.gymroutines.ViewModelFactory
import com.noahjutz.gymroutines.InjectorUtils
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.data.Routine
import com.noahjutz.gymroutines.databinding.FragmentRoutinesBinding
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
    }

    private fun initRecyclerView() {
        adapter = RoutinesAdapter()
        recycler_view.let {
            it.layoutManager = LinearLayoutManager(requireContext())
            it.setHasFixedSize(true)
            it.adapter = adapter
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
        viewModel.insertRoutine(Routine(
            names[i1],
            descriptions[i2]
        ))
    }

    fun addRoutine() {
        findNavController().navigate(R.id.add_routine)
    }
}
