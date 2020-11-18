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

package com.noahjutz.gymroutines.ui.routines.create.pick

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.viewModel
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.noahjutz.gymroutines.ui.exercises.ExercisesViewModel
import dagger.hilt.android.AndroidEntryPoint

@Suppress("unused")
private const val TAG = "PickExerciseFragment"

@AndroidEntryPoint
class PickExerciseFragment : Fragment() {

    private val exercisesViewModel: ExercisesViewModel by viewModels()
    private val sharedExerciseViewModel: SharedExerciseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setContent {
            MaterialTheme(colors = if (isSystemInDarkTheme()) darkColors() else lightColors()) {
                PickExercise(
                    exercisesViewModel = viewModel(),
                    sharedExerciseViewModel = viewModel()
                )
            }
        }
    }
}

@Composable
fun PickExercise(
    exercisesViewModel: ExercisesViewModel,
    sharedExerciseViewModel: SharedExerciseViewModel
) {
    Scaffold(
        topBar = {
            val selectedCount by sharedExerciseViewModel.selectedCount.observeAsState()
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            // TODO
                        },
                        icon = {
                            val asset =
                                if (selectedCount == 0) Icons.Default.ArrowBack else Icons.Default.Done
                            Icon(asset)
                        }
                    )
                },
                title = {
                    Text("$selectedCount selected")
                }
            )
        },
        bodyContent = {
            val exercises by exercisesViewModel.exercises.observeAsState()
            LazyColumnFor(exercises ?: emptyList()) { exercise ->
                var checked by mutableStateOf(false)
                ListItem(
                    trailing = {
                        Icon(
                            asset = Icons.Filled.CheckCircle,
                            tint = androidx.compose.animation.animate(
                                if (checked) MaterialTheme.colors.primary
                                else AmbientContentColor.current.copy(alpha = 0.25f)
                            )
                        )
                    },
                    modifier = Modifier.clickable {
                        checked = !checked
                        if (checked) sharedExerciseViewModel.addExercise(exercise)
                        else sharedExerciseViewModel.removeExercise(exercise)
                    }
                ) {
                    Text(exercise.name)
                }
            }
        }
    )
}