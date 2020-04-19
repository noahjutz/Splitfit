package com.noahjutz.gymroutines.ui.exercises.view_exercises

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.ui.exercises.create_exercise.CreateExerciseActivity
import com.noahjutz.gymroutines.models.Exercise
import com.noahjutz.gymroutines.ui.routines.view_routine.EXTRA_EXERCISE_TO_VIEW
import com.noahjutz.gymroutines.ui.routines.view_routines.EXTRA_POS
import kotlinx.android.synthetic.main.activity_view_exercises.*

private const val TAG = "ViewExerciseActivity"

const val SHARED_PREFS_EXERCISES = "com.noahjutz.gymroutines.VIEW_EXERCISE_ACTIVITY_PREFS"
const val SAVED_EXERCISES_PREFS = "com.noahjutz.gymroutines.SAVED_EXERCISES"

private const val REQUEST_CREATE_EXERCISE = 0
private const val REQUEST_EDIT_EXERCISE = 1

const val EXTRA_EXERCISE = "com.noahjutz.gymroutines.EXERCISE"
const val EXTRA_EXERCISE_ID = "com.noahjutz.gymroutines.EXERCISE_ID"
const val EXTRA_EXERCISE_NAME = "com.noahjutz.gymroutines.EXERCISE_NAME"

class ViewExercisesActivity : AppCompatActivity(), ExerciseRecyclerAdapter.OnExerciseClickListener {

    private lateinit var exerciseAdapter: ExerciseRecyclerAdapter
    private lateinit var exerciseList: ArrayList<Exercise>
    private lateinit var exerciseListToShow: ArrayList<Exercise>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_exercises)

        initRecyclerView()
        loadData()
        Log.d(TAG, "List: $exerciseList")
        Log.d(TAG, "Show: $exerciseListToShow")
    }

    override fun onPause() {
        super.onPause()
        saveData()
    }

    private fun saveData() {
        val sharedPrefs = getSharedPreferences(SHARED_PREFS_EXERCISES, Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit().apply {
            putString(SAVED_EXERCISES_PREFS, Gson().toJson(exerciseList))
        }
        editor.apply()
    }

    private fun loadData() {
        val sharedPrefs = getSharedPreferences(SHARED_PREFS_EXERCISES, Context.MODE_PRIVATE)
        val exerciseListJson = sharedPrefs.getString(SAVED_EXERCISES_PREFS, "[]")
        val gson = Gson()
        val type = object : TypeToken<ArrayList<Exercise>>() {}.type
        exerciseList = gson.fromJson(exerciseListJson, type)
        submitList()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.create_exercise -> {
                intent = Intent(this, CreateExerciseActivity::class.java)
                startActivityForResult(
                    intent,
                    REQUEST_CREATE_EXERCISE
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_exercises, menu)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CREATE_EXERCISE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val exerciseTitle = data?.getStringExtra(EXTRA_EXERCISE_NAME)
                    val exerciseId = data?.getIntExtra(EXTRA_EXERCISE_ID, -1)
                    val exercise = Exercise(exerciseId ?: -1, exerciseTitle ?: "")
                    if (exercise.id != -1) {
                        exerciseList.add(exercise)
                        submitList()
                    }
                }
            }
            REQUEST_EDIT_EXERCISE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val pos = data?.getIntExtra(EXTRA_POS, -1)
                    val exerciseTitle = data?.getStringExtra(EXTRA_EXERCISE_NAME)
                    val exerciseId = data?.getIntExtra(EXTRA_EXERCISE_ID, -1)
                    val exercise = Exercise(exerciseId ?: -1, exerciseTitle ?: "")
                    if (exercise.id != -1 && pos != null) {
                        exerciseList[pos] = exercise
                        submitList()
                    }
                }
            }
        }
    }

    private fun initRecyclerView() {
        list_exercises.apply {
            layoutManager = LinearLayoutManager(this@ViewExercisesActivity)
            exerciseAdapter = ExerciseRecyclerAdapter(this@ViewExercisesActivity)
            adapter = exerciseAdapter
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as? AdapterView.AdapterContextMenuInfo
        return when (item.itemId) {
            421 -> { // Edit
                intent = Intent(this, CreateExerciseActivity::class.java).apply {
                    putExtra(EXTRA_EXERCISE_NAME, exerciseListToShow[item.groupId].name)
                    putExtra(EXTRA_EXERCISE_ID, exerciseListToShow[item.groupId].id)
                    putExtra(EXTRA_POS, item.groupId)
                }
                startActivityForResult(
                    intent,
                    REQUEST_EDIT_EXERCISE
                )
                true
            }
            420 -> { // Delete
                exerciseListToShow[item.groupId].hidden = true
                submitList()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun submitList() {
        exerciseListToShow = ArrayList(exerciseList)
        val iterator = exerciseListToShow.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            if (item.hidden) {
                iterator.remove()
            }
        }
        exerciseAdapter.submitList(exerciseListToShow)
    }

    override fun onExerciseClick(pos: Int) {
        val intent = Intent().apply {
            putExtra(EXTRA_EXERCISE_ID, exerciseListToShow[pos].id)
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
