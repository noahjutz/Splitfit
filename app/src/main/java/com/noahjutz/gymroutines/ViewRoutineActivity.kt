package com.noahjutz.gymroutines

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.noahjutz.gymExercises.ViewRoutineExerciseRecyclerAdapter
import com.noahjutz.gymroutines.models.Exercise
import com.noahjutz.gymroutines.models.Routine
import kotlinx.android.synthetic.main.activity_view_routine.*

private const val TAG = "ViewRoutineActivity"

class ViewRoutineActivity : AppCompatActivity(), ViewRoutineExerciseRecyclerAdapter.OnExerciseClickListener {

    private var pos: Int = 0
    private lateinit var exerciseList: ArrayList<Exercise>
    private lateinit var exerciseAdapter: ViewRoutineExerciseRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_routine)

        initRecyclerView()

        val intent = intent
        val routine: Routine? = intent.getParcelableExtra("routine")
        pos = intent.getIntExtra("pos", -1)

        val gson = Gson()
        val type = object : TypeToken<ArrayList<Exercise>>() {}.type
        exerciseList = gson.fromJson(routine?.exercisesJson, type) ?: ArrayList()
        exerciseAdapter.submitList(exerciseList)

        view_title.text = routine?.title ?: "Error"
        view_content.text = routine?.content ?: "Error"
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
        Toast.makeText(this, "$pos TODO", Toast.LENGTH_SHORT).show()
    }
}
