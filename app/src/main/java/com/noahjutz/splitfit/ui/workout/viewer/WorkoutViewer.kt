package com.noahjutz.splitfit.ui.workout.viewer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.noahjutz.splitfit.ui.components.TopBar
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun WorkoutViewer(
    workoutId: Int,
    viewModel: WorkoutViewerViewModel = getViewModel { parametersOf(workoutId) },
    popBackStack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopBar(
                title = viewModel.workout.name,
                navigationIcon = {
                    IconButton(onClick = popBackStack) { Icon(Icons.Default.ArrowBack, null) }
                }
            )
        }
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Not yet implemented")
        }
    }
}
