package com.noahjutz.gymroutines.ui.exercises

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.noahjutz.gymroutines.InjectorUtils
import com.noahjutz.gymroutines.MainActivity
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.ViewModelFactory
import com.noahjutz.gymroutines.data.Exercise
import com.noahjutz.gymroutines.databinding.ActivityCreateExerciseBinding
import kotlinx.android.synthetic.main.activity_create_exercise.*

private const val TAG = "CreateExerciseActivity"

class CreateExerciseActivity : AppCompatActivity() {

    private val viewModel: CreateExerciseViewModel by viewModels { viewModelFactory }
    private val viewModelFactory: ViewModelFactory by lazy {
        InjectorUtils.provideViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_exercise)

        initBinding()

        title = "Create Exercise"
    }

    private fun initBinding() {
        DataBindingUtil.setContentView<ActivityCreateExerciseBinding>(
            this, R.layout.activity_create_exercise
        ).let {
            it.activity = this
        }
    }

    /**
     * Data Binding click listeners
     */
    fun saveExercise() {
        if (edit_name.text.toString().trim().isEmpty()) return
        val exercise = Exercise(
            edit_name.text.toString().trim(),
            edit_description.text.toString().trim()
        )
        viewModel.insert(exercise)
        finish()
    }
}
