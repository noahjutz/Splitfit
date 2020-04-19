package com.noahjutz.gymroutines.ui.routines.create_routine

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.noahjutz.gymroutines.*
import com.noahjutz.gymroutines.models.*
import com.noahjutz.gymroutines.models.Set
import com.noahjutz.gymroutines.ui.exercises.view_exercises.EXTRA_EXERCISE_ID
import com.noahjutz.gymroutines.ui.exercises.view_exercises.SAVED_EXERCISES_PREFS
import com.noahjutz.gymroutines.ui.exercises.view_exercises.SHARED_PREFS_EXERCISES
import com.noahjutz.gymroutines.ui.exercises.view_exercises.ViewExercisesActivity
import com.noahjutz.gymroutines.ui.routines.view_routines.EXTRA_POS
import com.noahjutz.gymroutines.ui.routines.view_routines.EXTRA_ROUTINE
import kotlinx.android.synthetic.main.activity_create_routine.*
import java.util.*
import kotlin.collections.ArrayList

private const val TAG = "CreateRoutineActivity"

private const val REQUEST_EXERCISE = 0

class CreateRoutineActivity : AppCompatActivity(),
    CreateRoutineExerciseRecyclerAdapter.OnExerciseClickListener {

    private lateinit var exerciseAdapter: CreateRoutineExerciseRecyclerAdapter
    private lateinit var allExercisesList: ArrayList<Exercise>
    private lateinit var exerciseRefList: ArrayList<ExerciseReference>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_routine)

        initRecyclerView()
        exerciseRefList = ArrayList()
        loadExercisesSharedPrefs() // init allExercisesList

        val pos = intent.getIntExtra(EXTRA_POS, -1)
        val routine: Routine? = intent.getParcelableExtra(EXTRA_ROUTINE)
        if (routine != null) {
            edit_title.setText(routine.title)
            edit_content.setText(routine.content)
            val gson = Gson()
            val type = object : TypeToken<ArrayList<ExerciseReference>>() {}.type
            exerciseRefList = gson.fromJson(routine.exerciseRefsJson, type) ?: ArrayList()
            submitList(exerciseRefList)
        }

        button_add_exercise.setOnClickListener {
            val intent = Intent(this, ViewExercisesActivity::class.java)
            startActivityForResult(intent,
                REQUEST_EXERCISE
            )
        }

        fab_save_routine.setOnClickListener {
            val title = edit_title.text.toString()
            val content = edit_content.text.toString()
            val gson = Gson()
            // val exercisesJson = gson.toJson(exerciseIdList)
            val exerciseRefsJson = gson.toJson(exerciseRefList)
            if (title != "") {
                val intent = Intent().apply {
                    putExtra(EXTRA_ROUTINE, Routine(title, content, exerciseRefsJson))
                    putExtra(EXTRA_POS, pos)
                }
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }

    private fun initRecyclerView() {
        list_added_exercises.apply {
            layoutManager = LinearLayoutManager(this@CreateRoutineActivity)
            exerciseAdapter =
                CreateRoutineExerciseRecyclerAdapter(
                    this@CreateRoutineActivity
                )
            adapter = exerciseAdapter
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_EXERCISE -> { // Add exercise to routine
                if (resultCode == Activity.RESULT_OK) {
                    val id = data?.getIntExtra(EXTRA_EXERCISE_ID, -1)
                    if (id != null) {
                        exerciseRefList.add(ExerciseReference("[]", id))
                    }
                }
                submitList(exerciseRefList)
            }
        }
    }

    private fun loadExercisesSharedPrefs() {
        val sharedPrefs = getSharedPreferences(SHARED_PREFS_EXERCISES, Context.MODE_PRIVATE)
        val exerciseListJson = sharedPrefs.getString(SAVED_EXERCISES_PREFS, "[]")
        val gson = Gson()
        val type = object : TypeToken<ArrayList<Exercise>>() {}.type
        val exerciseList: ArrayList<Exercise> = gson.fromJson(exerciseListJson, type)
        allExercisesList = exerciseList
    }

    private fun submitList(refList: ArrayList<ExerciseReference>) {
        exerciseAdapter.submitList(refList, allExercisesList)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            2420 -> { // Remove
                try {
                    exerciseRefList.removeAt(item.groupId)
                    submitList(exerciseRefList)
                } catch (e: ArrayIndexOutOfBoundsException) {
                    Log.d(TAG, "Error: $e\nList: $exerciseRefList")
                }
                true
            }
            2421 -> { // Edit
                false
            }
            2422 -> { // Add Set
                // val setsJson = exerciseRefList[item.groupId].setsJson
                // val type = object: TypeToken<ArrayList<com.noahjutz.gymroutines.models.Set>>() {}.type
                // val gson = Gson()
                // val setsList: ArrayList<Set> = gson.fromJson(setsJson, type)
//
                // val fields: ArrayList<Field> = ArrayList()
                // fields.add(Field("Weight", UnitDouble("kg", 0.0)))
                // fields.add(Field("Reps", UnitDouble("rep", 0.0)))
                // val fieldsJson = gson.toJson(fields)
//
                // setsList.add(Set(fieldsJson))
                // val newSetsJson = gson.toJson(setsList)
                // exerciseRefList[item.groupId].setsJson = newSetsJson
                // Log.d(TAG, "Exercise: ${exerciseRefList[item.groupId]}")
                // submitList(exerciseRefList)
                false
            }
            2423 -> { // Move Up
                Collections.swap(exerciseRefList, item.groupId, item.groupId - 1)
                submitList(exerciseRefList)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    override fun onExerciseClick(pos: Int) {
    }
}
