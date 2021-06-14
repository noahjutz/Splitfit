package com.noahjutz.splitfit.ui.settings.about

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.noahjutz.splitfit.R
import com.noahjutz.splitfit.ui.LocalActivity
import com.noahjutz.splitfit.util.openUrl

enum class Licenses(val fullName: String) {
    APACHE2("Apache License 2.0"),
    EPL1("Eclipse Public License 1.0")
}

data class Dependency(
    val name: String,
    val license: Licenses,
    val url: String,
)

val dependencies = listOf(
    Dependency("Koin", Licenses.APACHE2, "https://github.com/InsertKoinIO/koin"),
    Dependency("Android Jetpack", Licenses.APACHE2, "https://developer.android.com/jetpack"),
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
    Dependency("ProcessPhoenix", Licenses.APACHE2, "https://github.com/JakeWharton/ProcessPhoenix"),
    Dependency("Junit 4", Licenses.EPL1, "https://github.com/junit-team/junit4"),
    Dependency("AssertJ", Licenses.APACHE2, "https://github.com/assertj/assertj-core"),
    Dependency("MockK", Licenses.APACHE2, "https://github.com/mockk/mockk")
)

@ExperimentalMaterialApi
@Composable
fun LicensesList(
    popBackStack: () -> Unit,
) {
    val mainActivity = LocalActivity.current
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = popBackStack) { Icon(Icons.Default.ArrowBack, "Back") }
                },
                title = { Text("Licenses") },
            )
        }
    ) {
        LazyColumn {
            item {
                Card(Modifier.padding(16.dp)) {
                    Box(Modifier.padding(8.dp)) {
                        Text(stringResource(R.string.license_notice), style = typography.body2)
                    }
                }
                Text(
                    "Open-Source Dependencies",
                    style = typography.h5,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            items(dependencies) { dependency ->
                ListItem(
                    modifier = Modifier.clickable { mainActivity.openUrl(dependency.url) },
                    text = { Text(dependency.name) },
                    secondaryText = { Text(dependency.license.fullName) }
                )
            }
        }
    }
}