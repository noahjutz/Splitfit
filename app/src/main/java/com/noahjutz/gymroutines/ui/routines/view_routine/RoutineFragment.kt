package com.noahjutz.gymroutines.ui.routines.view_routine

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.databinding.FragmentRoutineBinding
import kotlinx.android.synthetic.main.fragment_routine.*

private const val TAG = "RoutineFragment"

class RoutineFragment : Fragment(),
    ExerciseAdapter.OnExerciseClickListener {

    private lateinit var exerciseAdapter: ExerciseAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentRoutineBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_routine, container, false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initRecyclerView()
        populateViews()

        activity?.title = "View Routine"
    }

    private fun viewExercise(pos: Int) {
        Log.d(TAG, "viewExercise(): called at $pos")
        // TODO: Launch [ViewExerciseActivity]
    }

    private fun editRoutine() {
        Log.d(TAG, "editRoutine(): called")
        // TODO
    }

    private fun deleteRoutine() {
        Log.d(TAG, "deleteRoutine: called")
        // TODO
    }

    private fun populateViews() {
        Log.d(TAG, "populateViews: called")
        // TODO
    }

    private fun initRecyclerView() {
        recycler_view_exercises.apply {
            layoutManager = LinearLayoutManager(this@RoutineFragment.context)
            exerciseAdapter = ExerciseAdapter(this@RoutineFragment)
            adapter = exerciseAdapter
        }
    }

    // override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    //     val inflater: MenuInflater = menuInflater
    //     inflater.inflate(R.menu.menu_view_routine, menu)
    //     return true
    // }

    // override fun onOptionsItemSelected(item: MenuItem): Boolean {
    //     return when (item.itemId) {
    //         R.id.delete_routine -> {
    //             deleteRoutine()
    //             true
    //         }
    //         R.id.edit_routine -> {
    //             editRoutine()
    //             true
    //         }
    //         else -> super.onOptionsItemSelected(item)
    //     }
    // }

    override fun onExerciseClick(pos: Int) {
        viewExercise(pos)
    }
}
