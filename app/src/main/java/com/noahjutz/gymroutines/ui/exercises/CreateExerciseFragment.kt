package com.noahjutz.gymroutines.ui.exercises

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.databinding.FragmentCreateExerciseBinding

private const val TAG = "CreateExerciseFragment"

class CreateExerciseFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentCreateExerciseBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_create_exercise, container, false
        )

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.title= "Create Exercise"

        populateViews()
    }

    private fun populateViews() {
        Log.d(TAG, "populateViews(): called")
        // TODO: Populate views if called as edit exercise activity
    }

}
