/*
 * Splitfit
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

package com.noahjutz.splitfit.ui.settings.about

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.noahjutz.splitfit.BuildConfig
import com.noahjutz.splitfit.R
import com.noahjutz.splitfit.ui.LocalActivity
import com.noahjutz.splitfit.ui.MainActivity
import com.noahjutz.splitfit.ui.settings.about.AboutSplitfitViewModel.Urls.contributing
import com.noahjutz.splitfit.ui.settings.about.AboutSplitfitViewModel.Urls.donateLiberapay
import com.noahjutz.splitfit.ui.settings.about.AboutSplitfitViewModel.Urls.googlePlay
import com.noahjutz.splitfit.ui.settings.about.AboutSplitfitViewModel.Urls.sourceCode
import org.koin.androidx.compose.getViewModel

private fun MainActivity.openUrl(url: String) {
    startActivity(Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(url) })
}

@ExperimentalMaterialApi
@Composable
fun AboutSplitfit(
    popBackStack: () -> Unit,
    viewModel: AboutSplitfitViewModel = getViewModel(),
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About") },
                navigationIcon = {
                    IconButton(onClick = popBackStack) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                }
            )
        }
    ) {
        var showLicenses by remember { mutableStateOf(false) }
        if (showLicenses) LicensesDialog(
            onDismiss = { showLicenses = false },
            dependencies = viewModel.dependencies
        )
        val mainActivity = LocalActivity.current
        LazyColumn {
            item {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color.Black,
                        elevation = 4.dp,
                    ) {
                        Image(
                            modifier = Modifier
                                .padding(4.dp)
                                .size(48.dp),
                            imageVector = ImageVector.vectorResource(R.drawable.ic_splitfit),
                            contentDescription = null,
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Text("Splitfit", style = typography.h4)
                }

                ListItem(
                    text = { Text("Version") },
                    secondaryText = { Text(BuildConfig.VERSION_NAME) },
                    icon = { Icon(Icons.Default.Update, null) },
                )
                ListItem(
                    modifier = Modifier.clickable { showLicenses = true },
                    text = { Text("Licenses") },
                    icon = { Icon(Icons.Default.ListAlt, null) },
                )
                ListItem(
                    modifier = Modifier.clickable { mainActivity.openUrl(sourceCode) },
                    text = { Text("Source Code") },
                    secondaryText = { Text("On GitHub") },
                    icon = { Icon(Icons.Default.Code, null) },
                    trailing = { Icon(Icons.Default.Launch, null) },
                )

                Divider()

                ListItem(
                    modifier = Modifier.clickable { mainActivity.openUrl(contributing) },
                    text = { Text("Contributing") },
                    secondaryText = { Text("Find out how to contribute to Splitfit.") },
                    icon = { Icon(Icons.Default.Forum, null) },
                    trailing = { Icon(Icons.Default.Launch, null) },
                )
                if (BuildConfig.FLAVOR == "googleplay") ListItem(
                    modifier = Modifier.clickable { mainActivity.openUrl(googlePlay) },
                    text = { Text("Rate App") },
                    secondaryText = { Text("On Google Play") },
                    icon = { Icon(Icons.Default.RateReview, null) },
                    trailing = { Icon(Icons.Default.Launch, null) },
                )
                if (BuildConfig.FLAVOR != "googleplay") ListItem(
                    modifier = Modifier.clickable { mainActivity.openUrl(donateLiberapay) },
                    text = { Text("Donate") },
                    secondaryText = { Text("On Liberapay") },
                    icon = { Icon(Icons.Default.CardGiftcard, null) },
                    trailing = { Icon(Icons.Default.Launch, null) },
                )

                Divider()

                ListItem(
                    text = { Text("Author") },
                    secondaryText = { Text("Noah Jutz") },
                    icon = { Icon(Icons.Default.Face, null) }
                )
                ListItem(
                    text = { Text("Contact") },
                    secondaryText = { Text("noahjutz@tutanota.de") },
                    icon = { Icon(Icons.Default.ContactMail, null) },
                )
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun LicensesDialog(
    onDismiss: () -> Unit,
    dependencies: List<Dependency>,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        dismissButton = { TextButton(onClick = onDismiss) { Text("Close") } },
        title = { Text("Licenses") },
        text = {
            LazyColumn {
                item {
                    var expanded by remember { mutableStateOf(false) }
                    Card {
                        ListItem(
                            modifier = Modifier.clickable { expanded = !expanded },
                            text = { Text("Splitfit is licensed under the GNU GPLv3.") },
                            secondaryText = (@Composable {
                                Text(stringResource(R.string.license_notice))
                            }).takeIf { expanded },
                            trailing = {
                                Icon(
                                    if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                    null
                                )
                            }
                        )
                    }
                }

                items(dependencies) { dependency ->
                    val mainActivity = LocalActivity.current
                    ListItem(
                        modifier = Modifier.clickable { mainActivity.openUrl(dependency.url) },
                        text = { Text(dependency.name) },
                        secondaryText = { Text(dependency.license.fullName) },
                    )
                }
            }
        },
    )
}
