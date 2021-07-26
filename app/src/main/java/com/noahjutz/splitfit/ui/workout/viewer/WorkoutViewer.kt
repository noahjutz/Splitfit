package com.noahjutz.splitfit.ui.workout.viewer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.noahjutz.splitfit.data.domain.Workout
import com.noahjutz.splitfit.data.domain.duration
import com.noahjutz.splitfit.ui.components.TopBar
import com.noahjutz.splitfit.util.iso8601
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun WorkoutViewer(
    workoutId: Int,
    viewModel: WorkoutViewerViewModel = getViewModel { parametersOf(workoutId) },
    popBackStack: () -> Unit,
) {
    val state by viewModel.state.collectAsState()
    when (state) {
        is WorkoutViewerViewModel.State.Found -> {
            val found = state as? WorkoutViewerViewModel.State.Found
            found?.let {
                WorkoutViewer(popBackStack, found.workout)
            }
        }
        WorkoutViewerViewModel.State.Error -> TODO()
        WorkoutViewerViewModel.State.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun WorkoutViewer(
    popBackStack: () -> Unit,
    workout: Workout
) {
    Scaffold(
        topBar = {
            TopBar(
                title = workout.name,
                navigationIcon = {
                    IconButton(onClick = popBackStack) { Icon(Icons.Default.ArrowBack, null) }
                }
            )
        }
    ) {
        LazyColumn {
            item {
                Text(
                    text = workout.duration.iso8601(),
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
                Divider()
            }

            items(workout.setGroups) {
                Text(it.sets.toString()) // TODO use SetGroupCards
            }
        }
    }
}
