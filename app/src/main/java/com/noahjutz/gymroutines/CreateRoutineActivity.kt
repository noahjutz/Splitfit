package com.noahjutz.gymroutines

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.noahjutz.gymroutines.models.Routine
import kotlinx.android.synthetic.main.activity_create_routine.*
import kotlinx.android.synthetic.main.activity_view_routine.*

class CreateRoutineActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_routine)

        val pos = intent.getIntExtra(EXTRA_POS, -1)
        val routine = intent.getParcelableExtra<Routine>(EXTRA_ROUTINE)
        if (routine != null) {
            edit_title.setText(routine.title)
            edit_content.setText(routine.content)
        }

        fab_save_routine.setOnClickListener {
            val title = edit_title.text.toString()
            val content = edit_content.text.toString()
            if (title != "") {
                val intent = Intent().apply {
                    putExtra(EXTRA_ROUTINE, Routine(title, content))
                    putExtra(EXTRA_POS, pos)
                }
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }
}
