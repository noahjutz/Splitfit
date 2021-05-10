package com.noahjutz.splitfit.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun NothingHereYet(
    message: String = "",
    title: String = "Nothing here yet...",
    icon: ImageVector = Icons.Default.Lightbulb,
) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(48.dp), contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, null, modifier = Modifier.size(48.dp))
            Spacer(Modifier.height(8.dp))
            Text(title, style = typography.h5, textAlign = TextAlign.Center)
            Spacer(Modifier.height(8.dp))
            Text(message, style = typography.body2, textAlign = TextAlign.Center)
        }
    }
}