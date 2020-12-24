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
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.KEY_ROUTE
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.noahjutz.splitfit.R

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            MainScreenTopBar(navController)
        },
    ) {
        NavGraph(navController = navController)
    }
}

sealed class HomeTabs(val route: String, @StringRes val name: Int) {
    object Routines : HomeTabs("routines", R.string.tab_routines)
    object Exercises : HomeTabs("exercises", R.string.tab_exercises)
}

val homeTabs = listOf(
    HomeTabs.Routines,
    HomeTabs.Exercises
)

@Composable
fun MainScreenTopBar(
    navController: NavHostController,
) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.arguments?.getString(KEY_ROUTE)
    if (currentRoute in homeTabs.map { it.route }) {
        TabRow(
            selectedTabIndex = homeTabs.map { it.route }.indexOf(currentRoute).takeIf { it > 0 }
                ?: 0
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
}
