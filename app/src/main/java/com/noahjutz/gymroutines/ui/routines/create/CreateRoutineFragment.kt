package com.noahjutz.gymroutines.ui.routines.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.noahjutz.gymroutines.InjectorUtils
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.ViewModelFactory
import com.noahjutz.gymroutines.data.Exercise
import com.noahjutz.gymroutines.databinding.FragmentCreateRoutineBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_create_routine.*

@Suppress("unused")
private const val TAG = "CreateRoutineFragment"

class CreateRoutineFragment : Fragment() {

    private val viewModel: CreateRoutineViewModel by viewModels { viewModelFactory }
    private val viewModelFactory: ViewModelFactory by lazy {
        InjectorUtils.provideViewModelFactory(requireActivity().application)
    }
    private val args: CreateRoutineFragmentArgs by navArgs()

    private lateinit var binding: FragmentCreateRoutineBinding
    private lateinit var adapter: ExercisesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_create_routine,
            container,
            false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initActivity()
        initRecyclerView()
        initBinding()
        initViewModel()
        initViews()
    }

    private fun initActivity() {
        requireActivity().apply {
            (this as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
            title = if (args.routineId == -1) "Create Routine" else "Edit Routine"
            bottom_nav.visibility = GONE
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
                val exercise = adapter.getExerciseAt(viewHolder.adapterPosition)
                viewModel.removeExercise(exercise)
                adapter.notifyDataSetChanged()
                Snackbar.make(recycler_view, "Deleted ${exercise.name}", Snackbar.LENGTH_SHORT)
                    .setAction("Undo") {
                        viewModel.addExercise(exercise)
                        adapter.notifyDataSetChanged()
                    }
                    .setAnchorView(fab_save)
                    .show()
            }
        }

        val onItemClickListener = object : ExercisesAdapter.OnItemClickListener {
            override fun onExerciseClick(exercise: Exercise) {
                // TODO
            }

            override fun onExerciseLongClick(exercise: Exercise) {
                // TODO
            }
        }

        adapter = ExercisesAdapter(onItemClickListener)

        recycler_view.apply {
            adapter = this@CreateRoutineFragment.adapter
            layoutManager = LinearLayoutManager(this@CreateRoutineFragment.requireContext())
            addItemDecoration(
                MarginItemDecoration(
                    resources.getDimension(R.dimen.any_margin_default).toInt()
                )
            )
            ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(this)
            isNestedScrollingEnabled = false
        }
    }

    private fun initBinding() {
        binding.fragment = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel
    }

    private fun initViewModel() {
        viewModel.init(args.routineId, args.exercisesString)
        viewModel.routineWithExercises.observe(viewLifecycleOwner, Observer { rwe ->
            adapter.submitList(rwe.exercises)
            adapter.notifyDataSetChanged()
        })
    }

    private fun initViews() {
        edit_name.editText?.doOnTextChanged { text, _, _, _ ->
            if (text?.trim().toString().isNotEmpty()) {
                edit_name.error = null
            }
        }
    }

    fun debugShow(view: View) {
        val isVisible = if ((view as Switch).isChecked) VISIBLE else GONE
        debug_button_insert.visibility = isVisible
        debug_button_clear.visibility = isVisible
        debug_textview.visibility = isVisible
    }

    fun saveRoutine() {
        if (viewModel.save()) {
            val action =
                CreateRoutineFragmentDirections.saveRoutine()
            findNavController().navigate(action)
        } else if (viewModel.routineWithExercises.value?.routine?.name.toString().isEmpty())
            edit_name.error = "Please enter a name"
    }

    fun addExercise() {
        val action = CreateRoutineFragmentDirections.addExercise(args.routineId)
        findNavController().navigate(action)
    }
}
