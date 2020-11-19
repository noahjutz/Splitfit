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
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animate
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
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
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
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
import java.util.*

@ExperimentalFocus
@ExperimentalFoundationApi
@AndroidEntryPoint
class CreateRoutineFragment : Fragment() {

    private val sharedExerciseViewModel: SharedExerciseViewModel by activityViewModels()
    private val viewModel: CreateRoutineEditor by viewModels()

    @ExperimentalMaterialApi
    @ExperimentalAnimationApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setContent {
            MaterialTheme(colors = if (isSystemInDarkTheme()) darkColors() else lightColors()) {
                CreateRoutineScreen(
                    onAddExercise = ::addExercise,
                    popBackStack = ::popBackStack,
                    presenter = viewModel(),
                    editor = viewModel()
                )
            }
        }
    }

    private fun popBackStack() {
        findNavController().popBackStack()
    }

    fun addExercise() {
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
            for (e in exercises) viewModel.addSet(e.exerciseId)
            if (exercises.isNotEmpty()) sharedExerciseViewModel.clearExercises()
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalFocus
@ExperimentalFoundationApi
@Composable
fun CreateRoutineScreen(
    onAddExercise: () -> Unit,
    popBackStack: () -> Unit,
    presenter: CreateRoutinePresenter,
    editor: CreateRoutineEditor
) {
    val sets by presenter.sets.observeAsState()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddExercise,
                icon = { Icon(Icons.Default.Add) },
                modifier = Modifier.testTag("addExerciseFab")
            )
        },
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = popBackStack,
                        icon = { Icon(Icons.Default.ArrowBack) },
                        modifier = Modifier.testTag("backButton")
                    )
                },
                title = {
                    Box {
                        var nameFieldValue by remember { mutableStateOf(TextFieldValue(presenter.initialName)) }
                        var focusState by remember { mutableStateOf(false) }
                        BasicTextField(
                            value = nameFieldValue,
                            onValueChange = {
                                nameFieldValue = it
                                editor.updateRoutine { this.name = it.text }
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
        LazyColumnFor(
            items = sets?.let { it.groupBy { it.exerciseId }.values.toList() } ?: emptyList(),
            modifier = Modifier.fillMaxHeight()
        ) { setGroup ->
            SetGroupCard(setGroup, editor, presenter)
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalFocus
@ExperimentalFoundationApi
@Composable
fun SetGroupCard(
    setGroup: List<Set>,
    editor: CreateRoutineEditor,
    presenter: CreateRoutinePresenter,
) {
    val offsetPosition = remember { mutableStateOf(0f) }
    Card(
        elevation = animate(if (offsetPosition.value == 0f) 0.dp else 4.dp),
        modifier = Modifier.fillMaxWidth()
            .offsetPx(y = offsetPosition)
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth()
                .clickable {}
                .longPressDragGestureFilter(
                    longPressDragObserver = object : LongPressDragObserver {
                        override fun onDrag(dragDistance: Offset): Offset {
                            super.onDrag(dragDistance)
                            offsetPosition.value += dragDistance.y
                            return dragDistance
                        }

                        override fun onStop(velocity: Offset) {
                            super.onStop(velocity)
                            offsetPosition.value = 0f
                        }

                        override fun onDragStart() {
                            super.onDragStart()
                        }
                    })
            ) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = presenter.getExerciseName(setGroup.first().exerciseId),
                    fontSize = 20.sp,
                )
            }
            Row(modifier = Modifier.padding(bottom = 16.dp, start = 16.dp, end = 16.dp)) {
                SetHeader("reps")
                SetHeader("weight")
                SetHeader("time")
                SetHeader("distance")
            }
            setGroup.forEachIndexed { i, set ->
                val dismissState = rememberDismissState()

                onCommit(dismissState.value) {
                    if (dismissState.value != DismissValue.Default) {
                        editor.updateRoutine { sets.removeAt(i) }
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
                            alignment = alignment,
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
                                    onValueChange = {
                                        editor.updateRoutine {
                                            sets[i].reps =
                                                it.takeIf { it.isNotEmpty() }?.toInt()
                                        }
                                    },
                                    regexPattern = RegexPatterns.integer,
                                    valueGetter = { set.reps?.toString() }
                                )

                                SetTextField(
                                    onValueChange = {
                                        editor.updateRoutine {
                                            sets[i].weight =
                                                it.takeIf { it.isNotEmpty() }
                                                    ?.toDouble()
                                        }
                                    },
                                    regexPattern = RegexPatterns.float,
                                    valueGetter = { set.weight?.toString() }
                                )

                                SetTextField(
                                    onValueChange = {
                                        editor.updateRoutine {
                                            sets[i].time =
                                                it.takeIf { it.isNotEmpty() }?.toInt()
                                        }
                                    },
                                    regexPattern = RegexPatterns.time,
                                    valueGetter = { set.time?.toString() },
                                    visualTransformation = timeVisualTransformation
                                )

                                SetTextField(
                                    onValueChange = {
                                        editor.updateRoutine {
                                            sets[i].distance =
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
                onClick = { editor.addSet(setGroup[0].exerciseId) },
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
fun RowScope.SetTextField(
    onValueChange: (String) -> Unit = {},
    modifier: Modifier = Modifier,
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
        modifier = modifier.weight(1f).fillMaxWidth().padding(horizontal = 4.dp)
            .clip(shape = RoundedCornerShape(8.dp))
            .background(MaterialTheme.colors.onSurface.copy(alpha = 0.12f)).padding(4.dp)
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
