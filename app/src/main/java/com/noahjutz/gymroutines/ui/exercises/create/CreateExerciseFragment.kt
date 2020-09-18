/*
 * GymRoutines
 * Copyright (C) 2020  Noah Jutz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.noahjutz.gymroutines.ui.exercises.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.databinding.FragmentCreateExerciseBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class CreateExerciseFragment : Fragment() {

    private val viewModel: CreateExerciseViewModel by viewModels()
    private val args: CreateExerciseFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentCreateExerciseBinding>(
        inflater, R.layout.fragment_create_exercise, container, false
    ).apply {
        fragment = this@CreateExerciseFragment
        lifecycleOwner = viewLifecycleOwner
        viewmodel = viewModel
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initActivity()
        initViewModel()
    }

    private fun initActivity() {
        requireActivity().apply {
            title = if (args.exerciseId == -1) "Create Exercise" else "Edit Exercise"
            bottom_nav.visibility = GONE
        }
    }

    private fun initViewModel() {
        viewModel.exercise.observe(viewLifecycleOwner, Observer {})
    }
}
