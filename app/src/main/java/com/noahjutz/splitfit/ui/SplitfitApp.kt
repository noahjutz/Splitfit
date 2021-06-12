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
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.noahjutz.splitfit.R
import com.noahjutz.splitfit.data.AppPrefs
import kotlinx.coroutines.flow.map
import org.koin.androidx.compose.get
import kotlin.time.ExperimentalTime

sealed class BottomNavItem(
    val route: String,
    @StringRes val name: Int,
    val icon: ImageVector,
) {
    object Routines :
        BottomNavItem(Screen.routineList.name, R.string.tab_routines, Icons.Default.ViewAgenda)

    object Exercises :
        BottomNavItem(Screen.exerciseList.name, R.string.tab_exercises, Icons.Default.FitnessCenter)

    object Workouts :
        BottomNavItem(Screen.insights.name, R.string.tab_insights, Icons.Default.Insights)

    object Settings :
        BottomNavItem(Screen.settings.name, R.string.tab_settings, Icons.Default.Settings)
}

val bottomNavItems = listOf(
    BottomNavItem.Routines,
    BottomNavItem.Exercises,
    BottomNavItem.Workouts,
    BottomNavItem.Settings,
)

@ExperimentalTime
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun SplitfitApp(
    preferences: DataStore<Preferences> = get(),
) {
    val navController = rememberNavController()

    val currentWorkoutId by preferences.data
        .map { it[AppPrefs.CurrentWorkout.key] }
        .collectAsState(initial = -1)
    val isWorkoutInProgress = currentWorkoutId?.let { it >= 0 } ?: false
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val isCurrentDestinationHomeTab =
        navBackStackEntry?.destination?.route in bottomNavItems.map { it.route }
    val showWorkoutBottomSheet = isWorkoutInProgress && isCurrentDestinationHomeTab

    val navToWorkoutScreen =
        { navController.navigate("${Screen.workoutInProgress}?workoutId=$currentWorkoutId") }

    Scaffold(
        bottomBar = {
            val showBottomNavLabels by preferences.data
                .map { it[AppPrefs.ShowBottomNavLabels.key] == true }
                .collectAsState(initial = true)
            Column {
                if (showWorkoutBottomSheet) WorkoutBottomSheet(navToWorkoutScreen)
                if (isCurrentDestinationHomeTab) HomeBottomBar(
                    navController = navController,
                    showLabels = showBottomNavLabels
                )
            }
        }
    ) { paddingValues ->
        Box(Modifier.padding(paddingValues)) {
            NavGraph(navController = navController)
        }
    }
}

@Composable
private fun HomeBottomBar(
    navController: NavController,
    showLabels: Boolean,
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    BottomNavigation {
        for (screen in bottomNavItems) {
            BottomNavigationItem(
                icon = { Icon(screen.icon, null) },
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                label = (@Composable { Text(stringResource(screen.name)) }).takeIf { showLabels },
                selected = screen.route == currentRoute,
            )
        }
    }
}

@Composable
private fun WorkoutBottomSheet(navToWorkoutScreen: () -> Unit) {
    BottomAppBar(
        Modifier.clickable(onClick = navToWorkoutScreen),
        elevation = 4.dp,
        backgroundColor = colors.surface,
    ) {
        ProvideTextStyle(value = MaterialTheme.typography.h6) {
            Text(
                modifier = Modifier.padding(horizontal = 12.dp),
                text = "Workout in progress"
            )
        }
        Spacer(Modifier.weight(1f))
        IconButton(onClick = navToWorkoutScreen) {
            Icon(Icons.Default.ExpandLess, null)
        }
    }
}
