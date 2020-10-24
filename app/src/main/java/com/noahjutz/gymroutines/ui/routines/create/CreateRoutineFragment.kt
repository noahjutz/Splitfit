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
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.data.domain.Set
import com.noahjutz.gymroutines.ui.routines.create.pick.SharedExerciseViewModel
import dagger.hilt.android.AndroidEntryPoint

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
                    var nameFieldValue by remember { mutableStateOf(TextFieldValue(presenter.initialName)) }
                    BaseTextField(
                        value = nameFieldValue,
                        onValueChange = {
                            nameFieldValue = it
                            editor.updateRoutine { this.name = it.text }
                        },
                    )
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
    LazyColumnFor(items = sets, modifier = Modifier.padding(16.dp)) { set ->
        SetCard(Modifier.padding(bottom = 16.dp), set)
    }
}

@ExperimentalFoundationApi
@Composable
fun SetCard(
    modifier: Modifier,
    set: Set
) {
    val presenter = viewModel<CreateRoutinePresenter>()
    Card(modifier.fillMaxWidth()) {
        Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = presenter.getExerciseName(set.exerciseId),
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth(0.3f)
            )
            Row(
                horizontalArrangement = Arrangement.End
            ) {
                for (i in (0..3)) {
                    var value by remember { mutableStateOf(TextFieldValue("1.2")) } // TODO real value
                    BaseTextField(
                        value = value,
                        onValueChange = {value = it},
                        modifier = Modifier.width(64.dp)
                            .clickable(onClick = {}),
                    )
                }
            }
        }
    }
}