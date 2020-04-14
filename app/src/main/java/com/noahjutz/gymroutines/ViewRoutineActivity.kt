package com.noahjutz.gymroutines

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
import com.noahjutz.gymExercises.ViewRoutineExerciseRecyclerAdapter
import com.noahjutz.gymroutines.models.Exercise
import com.noahjutz.gymroutines.models.ExerciseReference
import com.noahjutz.gymroutines.models.Routine
import kotlinx.android.synthetic.main.activity_view_routine.*

private const val TAG = "ViewRoutineActivity"

const val EXTRA_EXERCISE_TO_VIEW = "com.noahjutz.gymroutines.EXERCISE_TO_VIEW"

class ViewRoutineActivity : AppCompatActivity(),
    ViewRoutineExerciseRecyclerAdapter.OnExerciseClickListener {
    private var pos: Int = 0

    private lateinit var exerciseAdapter: ViewRoutineExerciseRecyclerAdapter
    private lateinit var exerciseRefList: ArrayList<ExerciseReference>
    private lateinit var allExercisesList: ArrayList<Exercise>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_routine)

        initRecyclerView()
        exerciseRefList = ArrayList()

        val intent = intent
        val routine: Routine? = intent.getParcelableExtra("routine")
        pos = intent.getIntExtra("pos", -1)

        // val exerciseIdListJson = routine?.exerciseRefsJson
        // val gson = Gson()
        // val type = object : TypeToken<ArrayList<Int>>() {}.type
        // exerciseIdList = gson.fromJson(exerciseIdListJson, type)
        // submitList(exerciseIdList)

        val exerciseRefListJson = routine?.exerciseRefsJson
        val gson = Gson()
        val type = object : TypeToken<ArrayList<ExerciseReference>>() {}.type
        exerciseRefList = gson.fromJson(exerciseRefListJson, type) ?: ArrayList()
        submitList(exerciseRefList)

        // val gson = Gson()
        // val type = object : TypeToken<ArrayList<Exercise>>() {}.type
        // exerciseList = gson.fromJson(routine?.exercisesJson, type) ?: ArrayList()
        // exerciseAdapter.submitList(exerciseList)
        allExercisesList = ArrayList()
        loadExercisesSharedPrefs()

        view_title.text = routine?.title ?: "Error"
        view_content.text = routine?.content ?: "Error"
    }

    private fun submitList(refList: ArrayList<ExerciseReference>) {
        loadExercisesSharedPrefs()
        exerciseAdapter.submitList(refList, allExercisesList)
    }

    private fun loadExercisesSharedPrefs() {
        val sharedPrefs = getSharedPreferences(SHARED_PREFS_EXERCISES, Context.MODE_PRIVATE)
        val exerciseListJson = sharedPrefs.getString(SAVED_EXERCISES_PREFS, "[]")
        val gson = Gson()
        val type = object : TypeToken<ArrayList<Exercise>>() {}.type
        val exerciseList: ArrayList<Exercise> = gson.fromJson(exerciseListJson, type)
        allExercisesList = exerciseList
    }

    private fun initRecyclerView() {
        recycler_view_exercises.apply {
            layoutManager = LinearLayoutManager(this@ViewRoutineActivity)
            exerciseAdapter = ViewRoutineExerciseRecyclerAdapter(this@ViewRoutineActivity)
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
                    putExtra(EXTRA_ACTION, ACTION_DELETE)
                    putExtra(EXTRA_POS, pos)
                }
                setResult(Activity.RESULT_OK, intent)
                finish()
                true
            }
            R.id.edit_routine -> {
                val intent = Intent().apply {
                    putExtra(EXTRA_ACTION, ACTION_EDIT)
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
        submitList(exerciseRefList)
        val exList = ArrayList<Exercise>()
        for (er: ExerciseReference in exerciseRefList) {
            for (e: Exercise in allExercisesList) {
                if (er.idToRef == e.id) {
                    exList.add(e)
                }
            }
        }
        intent = Intent(this, ViewExerciseActivity::class.java).apply {
            putExtra(
                EXTRA_EXERCISE_TO_VIEW, exList[pos]
            )
        }
        startActivity(intent)
    }
}
