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

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animate
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnForIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawOpacity
import androidx.compose.ui.focus.ExperimentalFocus
import androidx.compose.ui.focus.isFocused
import androidx.compose.ui.focusObserver
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.gesture.LongPressDragObserver
import androidx.compose.ui.gesture.longPressDragGestureFilter
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Transformations
import com.noahjutz.gymroutines.data.domain.SetGroup
import com.noahjutz.gymroutines.ui.routines.create.pick.SharedExerciseViewModel
import com.noahjutz.gymroutines.util.RegexPatterns
import kotlinx.coroutines.launch
import java.util.*

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalFocus
@ExperimentalFoundationApi
@Composable
fun CreateRoutineScreen(
    onAddExercise: () -> Unit,
    popBackStack: () -> Unit,
    viewModel: CreateRoutineViewModel,
    sharedExerciseVM: SharedExerciseViewModel
) {
    rememberCoroutineScope().launch {
        sharedExerciseVM.exercises.value.let { exercises ->
            viewModel.appendSets(exercises.map { it.exerciseId })
            sharedExerciseVM.clear()
        }
    }
    val setGroups by Transformations.map(viewModel.routineLiveData!!) {
        it?.setGroups ?: emptyList()
    }
        .observeAsState()
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
                        var nameFieldValue by remember { mutableStateOf(TextFieldValue(viewModel.initialName)) }
                        var focusState by remember { mutableStateOf(false) }
                        BasicTextField(
                            value = nameFieldValue,
                            onValueChange = {
                                nameFieldValue = it
                                viewModel.updateRoutine { this.name = it.text }
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
        }
    ) {
        LazyColumnForIndexed(
            items = setGroups ?: emptyList(),
            modifier = Modifier.fillMaxHeight()
        ) { i, setGroup ->
            SetGroupCard(
                setGroupIndex = i,
                setGroup = setGroup,
                viewModel = viewModel,
            )
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalFocus
@ExperimentalFoundationApi
@Composable
fun SetGroupCard(
    setGroupIndex: Int,
    setGroup: SetGroup,
    viewModel: CreateRoutineViewModel,
) {
    // Temporary rearranging solution
    var offsetPosition by remember { mutableStateOf(0f) }
    var dragging by remember { mutableStateOf(false) }
    var toSwap by remember { mutableStateOf(Pair(0, 0)) }
    onCommit(offsetPosition) {
        if (dragging) {
            toSwap = when {
                offsetPosition < -150 && viewModel.getSetGroup(setGroupIndex - 1) != null ->
                    Pair(setGroupIndex, setGroupIndex - 1)
                offsetPosition > 150 && viewModel.getSetGroup(setGroupIndex + 1) != null ->
                    Pair(setGroupIndex, setGroupIndex + 1)
                else -> Pair(0, 0)
            }
        } else {
            if (toSwap != Pair(0, 0)) {
                viewModel.swapSetGroups(toSwap.first, toSwap.second)
                toSwap = Pair(0, 0)
            }
        }
    }

    Card(
        elevation = animate(if (offsetPosition == 0f) 0.dp else 4.dp),
        modifier = Modifier.fillMaxWidth()
            .offset(y = { offsetPosition })
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
                    text = viewModel.getExerciseName(setGroup.exerciseId),
                    fontSize = 20.sp,
                )
            }
            Row(modifier = Modifier.padding(bottom = 16.dp, start = 16.dp, end = 16.dp)) {
                SetHeader("reps")
                SetHeader("weight")
                SetHeader("time")
                SetHeader("distance")
            }
            setGroup.sets.forEachIndexed { setIndex, set ->
                val dismissState = rememberDismissState()

                onCommit(dismissState.value) {
                    if (dismissState.value != DismissValue.Default) {
                        viewModel.updateRoutine {
                            setGroups[setGroupIndex].sets.removeAt(setIndex)
                            if (setGroups[setGroupIndex].sets.isEmpty()) setGroups.removeAt(
                                setGroupIndex
                            )
                        }
                        dismissState.snapTo(DismissValue.Default)
                    }
                }

                SwipeToDismiss(
                    state = dismissState,
                    background = {
                        val direction =
                            dismissState.dismissDirection ?: return@SwipeToDismiss
                        val alignment = when (direction) {
                            DismissDirection.StartToEnd -> Alignment.CenterStart
                            DismissDirection.EndToStart -> Alignment.CenterEnd
                        }
                        Box(
                            contentAlignment = alignment,
                            modifier = Modifier.fillMaxSize().background(Color.Red)
                                .padding(horizontal = 16.dp)
                        ) {
                            Icon(Icons.Default.Delete)
                        }
                    },
                    dismissContent = {
                        Card(elevation = animate(if (dismissState.dismissDirection == null) 0.dp else 4.dp)) {
                            Row(
                                modifier = Modifier.padding(
                                    vertical = 8.dp, horizontal = 16.dp
                                )
                            ) {
                                SetTextField(
                                    modifier = Modifier.weight(1f),
                                    onValueChange = {
                                        viewModel.updateRoutine {
                                            setGroups[setGroupIndex].sets[setIndex].reps =
                                                it.takeIf { it.isNotEmpty() }?.toInt()
                                        }
                                    },
                                    regexPattern = RegexPatterns.integer,
                                    valueGetter = { set.reps?.toString() }
                                )

                                SetTextField(
                                    modifier = Modifier.weight(1f),
                                    onValueChange = {
                                        viewModel.updateRoutine {
                                            setGroups[setGroupIndex].sets[setIndex].weight =
                                                it.takeIf { it.isNotEmpty() }
                                                    ?.toDouble()
                                        }
                                    },
                                    regexPattern = RegexPatterns.float,
                                    valueGetter = { set.weight?.toString() }
                                )

                                SetTextField(
                                    modifier = Modifier.weight(1f),
                                    onValueChange = {
                                        viewModel.updateRoutine {
                                            setGroups[setGroupIndex].sets[setIndex].time =
                                                it.takeIf { it.isNotEmpty() }?.toInt()
                                        }
                                    },
                                    regexPattern = RegexPatterns.time,
                                    valueGetter = { set.time?.toString() },
                                    visualTransformation = timeVisualTransformation
                                )

                                SetTextField(
                                    modifier = Modifier.weight(1f),
                                    onValueChange = {
                                        viewModel.updateRoutine {
                                            setGroups[setGroupIndex].sets[setIndex].distance =
                                                it.takeIf { it.isNotEmpty() }
                                                    ?.toDouble()
                                        }
                                    },
                                    regexPattern = RegexPatterns.float,
                                    valueGetter = { set.distance?.toString() }
                                )
                            }
                        }
                    }
                )
            }
            TextButton(
                onClick = { viewModel.addSet(setGroup.exerciseId) },
                content = {
                    Icon(Icons.Default.Add)
                    Text("Add Set")
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun RowScope.SetHeader(
    text: String
) {
    Text(
        text = text.capitalize(Locale.getDefault()),
        textAlign = TextAlign.Center,
        modifier = Modifier.weight(1f),
        maxLines = 1,
        fontWeight = FontWeight.Bold
    )
}

@ExperimentalFocus
@ExperimentalFoundationApi
@Composable
fun SetTextField(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = {},
    visualTransformation: VisualTransformation = VisualTransformation.None,
    regexPattern: Regex = Regex(""),
    valueGetter: () -> String? = { null },
) {
    var value by remember { mutableStateOf(TextFieldValue(valueGetter() ?: "")) }
    var kb: SoftwareKeyboardController? by remember { mutableStateOf(null) }
    BasicTextField(
        value = value,
        onValueChange = {
            // TODO: Keep single-char values selected
            if (it.text.matches(regexPattern) && (it.text != value.text || it.text.length <= 1)) {
                value = TextFieldValue(it.text, TextRange(it.text.length))
                onValueChange(it.text)
            }
        },
        modifier = modifier
            .padding(horizontal = 4.dp)
            .clip(shape = RoundedCornerShape(8.dp))
            .background(MaterialTheme.colors.onSurface.copy(alpha = 0.12f))
            .padding(4.dp)
            .focusObserver {
                if (!it.isFocused) value = TextFieldValue(valueGetter() ?: value.text)
            },
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        onTextInputStarted = {
            kb = it
            value = TextFieldValue(value.text, TextRange(0, value.text.length))
        },
        textStyle = AmbientTextStyle.current.copy(
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.onSurface
        ),
        cursorColor = MaterialTheme.colors.onSurface,
        onImeActionPerformed = {
            val text = valueGetter() ?: value.text
            value = TextFieldValue(text, TextRange(0, text.length))
            kb?.hideSoftwareKeyboard()
        },
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
        val withColons = "${withZeroes[0]}${withZeroes[1]}:${withZeroes[2]}${withZeroes[3]}"
        return TransformedText(
            AnnotatedString(if (text.text.isEmpty()) "" else withColons),
            offsetMap
        )
    }
}
