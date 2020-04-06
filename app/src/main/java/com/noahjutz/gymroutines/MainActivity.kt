package com.noahjutz.gymroutines

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.noahjutz.gymroutines.models.Routine
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MainActivity"

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

class MainActivity : AppCompatActivity(), RecyclerViewAdapter.OnRoutineClickListener {

    private lateinit var routineAdapter: RecyclerViewAdapter
    private lateinit var routineList: ArrayList<Routine>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()
        loadData()

        fab_add_routine.setOnClickListener {
            val intent = Intent(this, CreateRoutineActivity::class.java)
            startActivityForResult(intent, REQUEST_CREATE_ROUTINE)
        }

        button_sample_data.setOnClickListener {
            routineList = DataSource.createDataSet()
            routineAdapter.submitList(routineList)
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
                if (resultCode == Activity.RESULT_OK) {
                    val routine = data?.getParcelableExtra<Routine>(EXTRA_ROUTINE)
                    val pos = data?.getIntExtra(EXTRA_POS, -1)
                    if (routine != null && pos != null) {
                        try {
                            routineList[pos] = routine
                            routineAdapter.submitList(routineList)
                        } catch (e: ArrayIndexOutOfBoundsException) {
                            Toast.makeText(this, "Error: $e", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            REQUEST_CREATE_ROUTINE -> {
                if (resultCode == RESULT_OK) {
                    val routine = data?.getParcelableExtra<Routine>(EXTRA_ROUTINE)
                    if (routine != null) {
                        routineList.add(routine)
                        routineAdapter.submitList(routineList)
                    }
                }
            }
            REQUEST_VIEW_ROUTINE -> {
                if (resultCode == RESULT_OK) {
                    val action: Int = data?.getIntExtra(EXTRA_ACTION, -1) ?: -1
                    val pos: Int = data?.getIntExtra(EXTRA_POS, -1) ?: -1
                    Log.d(TAG, "Action: $action | Pos: $pos")

                    when (action) {
                        ACTION_DELETE -> {
                            try {
                                routineList.removeAt(pos)
                            } catch (e: ArrayIndexOutOfBoundsException) {
                                Toast.makeText(this, "Error: $e", Toast.LENGTH_SHORT).show()
                            }
                            routineAdapter.submitList(routineList)
                        }
                        ACTION_EDIT -> {
                            val intent = Intent(this, CreateRoutineActivity::class.java).apply {
                                putExtra(EXTRA_ROUTINE, routineList[pos])
                                putExtra(EXTRA_POS, pos)
                            }
                            startActivityForResult(intent, REQUEST_EDIT_ROUTINE)
                        }
                    }
                }
            }
        }
    }

    private fun initRecyclerView() {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            routineAdapter = RecyclerViewAdapter(this@MainActivity)
            adapter = routineAdapter
        }
    }

    override fun onRoutineClick(pos: Int) {
        val intent = Intent(this, ViewRoutineActivity::class.java).apply {
            putExtra("routine", routineList[pos])
            putExtra("pos", pos)
        }
        startActivityForResult(intent, REQUEST_VIEW_ROUTINE)
    }
}
