package com.noahjutz.gymroutines.ui.exercises

import android.os.Bundle
import com.noahjutz.gymroutines.databinding.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.noahjutz.gymroutines.R
import kotlinx.android.synthetic.main.fragment_exercises.*

private const val TAG = "ExercisesActivity"

class ExercisesFragment : Fragment(), ExerciseAdapter.OnExerciseClickListener {

    private lateinit var exerciseAdapter: ExerciseAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentExercisesBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_exercises, container, false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
        populateViews()

        fab_add_exercise.setOnClickListener { createExercise() }
    }

    private fun populateViews() {
        Log.d(TAG, "populateViews(): called")
        // TODO
    }

    private fun createExercise() {
        findNavController().navigate(R.id.add_exercise)
    }

    private fun viewExercise(pos: Int) {
        Log.d(TAG, "viewExercise(): called at $pos")
        // TODO: Launch [ViewExerciseActivity]
    }

    private fun initRecyclerView() {
        // TODO
    }

    override fun onExerciseClick(pos: Int) {
        viewExercise(pos)
    }
}
