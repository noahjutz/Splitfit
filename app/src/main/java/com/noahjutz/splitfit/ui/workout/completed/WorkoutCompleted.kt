package com.noahjutz.splitfit.ui.workout.completed

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.noahjutz.splitfit.data.ColorTheme
import com.noahjutz.splitfit.ui.components.TopBar
import com.noahjutz.splitfit.ui.theme.SplitfitTheme

@ExperimentalMaterialApi
@Composable
fun WorkoutCompleted(
    routineId: Int,
    popBackStack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopBar(
                navigationIcon = {
                    IconButton(onClick = popBackStack) { Icon(Icons.Default.ArrowBack, "back") }
                },
                title = {}
            )
        }
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 48.dp, horizontal = 24.dp),
            ) {
                Text("Workout complete!", style = typography.h4)
                if (routineId >= 0) {
                    Spacer(Modifier.height(24.dp))
                    Button(onClick = { /*TODO*/ }) {
                        Text("Update Routine")
                    }
                }
            }
            TextButton(
                onClick = popBackStack,
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(24.dp)
            ) {
                Text("Close")
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
@Preview
fun WorkoutCompletedPreview() {
    SplitfitTheme(colors = ColorTheme.Black) {
        WorkoutCompleted(
            popBackStack = {},
            routineId = -1
        )
    }
}