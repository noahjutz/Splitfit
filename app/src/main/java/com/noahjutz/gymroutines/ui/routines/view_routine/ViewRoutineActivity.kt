package com.noahjutz.gymroutines.ui.routines.view_routine

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.models.Exercise
import com.noahjutz.gymroutines.models.ExerciseReference
import com.noahjutz.gymroutines.models.Routine
import com.noahjutz.gymroutines.ui.exercises.view_exercise.ViewExerciseActivity
import com.noahjutz.gymroutines.ui.exercises.view_exercises.SAVED_EXERCISES_PREFS
import com.noahjutz.gymroutines.ui.exercises.view_exercises.SHARED_PREFS_EXERCISES
import com.noahjutz.gymroutines.ui.routines.view_routines.ACTION_DELETE
import com.noahjutz.gymroutines.ui.routines.view_routines.ACTION_EDIT
import com.noahjutz.gymroutines.ui.routines.view_routines.EXTRA_ACTION
import com.noahjutz.gymroutines.ui.routines.view_routines.EXTRA_POS
import kotlinx.android.synthetic.main.activity_view_routine.*

private const val TAG = "ViewRoutineActivity"

const val EXTRA_EXERCISE_TO_VIEW = "com.noahjutz.gymroutines.EXERCISE_TO_VIEW"

class ViewRoutineActivity : AppCompatActivity(),
    ExerciseRecyclerAdapter.OnExerciseClickListener {
    private var pos: Int = 0

    private lateinit var exerciseAdapter: ExerciseRecyclerAdapter
    private lateinit var exerciseRefList: ArrayList<ExerciseReference>
    private lateinit var allExercisesList: ArrayList<Exercise>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_routine)

        initRecyclerView()
        exerciseRefList = ArrayList()
        allExercisesList = ArrayList()
        populateViews()
    }

    private fun populateViews() {
        // TODO: populate recyclerview and these:
        view_title.text = "Error"
        view_content.text = "Error"
    }

    private fun submitList(refList: ArrayList<ExerciseReference>) {
        exerciseAdapter.submitList(refList, allExercisesList)
    }

    private fun initRecyclerView() {
        recycler_view_exercises.apply {
            layoutManager = LinearLayoutManager(this@ViewRoutineActivity)
            exerciseAdapter = ExerciseRecyclerAdapter(this@ViewRoutineActivity)
            adapter = exerciseAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_view_routine, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_routine -> {
                val intent = Intent().apply {
                    putExtra(
                        EXTRA_ACTION,
                        ACTION_DELETE
                    )
                    putExtra(EXTRA_POS, pos)
                }
                setResult(Activity.RESULT_OK, intent)
                finish()
                true
            }
            R.id.edit_routine -> {
                val intent = Intent().apply {
                    putExtra(
                        EXTRA_ACTION,
                        ACTION_EDIT
                    )
                    putExtra(EXTRA_POS, pos)
                }
                setResult(Activity.RESULT_OK, intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onExerciseClick(pos: Int) {
        // TODO: View Exercise
    }
}
