package com.noahjutz.gymroutines.ui.routines

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.noahjutz.gymroutines.InjectorUtils
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.ViewModelFactory
import com.noahjutz.gymroutines.data.Routine
import com.noahjutz.gymroutines.databinding.ActivityCreateRoutineBinding
import kotlinx.android.synthetic.main.activity_create_routine.*

class CreateRoutineActivity : AppCompatActivity() {

    private val viewModel: CreateRoutineViewModel by viewModels { viewModelFactory }
    private val viewModelFactory: ViewModelFactory by lazy {
        InjectorUtils.provideViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_routine)

        initBinding()

        title = "Create Routine"
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
        val routine = Routine(
            edit_name.text.toString().trim(),
            edit_description.text.toString().trim()
        )
        viewModel.insertRoutine(routine)
        finish()
    }
}
