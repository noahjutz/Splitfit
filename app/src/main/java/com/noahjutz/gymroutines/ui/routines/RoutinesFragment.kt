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

package com.noahjutz.gymroutines.ui.routines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.noahjutz.gymroutines.R
import com.noahjutz.gymroutines.data.domain.Routine
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoutinesFragment : Fragment() {

    private val viewModel: RoutinesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setContent {
            MaterialTheme(colors = if (isSystemInDarkTheme()) darkColors() else lightColors()) {
                RoutinesScreen(::addEditRoutine, viewModel)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().apply {
            title = "Routines"
            findViewById<BottomNavigationView>(R.id.bottom_nav)?.visibility = VISIBLE
        }
    }

    private fun addEditRoutine(routineId: Int) {
        val routineIdToEdit =
            if (routineId < 0) viewModel.addRoutine().toInt()
            else routineId

        findNavController().navigate(RoutinesFragmentDirections.addRoutine(routineIdToEdit))
    }
}

@Composable
fun RoutinesScreen(
    addEditRoutine: (Int) -> Unit,
    viewModel: RoutinesViewModel
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { addEditRoutine(-1) },
                icon = { Icon(Icons.Default.Add) }
            )
        }
    ) {
        var showDialog by remember { mutableStateOf(false) }
        var toDelete by remember { mutableStateOf<Routine?>(null) }
        val routines by viewModel.routines.observeAsState()
        LazyColumnFor(items = routines ?: emptyList()) { routine ->
            ListItem(
                text = {
                    Text(routine.name.takeIf { it.isNotBlank() } ?: "Unnamed")
                },
                modifier = Modifier.clickable(
                    onClick = {
                        addEditRoutine(routine.routineId)
                    },
                    onLongClick = {
                        toDelete = routine
                        showDialog = true
                    }
                )
            )
        }
        if (showDialog) {
            AlertDialog(
                title = { Text("Delete ${toDelete?.name?.takeIf { it.isNotBlank() } ?: "Unnamed"}?") },
                confirmButton = {
                    Button(
                        onClick = {
                            toDelete?.routineId?.let { viewModel.deleteRoutine(it) }
                            showDialog = false
                        },
                        content = { Text("Yes") }
                    )
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDialog = false },
                        content = { Text("Cancel") }
                    )
                },
                onDismissRequest = { showDialog = false }
            )
        }
    }
}
