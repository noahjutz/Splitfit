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

package com.noahjutz.splitfit.ui

import androidx.annotation.StringRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.navigation.NavHostController
import androidx.navigation.compose.KEY_ROUTE
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.noahjutz.splitfit.R
import com.noahjutz.splitfit.util.DatastoreKeys
import kotlinx.coroutines.flow.map
import org.koin.androidx.compose.get

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun SplitfitApp(
    preferences: DataStore<Preferences> = get(),
) {
    val navController = rememberNavController()

    val currentWorkoutId by preferences.data
        .map { it[DatastoreKeys.currentWorkout] }
        .collectAsState(initial = -1)
    val isWorkoutInProgress = currentWorkoutId?.let { it >= 0 } ?: false
    val isCurrentDestinationHomeTab = navController.currentBackStackEntryAsState()
        .value?.arguments?.getString(KEY_ROUTE) in homeTabs.map { it.route }
    val showWorkoutBottomSheet = isWorkoutInProgress && isCurrentDestinationHomeTab

    val navToWorkoutScreen = { navController.navigate("createWorkout?workoutId=$currentWorkoutId") }

    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            if (isCurrentDestinationHomeTab) {
                Column {
                    HomeTopBar { scaffoldState.drawerState.open() }
                    HomeTabRow(navController)
                }
            }
        },
        bottomBar = {
            if (showWorkoutBottomSheet) WorkoutBottomSheet(navToWorkoutScreen)
        },
        drawerContent = {
            AppDrawer(
                navToAbout = { navController.navigate("about") },
                navToSettings = { navController.navigate("settings") },
                drawerState = scaffoldState.drawerState,
            )
        }
    ) {
        val bottomPadding = if (showWorkoutBottomSheet) 56.dp else 0.dp
        Box(Modifier.padding(bottom = bottomPadding)) {
            NavGraph(navController = navController)
        }
    }
}

private sealed class HomeTabs(val route: String, @StringRes val name: Int) {
    object Routines : HomeTabs("routines", R.string.tab_routines)
    object Exercises : HomeTabs("exercises", R.string.tab_exercises)
    object Workouts : HomeTabs("workouts", R.string.tab_workouts)
}

private val homeTabs = listOf(
    HomeTabs.Routines,
    HomeTabs.Exercises,
    HomeTabs.Workouts
)

@Composable
private fun HomeTabRow(
    navController: NavHostController,
) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.arguments?.getString(KEY_ROUTE)
    TabRow(
        selectedTabIndex = homeTabs.map { it.route }.indexOf(currentRoute).takeIf { it > 0 } ?: 0
    ) {
        for (screen in homeTabs)
            Tab(
                selected = screen.route == currentRoute,
                onClick = {
                    navController.popBackStack(navController.graph.startDestination, false)
                    if (currentRoute != screen.route) navController.navigate(screen.route)
                }
            ) {
                Text(
                    stringResource(screen.name),
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
    }
}

@Composable
private fun HomeTopBar(openDrawer: () -> Unit) {
    TopAppBar(
        title = { Text(stringResource(R.string.app_name)) },
        navigationIcon = {
            IconButton(onClick = openDrawer) {
                Icon(Icons.Default.Menu)
            }
        }
    )
}

@Composable
private fun WorkoutBottomSheet(navToWorkoutScreen: () -> Unit) {
    BottomAppBar(
        Modifier.clickable(onClick = navToWorkoutScreen)
    ) {
        ProvideTextStyle(value = MaterialTheme.typography.h6) {
            Text(
                modifier = Modifier.padding(horizontal = 12.dp),
                text = "Workout in progress"
            )
        }
        Spacer(Modifier.weight(1f))
        IconButton(onClick = navToWorkoutScreen) {
            Icon(Icons.Default.ExpandLess)
        }
    }
}