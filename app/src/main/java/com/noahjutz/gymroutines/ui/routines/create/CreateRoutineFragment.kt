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

package com.noahjutz.gymroutines.ui.routines.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.compose.foundation.BaseTextField
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Text
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawOpacity
import androidx.compose.ui.focus.ExperimentalFocus
import androidx.compose.ui.focus.isFocused
import androidx.compose.ui.focusObserver
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.viewModel
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.data.domain.Set
import com.noahjutz.gymroutines.ui.routines.create.pick.SharedExerciseViewModel
import com.noahjutz.gymroutines.util.RegexPatterns
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalFocus
@ExperimentalFoundationApi
@AndroidEntryPoint
class CreateRoutineFragment : Fragment() {

    private val sharedExerciseViewModel: SharedExerciseViewModel by activityViewModels()
    private val viewModel: CreateRoutineEditor by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setContent {
            MaterialTheme(colors = if (isSystemInDarkTheme()) darkColors() else lightColors()) {
                CreateRoutineScreen(::addExercise, ::popBackStack)
            }
        }
    }

    private fun popBackStack() {
        findNavController().popBackStack()
    }

    private fun addExercise() {
        val action = CreateRoutineFragmentDirections.addExercise()
        findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initActivity()
    }

    private fun initActivity() {
        requireActivity().apply {
            findViewById<BottomNavigationView>(R.id.bottom_nav).visibility = GONE
        }
    }

    private fun initViewModel() {
        sharedExerciseViewModel.exercises.observe(viewLifecycleOwner) { exercises ->
            for (e in exercises) viewModel.updateRoutine { sets.add(Set(e.exerciseId)) }
            if (exercises.isNotEmpty()) sharedExerciseViewModel.clearExercises()
        }
    }
}

@ExperimentalFocus
@ExperimentalFoundationApi
@Composable
fun CreateRoutineScreen(
    onAddExercise: () -> Unit,
    popBackStack: () -> Unit,
) {
    val presenter = viewModel<CreateRoutinePresenter>()
    val editor = viewModel<CreateRoutineEditor>()

    val sets by presenter.sets.observeAsState()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddExercise, icon = { Icon(Icons.Default.Add) })
        },
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = popBackStack,
                        icon = { Icon(Icons.Default.ArrowBack) })
                },
                title = {
                    Box {
                        var nameFieldValue by remember { mutableStateOf(TextFieldValue(presenter.initialName)) }
                        var focusState by remember { mutableStateOf(false) }
                        BaseTextField(
                            value = nameFieldValue,
                            onValueChange = {
                                nameFieldValue = it
                                editor.updateRoutine { this.name = it.text }
                            },
                            modifier = Modifier.focusObserver {
                                focusState = it.isFocused
                            }
                        )
                        if (nameFieldValue.text.isEmpty() && !focusState) {
                            Text("Unnamed", modifier = Modifier.drawOpacity(0.5f))
                        }
                    }
                }
            )
        }
    ) {
        val setsNotNull = sets ?: emptyList()
        LazyColumnFor(
            items = mutableListOf<MutableList<Set>>().also { setGroupList ->
                setsNotNull.forEachIndexed { i, set ->
                    if (i == 0 || (i > 0 && setsNotNull[i - 1].exerciseId != set.exerciseId))
                        setGroupList.add(mutableListOf(set))
                    else setGroupList.last().add(set)
                }
            }
        ) { setGroup ->
            ExerciseCard(setGroup)
        }
    }
}

