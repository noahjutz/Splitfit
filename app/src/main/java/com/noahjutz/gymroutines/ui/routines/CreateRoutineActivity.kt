package com.noahjutz.gymroutines.ui.routines

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.navArgs
import com.noahjutz.gymroutines.InjectorUtils
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.ViewModelFactory
import com.noahjutz.gymroutines.data.Routine
import com.noahjutz.gymroutines.databinding.ActivityCreateRoutineBinding
import kotlinx.android.synthetic.main.activity_create_routine.*

class CreateRoutineActivity : AppCompatActivity() {

    private val args: CreateRoutineActivityArgs by navArgs()
    private val viewModel: CreateRoutineViewModel by viewModels { viewModelFactory }
    private val viewModelFactory: ViewModelFactory by lazy {
        InjectorUtils.provideViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_routine)

        initBinding()
        initActivity()
    }

    private fun initActivity() {
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
        title = "Create Routine"

        if (args.routineId != -1) {
            title = "Edit Routine"

            val routine = viewModel.getRoutineById(args.routineId)
            edit_name.setText(routine.name)
            edit_description.setText(routine.description)
        }
    }

    private fun initBinding() {
        DataBindingUtil.setContentView<ActivityCreateRoutineBinding>(
            this, R.layout.activity_create_routine
        ).let {
            it.activity = this
        }
    }

    /**
     * Data Binding click listeners
     */
    fun saveRoutine() {
        if (edit_name.text.trim().isEmpty()) return

        if (args.routineId != -1) {
            val routine = viewModel.getRoutineById(args.routineId).apply {
                name = edit_name.text.toString().trim()
                description = edit_description.text.toString().trim()
            }
            viewModel.updateRoutine(routine)
        } else {
            val routine = Routine(
                edit_name.text.toString().trim(),
                edit_description.text.toString().trim()
            )
            viewModel.insertRoutine(routine)
        }
        finish()
    }
}
