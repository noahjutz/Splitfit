package com.noahjutz.gymroutines

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.noahjutz.gymExercises.ExerciseRecyclerAdapter
import com.noahjutz.gymroutines.datasets.ExerciseDataSource
import com.noahjutz.gymroutines.models.Exercise
import kotlinx.android.synthetic.main.activity_view_exercises.*

class ViewExercisesActivity : AppCompatActivity(), ExerciseRecyclerAdapter.OnExerciseClickListener {

    private lateinit var exerciseAdapter: ExerciseRecyclerAdapter
    private lateinit var exerciseList: ArrayList<Exercise>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_exercises)

        initRecyclerView()
        loadData()
    }

    private fun loadData() {
        exerciseList = ExerciseDataSource.createDataSet()
        exerciseAdapter.submitList(exerciseList)
    }

    private fun initRecyclerView() {
        list_exercises.apply {
            layoutManager = LinearLayoutManager(this@ViewExercisesActivity)
            exerciseAdapter = ExerciseRecyclerAdapter(this@ViewExercisesActivity)
            adapter = exerciseAdapter
        }
    }

    override fun onExerciseClick(pos: Int) {
        val intent = Intent().apply {
            putExtra("EXERCISE", exerciseList[pos])
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
