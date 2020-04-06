package com.noahjutz.gymroutines

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.noahjutz.gymroutines.models.Routine
import kotlinx.android.synthetic.main.activity_view_routine.*

class ViewRoutineActivity : AppCompatActivity() {

    private var pos: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_routine)

        val intent = intent
        val routine: Routine = intent.getParcelableExtra("routine")!!
        pos = intent.getIntExtra("pos", -1)

        view_title.text = routine.title
        view_content.text = routine.content
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_view_routine, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_routine -> {
                val intent = Intent()
                intent.putExtra("delete", true)
                intent.putExtra("pos", pos)
                setResult(Activity.RESULT_OK, intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
