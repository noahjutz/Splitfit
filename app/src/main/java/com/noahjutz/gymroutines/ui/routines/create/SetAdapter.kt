package com.noahjutz.gymroutines.ui.routines.create

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.data.AppDatabase
import com.noahjutz.gymroutines.data.domain.Set
import com.noahjutz.gymroutines.util.DiffUtilCallback
import kotlinx.android.synthetic.main.listitem_set.view.*
import kotlinx.coroutines.runBlocking

@Suppress("UNUSED")
private const val TAG = "SetAdapter"

class SetAdapter(val exerciseHolderId: Int) : ListAdapter<Set, SetAdapter.SetHolder>(diffUtil) {
    // TODO: Find another way of doing this.
    fun getItemPublic(position: Int): Set = getItem(position)

    inner class SetHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SetHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.listitem_set, parent, false)
    )

    override fun onBindViewHolder(holder: SetAdapter.SetHolder, position: Int) {
        val set = getItem(position)

        // TODO: pass exercise with dependency injection instead of creating dependency here
        val db = AppDatabase.getInstance(holder.itemView.context)
        val dao = db.exerciseImplDao
        val exercise = runBlocking {
            dao.getExerciseImpl(set.exerciseHolderId)?.exercise
                ?: throw NullPointerException("Set assigned to exercise that doesn't exist")
        }

        val setTextOrHide: EditText.(value: Any?, show: Boolean) -> Unit = { value, show ->
            if (show) setText(value?.toString() ?: "")
            else (parent.parent as View).visibility = GONE
        }

        holder.itemView.apply {
            edit_reps.setTextOrHide(set.reps, exercise.logReps)
            edit_weight.setTextOrHide(set.weight, exercise.logWeight)
            edit_time.setTextOrHide(set.time, exercise.logTime)
            edit_distance.setTextOrHide(set.distance, exercise.logDistance)
        }
    }
}

private val diffUtil = DiffUtilCallback<Set>({ old, new -> old.setId == new.setId })