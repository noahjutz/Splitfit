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
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawOpacity
import androidx.compose.ui.focus.ExperimentalFocus
import androidx.compose.ui.focus.FocusState
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
                CreateRoutineScreen(::addExercise, ::popUp, ::deleteSet)
            }
        }
    }

    private fun deleteSet(set: Set) {
        viewModel.updateRoutine { sets.remove(set) }
    }

    private fun popUp() {
        findNavController().popBackStack()
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

    fun addExercise() {
        val action = CreateRoutineFragmentDirections.addExercise()
        findNavController().navigate(action)
    }
}

@ExperimentalFocus
@ExperimentalFoundationApi
@Composable
fun CreateRoutineScreen(
    onAddExercise: () -> Unit,
    popUp: () -> Unit,
    deleteSet: (Set) -> Unit
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
                        onClick = popUp,
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
                                focusState = it == FocusState.Active
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
        Column {
            SetList(sets ?: emptyList(), deleteSet)
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun SetList(
    sets: List<Set>,
    deleteSet: (Set) -> Unit
) {
    LazyColumnFor(
        items = mutableListOf<MutableList<Set>>().also { setGroupList ->
            sets.forEachIndexed { i, set ->
                if (i == 0 || (i > 0 && sets[i - 1].exerciseId != set.exerciseId))
                    setGroupList.add(mutableListOf(set))
                else setGroupList.last().add(set)
            }
        }
    ) { setGroup ->
        ExerciseCard(setGroup)
    }
}

@Composable
fun FancyCard(modifier: Modifier = Modifier, children: @Composable() () -> Unit) {
    Card(
        modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth()
    ) {
        children()
    }
}

@ExperimentalFoundationApi
@Composable
fun ExerciseCard(setGroup: List<Set>) {
    val editor = viewModel<CreateRoutineEditor>()
    val presenter = viewModel<CreateRoutinePresenter>()
    FancyCard {
        Column(Modifier.padding(horizontal = 16.dp)) {
            Text(
                modifier = Modifier.padding(vertical = 16.dp),
                text = presenter.getExerciseName(setGroup.first().exerciseId),
                fontSize = 20.sp
            )
            Column {
                DataTableRow(modifier = Modifier.padding(bottom = 16.dp)) {
                    Text(modifier = Modifier.weight(1f), text = "set")
                    Text(modifier = Modifier.weight(1f), text = "reps")
                    Text(modifier = Modifier.weight(1f), text = "weight")
                    Text(modifier = Modifier.weight(1f), text = "time")
                    Text(modifier = Modifier.weight(1f), text = "distance")
                }
                setGroup.forEachIndexed { i, set ->
                    DataTableRow(modifier = Modifier.padding(bottom = 16.dp)) {
                        Text(modifier = Modifier.weight(1f), text = (0..5).random().toString())
                        SetTextField(
                            modifier = Modifier.weight(1f),
                            text = set.reps?.toString() ?: "",
                            onValueChange = {
                                val reps = if (it.isEmpty()) null
                                else it.split('.').first().toInt()
                                editor.updateRoutine { sets[i].reps = reps }
                            },
                            inputValidation = InputValidationType.Integer
                        )
                        SetTextField(
                            modifier = Modifier.weight(1f),
                            text = set.weight?.toString() ?: "",
                            onValueChange = {
                                val weight = if (it.isEmpty()) null
                                else it.toDouble()
                                editor.updateRoutine { sets[i].weight = weight }
                            }
                        )
                        SetTextField(
                            modifier = Modifier.weight(1f),
                            text = set.time?.toString() ?: "",
                            onValueChange = {
                                val time = if (it.isEmpty()) null
                                else it.split('.').first().toInt()
                                editor.updateRoutine { sets[i].time = time }
                            },
                            inputValidation = InputValidationType.Time
                        )
                        SetTextField(
                            modifier = Modifier.weight(1f),
                            text = set.distance?.toString() ?: "",
                            onValueChange = {
                                val distance = if (it.isEmpty()) null
                                else it.toDouble()
                                editor.updateRoutine { sets[i].distance = distance }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DataTableRow(
    modifier: Modifier = Modifier,
    children: @Composable RowScope.() -> Unit
) {
    Row(modifier = modifier.fillMaxWidth()) {
        children()
    }
}

@ExperimentalFoundationApi
@Composable
fun SetTextField(
    modifier: Modifier = Modifier,
    text: String,
    onValueChange: (String) -> Unit,
    inputValidation: InputValidationType = InputValidationType.Float,
) {
    var textFieldValue by remember { mutableStateOf(TextFieldValue(text)) }
    BaseTextField(
        modifier = modifier.width(64.dp),
        value = textFieldValue,
        onValueChange = { newValue ->
            val matches = when (inputValidation) {
                InputValidationType.Integer -> newValue.text.matches(RegexPatterns.integer)
                InputValidationType.Float -> newValue.text.matches(RegexPatterns.float)
                InputValidationType.Time -> newValue.text.matches(RegexPatterns.time)
            }
            if (matches) {
                textFieldValue = if (inputValidation != InputValidationType.Time) newValue
                else TextFieldValue(newValue.text, TextRange(newValue.text.length))
                onValueChange(newValue.text)
            }
        },
        keyboardType = KeyboardType.Number,
        visualTransformation = if (inputValidation == InputValidationType.Time) timeVisualTransformation else VisualTransformation.None,
        cursorColor = if (inputValidation == InputValidationType.Time) Color.Transparent else AmbientContentColor.current
    )
}

enum class InputValidationType {
    Integer, Float, Time
}

/** Turns integer of 0-4 digits to MM:SS format */
val timeVisualTransformation = object : VisualTransformation {
    // TODO: Fix IllegalArgumentException when using the following OffsetMap
    //val offsetMap = object : OffsetMap {
    //    override fun originalToTransformed(offset: Int) = 5
    //    override fun transformedToOriginal(offset: Int) = 5 - offset
    //}

    override fun filter(text: AnnotatedString): TransformedText {
        val withZeroes = "0".repeat(4 - text.text.length) + text.text
        val withColons = "${withZeroes[0]}${withZeroes[1]}:${withZeroes[2]}${withZeroes[3]}"
        return TransformedText(AnnotatedString(withColons), OffsetMap.identityOffsetMap)
    }
}