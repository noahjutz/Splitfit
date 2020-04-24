package com.noahjutz.gymroutines.ui.routines

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.noahjutz.gymroutines.InjectorUtils
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.ViewModelFactory
import com.noahjutz.gymroutines.data.Routine
import kotlinx.android.synthetic.main.activity_create_routine.*

class CreateRoutineActivity : AppCompatActivity() {

    private val viewModel: CreateRoutineViewModel by viewModels { viewModelFactory }
    private lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_routine)

        initViewModel()
        initViews()

        title = "Create Routine"
    }

    private fun initViewModel() {
        viewModelFactory = InjectorUtils.provideViewModelFactory(application)
    }

    private fun initViews() {
        fab_save_routine.setOnClickListener { saveRoutine() }

        // TODO: Populate Views
    }

    private fun saveRoutine() {
        if (edit_name.text.trim().isEmpty()) return
        val routine = Routine(
            edit_name.text.toString().trim(),
            edit_description.text.toString().trim()
        )
        viewModel.insertRoutine(routine)
        finish()
    }
}
