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

package com.noahjutz.splitfit.ui.routines.create

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animate
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.isFocused
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.gesture.LongPressDragObserver
import androidx.compose.ui.gesture.longPressDragGestureFilter
import androidx.compose.ui.platform.AmbientFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.noahjutz.splitfit.R
import com.noahjutz.splitfit.data.domain.SetGroup
import com.noahjutz.splitfit.ui.routines.create.pick.SharedExerciseViewModel
import com.noahjutz.splitfit.util.RegexPatterns
import com.noahjutz.splitfit.util.SwipeToDeleteBackground
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.coroutineContext
import kotlin.math.floor

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun CreateRoutineScreen(
    onAddExercise: () -> Unit,
    popBackStack: () -> Unit,
    controller: CreateRoutineController,
    sharedExerciseVM: SharedExerciseViewModel,
    showSnackbar: suspend (String) -> Unit
) {
    val editor = controller.Editor()
    val presenter = controller.Presenter()

    rememberCoroutineScope().launch {
        sharedExerciseVM.exercises.value.let { exercises ->
            editor.addExercises(exercises)
            sharedExerciseVM.clear()
        }
    }

    onDispose {
        if (presenter.routine.value.isEmpty()) {
            MainScope().launch {
                showSnackbar("Empty routine discarded")
            }
        }
        editor.close()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddExercise,
                content = { Icon(Icons.Default.Add) },
                modifier = Modifier.testTag("addExerciseFab")
            )
        },
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = popBackStack,
                        content = { Icon(Icons.Default.ArrowBack) },
                        modifier = Modifier.testTag("backButton")
                    )
                },
                title = {
                    Box {
                        var nameFieldValue by remember {
                            mutableStateOf(
                                TextFieldValue(presenter.routine.value.name)
                            )
                        }
                        var focusState by remember { mutableStateOf(false) }
                        BasicTextField(
                            value = nameFieldValue,
                            onValueChange = {
                                nameFieldValue = it
                                editor.setName(it.text)
                            },
                            modifier = Modifier
                                .onFocusChanged {
                                    focusState = it.isFocused
                                }
                                .fillMaxWidth(),
                            textStyle = AmbientTextStyle.current.copy(
                                color = if (isSystemInDarkTheme()) MaterialTheme.colors.onSurface else MaterialTheme.colors.onPrimary
                            ),
                            singleLine = true,
                            cursorColor = if (isSystemInDarkTheme()) MaterialTheme.colors.onSurface else MaterialTheme.colors.onPrimary
                        )
                        if (nameFieldValue.text.isEmpty() && !focusState) {
                            Text(
                                stringResource(R.string.unnamed_routine),
                                modifier = Modifier.alpha(0.5f)
                            )
                        }
                    }
                }
            )
        }
    ) {
        val setGroups by presenter.routine.mapLatest { it.setGroups }.collectAsState(emptyList())
        LazyColumn(Modifier.fillMaxHeight()) {
            itemsIndexed(setGroups) { i, setGroup ->
                SetGroupCard(
                    setGroupIndex = i,
                    setGroup = setGroup,
                    editor = editor,
                    presenter = presenter
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
    editor: CreateRoutineController.Editor,
    presenter: CreateRoutineController.Presenter,
) {
    val exercise = presenter.getExercise(setGroup.exerciseId)

    // Temporary rearranging solution
    var offsetPosition by remember { mutableStateOf(0f) }
    var dragging by remember { mutableStateOf(false) }
    var toSwap by remember { mutableStateOf(Pair(0, 0)) }
    val focusManager = AmbientFocusManager.current
    onCommit(offsetPosition) {
        if (dragging) {
            toSwap = when {
                offsetPosition < -150 -> Pair(setGroupIndex, setGroupIndex - 1)
                offsetPosition > 150 -> Pair(setGroupIndex, setGroupIndex + 1)
                else -> Pair(0, 0)
            }
        } else {
            if (toSwap != Pair(0, 0)) {
                focusManager.clearFocus()
                editor.swapSetGroups(toSwap.first, toSwap.second)
                toSwap = Pair(0, 0)
            }
        }
    }

    Card(
        elevation = animate(if (offsetPosition == 0f) 0.dp else 4.dp),
        modifier = Modifier.fillMaxWidth()
            .offset(y = offsetPosition.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .clickable {}
                    .longPressDragGestureFilter(
                        longPressDragObserver = object : LongPressDragObserver {
                            override fun onDrag(dragDistance: Offset): Offset {
                                super.onDrag(dragDistance)
                                dragging = true
                                offsetPosition += dragDistance.y
                                return dragDistance
                            }

                            override fun onStop(velocity: Offset) {
                                super.onStop(velocity)
                                dragging = false
                                offsetPosition = 0f
                            }
                        }
                    )
            ) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = exercise?.name?.takeIf { it.isNotBlank() } ?: "Unnamed",
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

                onCommit(dismissState.value) {
                    if (dismissState.value != DismissValue.Default) {
                        focusManager.clearFocus()
                        editor.deleteSetFrom(setGroup, setIndex)
                        dismissState.snapTo(DismissValue.Default)
                    }
                }

                SwipeToDismiss(
                    state = dismissState,
                    background = { SwipeToDeleteBackground(dismissState) }
                ) {
                    Card(
                        elevation = animate(
                            if (dismissState.dismissDirection == null) 0.dp else 4.dp
                        )
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
                                        //viewModel.updateRoutine {
                                        //    setGroups[setGroupIndex].sets[setIndex].reps =
                                        //        it.takeIf { it.isNotEmpty() }?.toInt()
                                        //}
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
                                        //viewModel.updateRoutine {
                                        //    setGroups[setGroupIndex].sets[setIndex].weight =
                                        //        it.takeIf { it.isNotEmpty() }
                                        //            ?.toDouble()
                                        //}
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
                                        //viewModel.updateRoutine {
                                        //    setGroups[setGroupIndex].sets[setIndex].time =
                                        //        it.takeIf { it.isNotEmpty() }?.toInt()
                                        //}
                                    },
                                    regexPattern = RegexPatterns.time,
                                    visualTransformation = timeVisualTransformation
                                )

                            var distance = set.distance?.toString() ?: ""
                            if (exercise?.logDistance == true)
                                SetTextField(
                                    modifier = Modifier.weight(1f),
                                    value = distance,
                                    onValueChange = {
                                        distance = it
                                        //viewModel.updateRoutine {
                                        //    setGroups[setGroupIndex].sets[setIndex].distance =
                                        //        it.takeIf { it.isNotEmpty() }
                                        //            ?.toDouble()
                                        //}
                                    },
                                    regexPattern = RegexPatterns.float,
                                )
                        }
                    }
                }
            }
            TextButton(
                onClick = { editor.addSetTo(setGroup) },
                content = {
                    Icon(Icons.Default.Add)
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
    onCommit(value) {
        val v = when {
            value.isEmpty() -> value
            value.toDouble() == floor(value.toDouble()) -> value.toDouble().toInt().toString()
            else -> value
        }
        if (tfValue.text != value) tfValue = TextFieldValue(v, TextRange(value.length))
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
        textStyle = AmbientTextStyle.current.copy(
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.onSurface
        ),
        cursorColor = MaterialTheme.colors.onSurface,
        maxLines = 1
    )
}

/** Turns integer of 0-4 digits to MM:SS format */
val timeVisualTransformation = object : VisualTransformation {
    val offsetMap = object : OffsetMap {
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
