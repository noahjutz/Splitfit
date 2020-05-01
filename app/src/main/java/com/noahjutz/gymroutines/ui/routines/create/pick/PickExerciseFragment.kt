package com.noahjutz.gymroutines.ui.routines.create.pick

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.noahjutz.gymroutines.InjectorUtils
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.data.Exercise
import com.noahjutz.gymroutines.databinding.FragmentPickExerciseBinding
import com.noahjutz.gymroutines.ui.exercises.ExercisesViewModel
import com.noahjutz.gymroutines.ui.routines.create.MarginItemDecoration
import kotlinx.android.synthetic.main.fragment_pick_exercise.*

class PickExerciseFragment : Fragment() {

    private val viewModel: ExercisesViewModel by viewModels { viewModelFactory }
    private val viewModelFactory by lazy { InjectorUtils.provideViewModelFactory(requireActivity().application) }
    private lateinit var adapter: ExercisesAdapter
    private lateinit var binding: FragmentPickExerciseBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_pick_exercise, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
        initActivity()
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.exercises.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    private fun initActivity() {
        requireActivity().apply {
            title = "Pick Exercise"
        }
    }

    private fun initRecyclerView() {
        val onItemClickListener = object : ExercisesAdapter.OnItemClickListener {
            override fun onExerciseClick(exercise: Exercise) {
                // TODO
            }

            override fun onExerciseLongClick(exercise: Exercise) {}
        }

        adapter = ExercisesAdapter(onItemClickListener)

        recycler_view.apply {
            adapter = this@PickExerciseFragment.adapter
            layoutManager = LinearLayoutManager(this@PickExerciseFragment.requireContext())
            setHasFixedSize(true)
            addItemDecoration(
                MarginItemDecoration(
                    resources.getDimension(R.dimen.any_margin_default).toInt()
                )
            )
        }
    }
}
