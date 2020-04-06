package com.noahjutz.gymroutines

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.noahjutz.gymroutines.models.Routine
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), RecyclerViewAdapter.OnRoutineClickListener {

    private val TAG = this::class.java.name
    private lateinit var routineAdapter: RecyclerViewAdapter
    private lateinit var routineList: ArrayList<Routine>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()
        loadData()

        fab_add_routine.setOnClickListener {
            val intent = Intent(this, CreateRoutineActivity::class.java)
            startActivityForResult(intent, 420)
        }

        // button_sample_data.setOnClickListener {
        //     routineList = DataSource.createDataSet()
        //     routineAdapter.submitList(routineList)
        // }
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

        if (requestCode == 420) {
            if (resultCode == RESULT_OK) {
                val title = data?.getStringExtra("title")
                val content = data?.getStringExtra("content")
                Log.d(TAG, "Result OK: $title - $content")
                val routine: Routine = Routine("" + title, "" + content)
                routineList.add(routine)
                routineAdapter.submitList(routineList)
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.d(TAG, "Result canceled")
            }
        }

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                val delete: Boolean = data?.getBooleanExtra("delete", false) ?: false
                val pos: Int = data?.getIntExtra("pos", -1) ?: -1
                Log.d(TAG, "deleting: $delete, $pos")
                if (delete) {
                    routineList.removeAt(pos)
                    routineAdapter.submitList(routineList)
                    Log.d(TAG, "at pos: $pos")
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
        Log.d(TAG, "$pos")
        //try {
        //    routineList.removeAt(pos)
        //} catch (e: ArrayIndexOutOfBoundsException) {}
        //routineAdapter.submitList(routineList)
        val intent = Intent(this, ViewRoutineActivity::class.java)
        intent.putExtra("routine", routineList[pos])
        intent.putExtra("pos", pos)
        startActivityForResult(intent, 1)
    }
}
