package com.noahjutz.gymroutines

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.noahjutz.gymExercises.CreateRoutineExerciseRecyclerAdapter
import com.noahjutz.gymroutines.models.Exercise
import com.noahjutz.gymroutines.models.Routine
import kotlinx.android.synthetic.main.activity_create_routine.*
import java.lang.Exception

private const val TAG = "CreateRoutineActivity"
private const val REQUEST_EXERCISE = 0

class CreateRoutineActivity : AppCompatActivity(),
    CreateRoutineExerciseRecyclerAdapter.OnExerciseClickListener {

    private lateinit var exerciseAdapter: CreateRoutineExerciseRecyclerAdapter
    private lateinit var exerciseList: ArrayList<Exercise>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_routine)

        initRecyclerView()
        exerciseList = ArrayList()

        val pos = intent.getIntExtra(EXTRA_POS, -1)
        val routine: Routine? = intent.getParcelableExtra(EXTRA_ROUTINE)
        if (routine != null) {
            edit_title.setText(routine?.title ?: "Error")
            edit_content.setText(routine?.content ?: "Error")
            val gson = Gson()
            val type = object : TypeToken<ArrayList<Exercise>>(){}.type
            exerciseList = gson.fromJson(routine?.exercisesJson, type) ?: ArrayList()
            exerciseAdapter.submitList(exerciseList)
        }

        button_add_exercise.setOnClickListener {
            val intent = Intent(this, ViewExercisesActivity::class.java)
            startActivityForResult(intent, REQUEST_EXERCISE)
        }

        fab_save_routine.setOnClickListener {
            val title = edit_title.text.toString()
            val content = edit_content.text.toString()
            val gson = Gson()
            val exercisesJson = gson.toJson(exerciseList)
            if (title != "") {
                val intent = Intent().apply {
                    putExtra(EXTRA_ROUTINE, Routine(title, content, exercisesJson))
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
            exerciseAdapter = CreateRoutineExerciseRecyclerAdapter(this@CreateRoutineActivity)
            adapter = exerciseAdapter
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_EXERCISE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val exercise = data?.getParcelableExtra<Exercise>("EXERCISE")
                    Toast.makeText(this, "Exercise result: ${exercise!!.name}", Toast.LENGTH_SHORT).show()
                    exerciseList.add(exercise)
                    exerciseAdapter.submitList(exerciseList)
                }
            }
        }
    }

    override fun onExerciseClick(pos: Int) {
        try {
            exerciseList.removeAt(pos)
            exerciseAdapter.submitList(exerciseList)
        } catch (e: Exception) {
            Toast.makeText(this, "Error: $e", Toast.LENGTH_SHORT).show()
            Log.d(TAG, exerciseList.toString())
        }
    }
}
