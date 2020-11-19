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
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawOpacity
import androidx.compose.ui.focus.ExperimentalFocus
import androidx.compose.ui.focus.isFocused
import androidx.compose.ui.focusObserver
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.viewinterop.viewModel
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.noahjutz.gymroutines.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateExerciseFragment : Fragment() {

    private val viewModel: CreateExerciseViewModel by viewModels()
    private val args: CreateExerciseFragmentArgs by navArgs()

    @ExperimentalFocus
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireActivity()).apply {
        setContent {
            MaterialTheme(colors = if (isSystemInDarkTheme()) darkColors() else lightColors()) {
                val viewModel = viewModel<CreateExerciseViewModel>()
                val exercise by viewModel.exercise.observeAsState()
                // TODO: Merge copy-paste-code from CreateRoutineFragment
                Scaffold(
                    topBar = {
                        TopAppBar(
                            navigationIcon = {
                                IconButton(
                                    onClick = ::popBackStack,
                                    icon = { Icon(Icons.Default.ArrowBack) },
                                )
                            },
                            title = {
                                Box {
                                    var nameFieldValue by remember {
                                        mutableStateOf(
                                            TextFieldValue(
                                                viewModel.exercise.value?.name ?: "Unnamed"
                                            )
                                        )
                                    }
                                    var focusState by remember { mutableStateOf(false) }
                                    BasicTextField(
                                        value = nameFieldValue,
                                        onValueChange = {
                                            nameFieldValue = it
                                            viewModel.updateExercise { name = it.text }
                                        },
                                        modifier = Modifier
                                            .focusObserver {
                                                focusState = it.isFocused
                                            }
                                            .fillMaxWidth(),
                                        textStyle = AmbientTextStyle.current.copy(
                                            color = if (isSystemInDarkTheme()) MaterialTheme.colors.onSurface else MaterialTheme.colors.onPrimary
                                        ),
                                        maxLines = 1,
                                        cursorColor = if (isSystemInDarkTheme()) MaterialTheme.colors.onSurface else MaterialTheme.colors.onPrimary
                                    )
                                    if (nameFieldValue.text.isEmpty() && !focusState) {
                                        Text("Unnamed", modifier = Modifier.drawOpacity(0.5f))
                                    }
                                }
                            }
                        )
                    },
                    bodyContent = {
                        // TODO: Reuse checkbox ListItem component
                        ScrollableColumn {
                            var repsChecked by remember { mutableStateOf(exercise!!.logReps) }
                            onCommit(repsChecked) {
                                viewModel.updateExercise {
                                    logReps = repsChecked
                                }
                            }
                            ListItem(
                                text = { Text("Log Reps") },
                                icon = {
                                    Checkbox(
                                        checked = repsChecked,
                                        onCheckedChange = { repsChecked = it }
                                    )
                                },
                                modifier = Modifier.clickable { repsChecked = !repsChecked }
                            )
                            var weightChecked by remember { mutableStateOf(exercise!!.logWeight) }
                            onCommit(weightChecked) {
                                viewModel.updateExercise {
                                    logWeight = weightChecked
                                }
                            }
                            ListItem(
                                text = { Text("Log Weight") },
                                icon = {
                                    Checkbox(
                                        checked = weightChecked,
                                        onCheckedChange = { weightChecked = it }
                                    )
                                },
                                modifier = Modifier.clickable { weightChecked = !weightChecked }
                            )
                            var timeChecked by remember { mutableStateOf(exercise!!.logTime) }
                            onCommit(timeChecked) {
                                viewModel.updateExercise {
                                    logTime = timeChecked
                                }
                            }
                            ListItem(
                                text = { Text("Log Time") },
                                icon = {
                                    Checkbox(
                                        checked = timeChecked,
                                        onCheckedChange = { timeChecked = it }
                                    )
                                },
                                modifier = Modifier.clickable { timeChecked = !timeChecked }
                            )
                            var distanceChecked by remember { mutableStateOf(exercise!!.logDistance) }
                            onCommit(distanceChecked) {
                                viewModel.updateExercise {
                                    logDistance = distanceChecked
                                }
                            }
                            ListItem(
                                text = { Text("Log Distance") },
                                icon = {
                                    Checkbox(
                                        checked = distanceChecked,
                                        onCheckedChange = { distanceChecked = it }
                                    )
                                },
                                modifier = Modifier.clickable { distanceChecked = !distanceChecked }
                            )
                        }
                    }
                )
            }
        }
    }

    private fun popBackStack() {
        findNavController().popBackStack()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().apply {
            findViewById<BottomNavigationView>(R.id.bottom_nav).visibility = GONE
        }
    }
}
