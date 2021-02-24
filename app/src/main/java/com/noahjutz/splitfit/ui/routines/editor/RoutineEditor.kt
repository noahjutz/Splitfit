/*
 * Splitfit
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

package com.noahjutz.splitfit.ui.routines.editor

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.isFocused
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.noahjutz.splitfit.R
import com.noahjutz.splitfit.data.domain.SetGroup
import com.noahjutz.splitfit.ui.components.AppBarTextField
import com.noahjutz.splitfit.ui.components.SwipeToDeleteBackground
import com.noahjutz.splitfit.ui.exercises.picker.SharedExercisePickerViewModel
import com.noahjutz.splitfit.util.DatastoreKeys
import com.noahjutz.splitfit.util.RegexPatterns
import com.noahjutz.splitfit.util.get
import com.noahjutz.splitfit.util.getViewModel
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import java.util.*
import kotlin.math.floor

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun CreateRoutineScreen(
    onAddExercise: () -> Unit,
    startWorkout: (Int) -> Unit,
    popBackStack: () -> Unit,
    routineId: Int,
    viewModel: RoutineEditorViewModel = getViewModel { parametersOf(routineId) },
    sharedExercisePickerViewModel: SharedExercisePickerViewModel,
    preferences: DataStore<Preferences> = get(),
) {
    val preferencesData by preferences.data.collectAsState(null)
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    scope.launch {
        viewModel.editor.addExercises(sharedExercisePickerViewModel.exercises.value)
        sharedExercisePickerViewModel.clear()
    }

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddExercise,
                icon = { Icon(Icons.Default.Add, null) },
                text = { Text("Add Exercise") },
                modifier = Modifier.testTag("addExerciseFab"),
            )
        },
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = popBackStack,
                        content = { Icon(Icons.Default.ArrowBack, null) },
                        modifier = Modifier.testTag("backButton")
                    )
                },
                title = {
                    var nameFieldValue by remember { mutableStateOf(viewModel.presenter.routine.value.name) }
                    AppBarTextField(
                        value = nameFieldValue,
                        onValueChange = {
                            nameFieldValue = it
                            viewModel.editor.setName(it)
                        },
                        hint = stringResource(R.string.unnamed_routine),
                    )
                },
                actions = {
                    var showMenu by remember { mutableStateOf(false) }
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(Icons.Default.MoreVert, null)
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                onClick = {
                                    val currentWorkout =
                                        preferencesData?.get(DatastoreKeys.currentWorkout)
                                    if (currentWorkout == null || currentWorkout < 0) {
                                        startWorkout(viewModel.presenter.routine.value.routineId)
                                    } else {
                                        scope.launch {
                                            scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                                            val snackbarResult =
                                                scaffoldState.snackbarHostState.showSnackbar(
                                                    "A workout is already in progress.",
                                                    "Stop current"
                                                )
                                            if (snackbarResult == SnackbarResult.ActionPerformed) {
                                                preferences.edit {
                                                    it[DatastoreKeys.currentWorkout] = -1
                                                }
                                                scaffoldState.snackbarHostState.showSnackbar("Current workout finished.")
                                            }
                                        }
                                    }
                                }
                            ) {
                                Text("Start workout")
                            }
                        }
                    }
                }
            )
        }
    ) {
        val setGroups by viewModel.presenter.routine.mapLatest { it.setGroups }
            .collectAsState(emptyList())
        LazyColumn(Modifier.fillMaxHeight()) {
            itemsIndexed(setGroups) { i, setGroup ->
                SetGroupCard(
                    setGroupIndex = i,
                    setGroup = setGroup,
                    viewModel = viewModel
                )
            }
            item {
                // Fix FAB overlap
                Box(Modifier.height(72.dp)) {}
            }
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun SetGroupCard(
    setGroupIndex: Int,
    setGroup: SetGroup,
    viewModel: RoutineEditorViewModel,
) {
    val scope = rememberCoroutineScope()
    val exercise = viewModel.presenter.getExercise(setGroup.exerciseId)

    // Temporary rearranging solution
    var offsetPosition by remember { mutableStateOf(0f) }
    var dragging by remember { mutableStateOf(false) }
    var toSwap by remember { mutableStateOf(Pair(0, 0)) }
    val focusManager = LocalFocusManager.current
    DisposableEffect(offsetPosition) {
        if (dragging) {
            toSwap = when {
                offsetPosition < -150 -> Pair(setGroupIndex, setGroupIndex - 1)
                offsetPosition > 150 -> Pair(setGroupIndex, setGroupIndex + 1)
                else -> Pair(0, 0)
            }
        } else {
            if (toSwap != Pair(0, 0)) {
                focusManager.clearFocus()
                viewModel.editor.swapSetGroups(toSwap.first, toSwap.second)
                toSwap = Pair(0, 0)
            }
        }
        onDispose {}
    }

    Card(
        elevation = animateDpAsState(if (offsetPosition == 0f) 0.dp else 4.dp).value,
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = offsetPosition.dp)
    ) {
        Column {
            Row(Modifier.fillMaxWidth()) {
                // TODO rearranging (same as in WorkoutEditor)
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = exercise?.name?.takeIf { it.isNotBlank() }
                        ?: stringResource(R.string.unnamed_routine),
                    fontSize = 20.sp,
                )
            }
            Row(modifier = Modifier.padding(bottom = 16.dp, start = 16.dp, end = 16.dp)) {
                if (exercise?.logReps == true) SetHeader(stringResource(R.string.reps))
                if (exercise?.logWeight == true) SetHeader(stringResource(R.string.weight))
                if (exercise?.logTime == true) SetHeader(stringResource(R.string.time))
                if (exercise?.logDistance == true) SetHeader(stringResource(R.string.distance))
            }
            setGroup.sets.forEachIndexed { setIndex, set ->
                val dismissState = rememberDismissState()

                DisposableEffect(dismissState.currentValue) {
                    if (dismissState.currentValue != DismissValue.Default) {
                        focusManager.clearFocus()
                        viewModel.editor.deleteSetFrom(setGroup, setIndex)
                        scope.launch {
                            dismissState.snapTo(DismissValue.Default)
                        }
                    }
                    onDispose {}
                }

                SwipeToDismiss(
                    state = dismissState,
                    background = { SwipeToDeleteBackground(dismissState) }
                ) {
                    Card(
                        elevation = animateDpAsState(
                            if (dismissState.dismissDirection == null) 0.dp else 4.dp
                        ).value
                    ) {
                        Row(
                            modifier = Modifier.padding(
                                vertical = 8.dp,
                                horizontal = 16.dp
                            )
                        ) {
                            var reps = set.reps?.toString() ?: ""
                            if (exercise?.logReps == true)
                                SetTextField(
                                    modifier = Modifier.weight(1f),
                                    value = reps,
                                    onValueChange = {
                                        reps = it
                                        val repsValue = it.takeIf { it.isNotEmpty() }?.toInt()
                                        viewModel.editor.updateSet(
                                            setGroupIndex,
                                            setIndex,
                                            reps = repsValue
                                        )
                                    },
                                    regexPattern = RegexPatterns.integer,
                                )

                            var weight = set.weight?.toString() ?: ""
                            if (exercise?.logWeight == true)
                                SetTextField(
                                    modifier = Modifier.weight(1f),
                                    value = weight,
                                    onValueChange = {
                                        weight = it
                                        val weightValue = it.takeIf { it.isNotEmpty() }?.toDouble()
                                        viewModel.editor.updateSet(
                                            setGroupIndex,
                                            setIndex,
                                            weight = weightValue
                                        )
                                    },
                                    regexPattern = RegexPatterns.float,
                                )

                            var time = set.time?.toString() ?: ""
                            if (exercise?.logTime == true)
                                SetTextField(
                                    modifier = Modifier.weight(1f),
                                    value = time,
                                    onValueChange = {
                                        time = it
                                        val timeValue = it.takeIf { it.isNotEmpty() }?.toInt()
                                        viewModel.editor.updateSet(
                                            setGroupIndex,
                                            setIndex,
                                            time = timeValue
                                        )
                                    },
                                    regexPattern = RegexPatterns.duration,
                                    visualTransformation = timeVisualTransformation
                                )

                            var distance = set.distance?.toString() ?: ""
                            if (exercise?.logDistance == true)
                                SetTextField(
                                    modifier = Modifier.weight(1f),
                                    value = distance,
                                    onValueChange = {
                                        distance = it
                                        val distanceValue =
                                            it.takeIf { it.isNotEmpty() }?.toDouble()
                                        viewModel.editor.updateSet(
                                            setGroupIndex,
                                            setIndex,
                                            distance = distanceValue
                                        )
                                    },
                                    regexPattern = RegexPatterns.float,
                                )
                        }
                    }
                }
            }
            TextButton(
                onClick = { viewModel.editor.addSetTo(setGroup) },
                content = {
                    Icon(Icons.Default.Add, null)
                    Text(stringResource(R.string.add_set))
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun RowScope.SetHeader(
    text: String,
) {
    Text(
        text = text.capitalize(Locale.getDefault()),
        textAlign = TextAlign.Center,
        modifier = Modifier.weight(1f),
        maxLines = 1,
        fontWeight = FontWeight.Bold
    )
}

@ExperimentalFoundationApi
@Composable
fun SetTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit = {},
    visualTransformation: VisualTransformation = VisualTransformation.None,
    regexPattern: Regex = Regex(""),
) {
    var tfValue by remember { mutableStateOf(TextFieldValue(value)) }
    DisposableEffect(value) {
        val v = when {
            value.isEmpty() -> value
            value.toDouble() == floor(value.toDouble()) -> value.toDouble().toInt().toString()
            else -> value
        }
        if (tfValue.text != value) tfValue = TextFieldValue(v, TextRange(value.length))
        onDispose {}
    }

    // onValueChange is called after onFocusChanged, overriding the selection in onFocusChanged.
    // Fix: Lock onValueChange when calling onFocusChanged
    var valueChangeLock = false
    BasicTextField(
        value = tfValue,
        onValueChange = {
            if (valueChangeLock) {
                valueChangeLock = false
                return@BasicTextField
            }
            if (it.text.matches(regexPattern)) {
                tfValue = TextFieldValue(it.text, TextRange(it.text.length))
                onValueChange(it.text)
            }
        },
        modifier = modifier
            .padding(horizontal = 4.dp)
            .clip(shape = RoundedCornerShape(8.dp))
            .background(MaterialTheme.colors.onSurface.copy(alpha = 0.12f))
            .padding(4.dp)
            .onFocusChanged {
                if (it.isFocused) {
                    valueChangeLock = true
                    tfValue = TextFieldValue(value, TextRange(0, value.length))
                }
            },
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        textStyle = LocalTextStyle.current.copy(
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.onSurface
        ),
        cursorBrush = SolidColor(MaterialTheme.colors.onSurface),
        maxLines = 1
    )
}

/** Turns integer of 0-4 digits to MM:SS format */
val timeVisualTransformation = object : VisualTransformation {
    val offsetMap = object : OffsetMapping {
        override fun originalToTransformed(offset: Int) = if (offset == 0) 0 else 5
        override fun transformedToOriginal(offset: Int) = 5 - offset
    }

    override fun filter(text: AnnotatedString): TransformedText {
        val withZeroes = "0".repeat((4 - text.text.length).takeIf { it > 0 } ?: 0) + text.text
        val withColon = withZeroes.let { it.substring(0, 2) + ":" + it.substring(2, 4) }
        return TransformedText(
            AnnotatedString(if (text.text.isEmpty()) "" else withColon),
            offsetMap
        )
    }
}
