package com.noahjutz.splitfit.ui.workout.completed

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.noahjutz.splitfit.data.ColorTheme
import com.noahjutz.splitfit.ui.theme.SplitfitTheme

@Composable
fun WorkoutCompleted() {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {}) { Icon(Icons.Default.ArrowBack, "back") }
                },
                title = {}
            )
        }
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 48.dp, horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Workout complete!", style = typography.h4.copy(textAlign = TextAlign.Center))
                Spacer(Modifier.height(24.dp))
                Button(onClick = {}) {
                    Text("Update Routine")
                }
            }
        }
    }
}

@Composable
@Preview
fun WorkoutCompletedPreview() {
    SplitfitTheme(colors = ColorTheme.FollowSystem) {
        WorkoutCompleted()
    }
}