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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.noahjutz.splitfit.BuildConfig
import com.noahjutz.splitfit.R
import com.noahjutz.splitfit.util.IntentsForCompose.openUrl
import com.noahjutz.splitfit.util.Urls

@Composable
fun AboutSplitfit(
    popBackStack: () -> Unit,
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
        if (showLicenses) LicensesDialog(onDismiss = { showLicenses = false })
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
                                .preferredSize(48.dp),
                            imageVector = vectorResource(R.drawable.ic_splitfit),
                            contentDescription = null,
                        )
                    }
                    Spacer(Modifier.preferredWidth(12.dp))
                    ProvideTextStyle(value = typography.h4) {
                        Text("Splitfit")
                    }
                }

                ListItem(
                    modifier = Modifier.clickable { showLicenses = true },
                    text = { Text("Licenses") },
                    icon = { Icon(Icons.Default.ListAlt, null) },
                )
                ListItem(
                    modifier = Modifier.clickable { openUrl(Urls.sourceCode) },
                    text = { Text("Source Code") },
                    secondaryText = { Text("On GitHub") },
                    icon = { Icon(Icons.Default.Code, null) },
                    trailing = { Icon(Icons.Default.Launch, null) },
                )

                Divider()

                ListItem(
                    modifier = Modifier.clickable { openUrl(Urls.contributing) },
                    text = { Text("Contributing") },
                    secondaryText = { Text("Find out how to contribute to Splitfit.") },
                    icon = { Icon(Icons.Default.Forum, null) },
                    trailing = { Icon(Icons.Default.Launch, null) },
                )
                if (BuildConfig.FLAVOR == "googleplay") ListItem(
                    modifier = Modifier.clickable { openUrl(Urls.googlePlay) },
                    text = { Text("Rate App") },
                    secondaryText = { Text("On Google Play") },
                    icon = { Icon(Icons.Default.RateReview, null) },
                    trailing = { Icon(Icons.Default.Launch, null) },
                )
                if (BuildConfig.FLAVOR != "googleplay") ListItem(
                    modifier = Modifier.clickable { openUrl(Urls.donateLiberapay) },
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

enum class Licenses(val fullName: String) {
    APACHE2("Apache License 2.0"),
    EPL1("Eclipse Public License 1.0")
}

data class Dependency(
    val name: String,
    val license: Licenses,
    val url: String,
)

private val dependencies = listOf(
    Dependency(
        "Koin",
        Licenses.APACHE2,
        "https://github.com/InsertKoinIO/koin"
    ),
    Dependency(
        "Android Jetpack",
        Licenses.APACHE2,
        "https://developer.android.com/jetpack"
    ),
    Dependency(
        "kotlinx.coroutines",
        Licenses.APACHE2,
        "https://github.com/Kotlin/kotlinx.coroutines"
    ),
    Dependency(
        "kotlinx.serialization",
        Licenses.APACHE2,
        "https://github.com/Kotlin/kotlinx.serialization"
    ),
    Dependency(
        "ProcessPhoenix",
        Licenses.APACHE2,
        "https://github.com/JakeWharton/ProcessPhoenix"
    ),
    Dependency(
        "Junit 4",
        Licenses.EPL1,
        "https://github.com/junit-team/junit4"
    ),
    Dependency(
        "AssertJ",
        Licenses.APACHE2,
        "https://github.com/assertj/assertj-core"
    ),
    Dependency(
        "MockK",
        Licenses.APACHE2,
        "https://github.com/mockk/mockk"
    )
)

@Composable
private fun LicensesDialog(
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        dismissButton = { TextButton(onClick = onDismiss) { Text("Close") } },
        title = { Text("Licenses") },
        text = {
            LazyColumn {
                items(dependencies) { dependency ->
                    ListItem(
                        modifier = Modifier.clickable { openUrl(dependency.url) },
                        text = { Text(dependency.name) },
                        secondaryText = { Text(dependency.license.fullName) },
                    )
                }
            }
        },
    )
}