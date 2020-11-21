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

package com.noahjutz.gymroutines.ui.exercises

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.noahjutz.gymroutines.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExercisesFragment : Fragment() {

    private val viewModel: ExercisesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        // TODO:
        //  - Reflect changes in ExercisesViewModel.exercises asynchronously  (LiveData/StateFlow)
        //  - Implement SwipeToDismiss with AnimatedVisibility
        //  - Add topBar (for RoutinesFragment as well)
        //  - Refactor code (Extract layout from this code block, etc)
        setContent {
            MaterialTheme(colors = if (isSystemInDarkTheme()) darkColors() else lightColors()) {
                Scaffold(
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { addEditExercise(-1) },
                            icon = { Icon(Icons.Default.Add) }
                        )
                    },
                    bodyContent = {
                        val exercises by viewModel.exercises.observeAsState()
                        LazyColumnFor(exercises ?: emptyList()) { exercise ->
                            var visible by remember { mutableStateOf(!exercise.hidden) }
                            if (visible) {
                                ListItem(
                                    Modifier.clickable(
                                        onClick = { addEditExercise(exercise.exerciseId) },
                                        onLongClick = {
                                            viewModel.hide(exercise, true)
                                            visible = false
                                        }
                                    )
                                ) { Text(exercise.name) }
                            }
                        }
                    }
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initActivity()
    }

    private fun initActivity() {
        requireActivity().apply {
            findViewById<BottomNavigationView>(R.id.bottom_nav).visibility = VISIBLE
        }
    }

    fun addEditExercise(exerciseId: Int) {
        val exerciseIdToEdit =
            if (exerciseId < 1) viewModel.addExercise().toInt()
            else exerciseId
        val action = ExercisesFragmentDirections.addExercise(exerciseIdToEdit)
        findNavController().navigate(action)
    }
}