@ExperimentalFocus
@ExperimentalFoundationApi
@Composable
fun ExerciseCard(setGroup: List<Set>) {
    val editor = viewModel<CreateRoutineEditor>()
    val presenter = viewModel<CreateRoutinePresenter>()
    Card(Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp).fillMaxWidth()) {
        Column(Modifier.padding(horizontal = 16.dp)) {
            Text(
                modifier = Modifier.padding(vertical = 16.dp),
                text = presenter.getExerciseName(setGroup.first().exerciseId),
                fontSize = 20.sp
            )
            Column {
                Row(modifier = Modifier.padding(bottom = 16.dp)) {
                    Text(modifier = Modifier.weight(1f), text = "reps")
                    Text(modifier = Modifier.weight(1f), text = "weight")
                    Text(modifier = Modifier.weight(1f), text = "time")
                    Text(modifier = Modifier.weight(1f), text = "distance")
                }
                setGroup.forEachIndexed { i, set ->
                    Row(modifier = Modifier.padding(bottom = 16.dp)) {
                        var reps by remember {
                            mutableStateOf(TextFieldValue(set.reps?.toString() ?: ""))
                        }
                        BaseTextField(
                            modifier = Modifier.weight(1f).fillMaxWidth(),
                            value = reps,
                            onValueChange = {
                                if (it.text.matches(RegexPatterns.integer)) {
                                    reps = it
                                    editor.updateRoutine {
                                        sets[i].reps = reps.text.takeIf { it.isNotEmpty() }?.toInt()
                                    }
                                }
                            },
                            keyboardType = KeyboardType.Number
                        )

                        var weight by remember {
                            mutableStateOf(TextFieldValue(set.weight?.toString() ?: ""))
                        }
                        BaseTextField(
                            modifier = Modifier.weight(1f).fillMaxWidth().focusObserver {
                                if (!it.isFocused) weight =
                                    TextFieldValue(set.weight?.toString() ?: "")
                            },
                            value = weight,
                            onValueChange = {
                                if (it.text.matches(RegexPatterns.float)) {
                                    weight = it
                                    editor.updateRoutine {
                                        sets[i].weight =
                                            weight.text.takeIf { it.isNotEmpty() }?.toDouble()
                                    }
                                }
                            },
                            keyboardType = KeyboardType.Number
                        )

                        var time by remember {
                            mutableStateOf(TextFieldValue(set.time?.toString() ?: ""))
                        }
                        BaseTextField(
                            modifier = Modifier.weight(1f).fillMaxWidth(),
                            value = time,
                            onValueChange = {
                                if (it.text.matches(RegexPatterns.time)) {
                                    time = TextFieldValue(it.text, TextRange(it.text.length))
                                    editor.updateRoutine {
                                        sets[i].time = time.text.takeIf { it.isNotEmpty() }?.toInt()
                                    }
                                }
                            },
                            keyboardType = KeyboardType.Number,
                            visualTransformation = timeVisualTransformation,
                            cursorColor = Color.Transparent
                        )

                        var distance by remember {
                            mutableStateOf(TextFieldValue(set.distance?.toString() ?: ""))
                        }
                        BaseTextField(
                            modifier = Modifier.weight(1f).fillMaxWidth().focusObserver {
                                if (!it.isFocused) distance =
                                    TextFieldValue(set.distance?.toString() ?: "")
                            },
                            value = distance,
                            onValueChange = {
                                if (it.text.matches(RegexPatterns.float)) {
                                    distance = it
                                    editor.updateRoutine {
                                        sets[i].distance =
                                            distance.text.takeIf { it.isNotEmpty() }?.toDouble()
                                    }
                                }
                            },
                            keyboardType = KeyboardType.Number
                        )
                    }
                }
            }
        }
    }
}

/** Turns integer of 0-4 digits to MM:SS format */
val timeVisualTransformation = object : VisualTransformation {
    // TODO: Fix IllegalArgumentException when using the following OffsetMap
    //val offsetMap = object : OffsetMap {
    //    override fun originalToTransformed(offset: Int) = 5
    //    override fun transformedToOriginal(offset: Int) = 5 - offset
    //}

    override fun filter(text: AnnotatedString): TransformedText {
        val withZeroes = "0".repeat((4 - text.text.length).takeIf { it > 0 } ?: 0) + text.text
        val withColons = "${withZeroes[0]}${withZeroes[1]}:${withZeroes[2]}${withZeroes[3]}"
        return TransformedText(AnnotatedString(withColons), OffsetMap.identityOffsetMap)
    }
}