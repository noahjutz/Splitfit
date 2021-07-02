package com.noahjutz.splitfit.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.noahjutz.splitfit.data.ColorTheme
import com.noahjutz.splitfit.ui.LocalColorTheme

@Composable
fun NormalDialog(
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit,
) {
    Dialog(onDismissRequest) {
        val theme = LocalColorTheme.current
        Card(
            modifier = Modifier.padding(24.dp),
            elevation = if (theme == ColorTheme.Black) 2.dp else 0.dp,
            content = content
        )
    }
}

@Composable
fun NormalDialog(
    onDismissRequest: () -> Unit,
    title: @Composable () -> Unit,
    text: (@Composable () -> Unit)? = null,
    confirmButton: (@Composable () -> Unit)? = null,
    dismissButton: (@Composable () -> Unit)? = null,
) {
    NormalDialog(onDismissRequest) {
        Box(Modifier.verticalScroll(rememberScrollState())) {
            Column(Modifier.padding(16.dp)) {
                ProvideTextStyle(
                    value = typography.body1.copy(fontWeight = FontWeight.Black),
                    content = title
                )
                if (text != null) {
                    Spacer(Modifier.height(8.dp))
                    ProvideTextStyle(
                        value = typography.body1.copy(color = colors.onSurface.copy(alpha = 0.7f)),
                        content = text
                    )
                }
                Spacer(Modifier.height(16.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    if (dismissButton != null) dismissButton()
                    Spacer(Modifier.width(8.dp))
                    if (confirmButton != null) confirmButton()
                }
            }
        }
    }
}