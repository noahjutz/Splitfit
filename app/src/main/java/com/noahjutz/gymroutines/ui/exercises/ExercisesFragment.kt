package com.noahjutz.gymroutines.ui.exercises

import android.os.Bundle
import com.noahjutz.gymroutines.databinding.*
import android.view.LayoutInflater
import android.view.View
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
import com.noahjutz.gymroutines.ViewModelFactory
import com.noahjutz.gymroutines.InjectorUtils
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.data.Exercise
import kotlinx.android.synthetic.main.fragment_exercises.*
import kotlinx.android.synthetic.main.fragment_exercises.debug_button_clear
import kotlinx.android.synthetic.main.fragment_exercises.debug_button_insert
import kotlinx.android.synthetic.main.fragment_exercises.debug_textview
import java.util.ArrayList

private const val TAG = "ExercisesActivity"

class ExercisesFragment : Fragment() {

    private val viewModel: ExercisesViewModel by viewModels { viewModelFactory }
    private val viewModelFactory: ViewModelFactory by lazy {
        InjectorUtils.provideViewModelFactory(requireActivity().application)
    }

    private lateinit var adapter: ExercisesAdapter
    private lateinit var binding: FragmentExercisesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_exercises, container, false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initRecyclerView()
        initViewModel()
        initBinding()

        requireActivity().title = "View Exercises"
    }

    private fun initRecyclerView() {
        adapter = ExercisesAdapter(object : ExercisesAdapter.OnItemClickListener {
            override fun onItemClick(exercise: Exercise) {
                val action = ExercisesFragmentDirections.addExercise(exercise.exerciseId)
                findNavController().navigate(action)
            }

            override fun onItemLongClick(exercise: Exercise) {
                // TODO
            }

        })

        recycler_view.let {
            it.layoutManager = LinearLayoutManager(requireContext())
            it.setHasFixedSize(true)
            it.adapter = adapter
            it.addItemDecoration(
                MarginItemDecoration(
                    resources.getDimension(R.dimen.any_margin_default).toInt()
                )
            )
            ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(
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
                    viewModel.delete(exercise)
                    Snackbar.make(recycler_view, "Deleted ${exercise.name}", Snackbar.LENGTH_SHORT)
                        .setAction("Undo") {
                            viewModel.insert(exercise)
                        }
                        .setAnchorView(fab_add_exercise)
                        .show()
                }

            }).attachToRecyclerView(it)
        }
    }

    private fun initViewModel() {
        viewModel.exercises.observe(viewLifecycleOwner, Observer { exercises ->
            viewModel.updateDebugText()
            adapter.submitList(exercises)
        })
    }

    private fun initBinding() {
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.fragment = this
    }

    /**
     * Data binding click listeners
     */
    fun showDebug(view: View) {
        if ((view as Switch).isChecked) {
            debug_button_insert.visibility = View.VISIBLE
            debug_button_clear.visibility = View.VISIBLE
            debug_textview.visibility = View.VISIBLE
        } else {
            debug_button_insert.visibility = View.GONE
            debug_button_clear.visibility = View.GONE
            debug_textview.visibility = View.GONE
        }
    }

    fun debugInsertExercise() {
        // Generate random exercise and insert it
        val i1 = (0..2).shuffled().first()
        val i2 = (0..2).shuffled().first()
        val names = ArrayList<String>().apply {
            add("Hammer curls")
            add("Sit ups")
            add("Squats")
        }
        val descriptions = ArrayList<String>().apply {
            add("")
            add("Really exhausting")
            add("Compound movement")
        }
        viewModel.insert(
            Exercise(
                names[i1],
                descriptions[i2]
            )
        )
    }

    fun addExercise() {
        val action = ExercisesFragmentDirections.addExercise(-1)
        findNavController().navigate(action)
    }
}
