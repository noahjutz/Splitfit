package com.noahjutz.splitfit.ui.workout.viewer

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.noahjutz.splitfit.data.domain.Workout
import com.noahjutz.splitfit.data.domain.duration
import com.noahjutz.splitfit.ui.components.TopBar
import com.noahjutz.splitfit.util.pretty
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import java.util.*
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun WorkoutViewer(
    workoutId: Int,
    viewModel: WorkoutViewerViewModel = getViewModel { parametersOf(workoutId) },
    popBackStack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopBar(
                title = "View Workout",
                navigationIcon = {
                    IconButton(onClick = popBackStack) { Icon(Icons.Default.ArrowBack, null) }
                }
            )
        }
    ) {
        val workout by viewModel.workout.collectAsState()
        if (workout != null) WorkoutViewerContent(workout!!)
    }
}

@ExperimentalTime
@Composable
fun WorkoutViewerContent(workout: Workout) {
    LazyColumn {
        item {
            Spacer(Modifier.height(16.dp))
            Text(
                text = workout.name,
                modifier = Modifier.padding(horizontal = 16.dp),
                style = typography.h4,
            )
            Text(
                text = workout.endTime.pretty(),
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Text(
                text = workout.duration.pretty(),
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Spacer(Modifier.height(16.dp))
            Divider()
            Spacer(Modifier.height(16.dp))
        }

        items(workout.setGroups) {
            // TODO show setGroups using SetGroupCards like in WorkoutInProgress
        }
    }
}
