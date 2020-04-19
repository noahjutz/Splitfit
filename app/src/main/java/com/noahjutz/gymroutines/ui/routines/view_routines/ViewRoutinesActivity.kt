package com.noahjutz.gymroutines.ui.routines.view_routines

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.ui.routines.view_routine.ViewRoutineActivity
import com.noahjutz.gymroutines.ui.routines.create_routine.CreateRoutineActivity
import com.noahjutz.gymroutines.models.Routine
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "ViewRoutinesActivity"

// Extra names
const val EXTRA_ROUTINE = "com.noahjutz.gymroutines.ROUTINE"
const val EXTRA_ACTION = "com.noahjutz.gymroutines.ACTION"
const val EXTRA_POS = "com.noahjutz.gymroutines.POS"

// Actions to perform on routines
const val ACTION_DELETE = 0
const val ACTION_EDIT = 1

// request IDs for startActivityForResult()
const val REQUEST_VIEW_ROUTINE = 0
const val REQUEST_CREATE_ROUTINE = 1
const val REQUEST_EDIT_ROUTINE = 2

class MainActivity : AppCompatActivity(),
    ViewRoutinesRoutineRecyclerAdapter.OnRoutineClickListener {

    private lateinit var routineAdapter: ViewRoutinesRoutineRecyclerAdapter
    private lateinit var routineList: ArrayList<Routine>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()
        loadData()

        fab_add_routine.setOnClickListener {
            val intent = Intent(this, CreateRoutineActivity::class.java)
            startActivityForResult(intent,
                REQUEST_CREATE_ROUTINE
            )
        }
    }

    override fun onPause() {
        super.onPause()
        saveData()
    }

    private fun saveData() {
        val sharedPreferences = getSharedPreferences("sharedprefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val routineListJson = gson.toJson(routineList)
        editor.putString("data", routineListJson)
        editor.apply()
    }

    private fun loadData() {
        val sharedPreferences = getSharedPreferences("sharedprefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("data", null)
        if (json != null) {
            val type = object : TypeToken<ArrayList<Routine>>() {}.type
            val dataAsList: ArrayList<Routine> = gson.fromJson(json, type)
            routineList = dataAsList
            routineAdapter.submitList(routineList)
        } else {
            routineList = ArrayList<Routine>()
            routineAdapter.submitList(routineList)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_EDIT_ROUTINE -> {
                // TODO
            }
            REQUEST_CREATE_ROUTINE -> {
                // TODO
            }
            REQUEST_VIEW_ROUTINE -> {
                if (resultCode == RESULT_OK) {
                    val action: Int = data?.getIntExtra(EXTRA_ACTION, -1) ?: -1
                    val pos: Int = data?.getIntExtra(EXTRA_POS, -1) ?: -1
                    Log.d(TAG, "Action: $action | Pos: $pos")

                    when (action) {
                        ACTION_DELETE -> {
                            deleteRoutine(pos)
                        }
                        ACTION_EDIT -> {
                            editRoutine(pos)
                        }
                    }
                }
            }
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as? AdapterView.AdapterContextMenuInfo
        return when (item.itemId) {
            1420 -> { // Delete
                deleteRoutine(item.groupId)
                true
            }
            1421 -> { // Edit
                editRoutine(item.groupId)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun deleteRoutine(pos: Int) {
        try {
            routineList.removeAt(pos)
        } catch (e: ArrayIndexOutOfBoundsException) {
            Toast.makeText(this, "Error: $e", Toast.LENGTH_SHORT).show()
        }
        routineAdapter.submitList(routineList)
    }

    private fun editRoutine(pos: Int) {
        // TODO
        startActivityForResult(intent,
            REQUEST_EDIT_ROUTINE
        )
    }

    private fun initRecyclerView() {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            routineAdapter =
                ViewRoutinesRoutineRecyclerAdapter(
                    this@MainActivity
                )
            adapter = routineAdapter
        }
    }

    override fun onRoutineClick(pos: Int) {
        // TODO
        startActivityForResult(intent,
            REQUEST_VIEW_ROUTINE
        )
    }
}
