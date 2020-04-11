package com.noahjutz.gymroutines

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.noahjutz.gymExercises.ExerciseRecyclerAdapter
import com.noahjutz.gymroutines.models.Exercise
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_view_exercises.*

private const val TAG = "ViewExerciseActivity"

private const val SHARED_PREFS = "com.noahjutz.gymroutines.VIEW_EXERCISE_ACTIVITY_PREFS"
private const val SAVED_EXERCISES = "com.noahjutz.gymroutines.SAVED_EXERCISES"

private const val REQUEST_CREATE_EXERCISE = 0
private const val REQUEST_EDIT_EXERCISE = 1

const val EXTRA_EXERCISE = "com.noahjutz.gymroutines.EXERCISE"

class ViewExercisesActivity : AppCompatActivity(), ExerciseRecyclerAdapter.OnExerciseClickListener {

    private lateinit var exerciseAdapter: ExerciseRecyclerAdapter
    private lateinit var exerciseList: ArrayList<Exercise>
    private lateinit var exerciseListToShow: ArrayList<Exercise>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_exercises)

        initRecyclerView()
        loadData()
    }

    override fun onPause() {
        super.onPause()
        saveData()
    }

    private fun saveData() {
        val sharedPrefs = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit().apply {
            putString(SAVED_EXERCISES, Gson().toJson(exerciseList))
        }
        editor.apply()
    }

    private fun loadData() {
        val sharedPrefs = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        val exerciseListJson = sharedPrefs.getString(SAVED_EXERCISES, "[]")
        val gson = Gson()
        val type = object : TypeToken<ArrayList<Exercise>>() {}.type
        exerciseList = gson.fromJson(exerciseListJson, type)
        submitList(exerciseList)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.create_exercise -> {
                intent = Intent(this, CreateExerciseActivity::class.java)
                startActivityForResult(intent, REQUEST_CREATE_EXERCISE)
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
                    val exercise = data?.getParcelableExtra<Exercise>(EXTRA_EXERCISE)
                    if (exercise != null) {
                        exerciseList.add(exercise)
                        submitList(exerciseList)
                    }

                }
            }
            REQUEST_EDIT_EXERCISE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val exercise = data?.getParcelableExtra<Exercise>(EXTRA_EXERCISE)
                    val pos = data?.getIntExtra(EXTRA_POS, -1)
                    if (exercise != null && pos != null) {
                        exerciseList[pos] = exercise
                        submitList(exerciseList)
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
                    putExtra(EXTRA_EXERCISE, exerciseList[item.groupId])
                    putExtra(EXTRA_POS, item.groupId)
                }
                startActivityForResult(intent, REQUEST_EDIT_EXERCISE)
                true
            }
            420 -> { // Delete
                exerciseList[item.groupId].hidden = true
                submitList(exerciseList)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun submitList(exerciseList: java.util.ArrayList<Exercise>) {
        exerciseListToShow = exerciseList
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
            putExtra("EXERCISE", exerciseList[pos])
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
