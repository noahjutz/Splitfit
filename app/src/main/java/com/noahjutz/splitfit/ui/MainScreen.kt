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
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.noahjutz.splitfit.R
import com.noahjutz.splitfit.data.Repository
import com.noahjutz.splitfit.ui.exercises.ExercisesScreen
import com.noahjutz.splitfit.ui.exercises.ExercisesViewModel
import com.noahjutz.splitfit.ui.exercises.create.CreateExerciseScreen
import com.noahjutz.splitfit.ui.exercises.create.CreateExerciseViewModel
import com.noahjutz.splitfit.ui.routines.RoutinesScreen
import com.noahjutz.splitfit.ui.routines.RoutinesViewModel
import com.noahjutz.splitfit.ui.routines.create.CreateRoutineScreen
import com.noahjutz.splitfit.ui.routines.create.CreateRoutineViewModel
import com.noahjutz.splitfit.ui.routines.create.pick.PickExerciseScreen
import com.noahjutz.splitfit.ui.routines.create.pick.PickExerciseViewModel
import com.noahjutz.splitfit.ui.routines.create.pick.SharedExerciseViewModel
import org.koin.java.KoinJavaComponent

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

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun NavGraph(
    navController: NavHostController,
) {
    NavHost(navController, startDestination = "routines") {
        val sharedExerciseViewModel = KoinJavaComponent.get(SharedExerciseViewModel::class.java)
        composable("routines") {
            RoutinesScreen(
                addEditRoutine = { routineId ->
                    navController.navigate("createRoutine/$routineId")
                },
                viewModel = KoinJavaComponent.get(RoutinesViewModel::class.java),
            )
        }
        composable(
            route = "createRoutine/{routineId}",
            arguments = listOf(navArgument("routineId") { type = NavType.IntType })
        ) { backStackEntry ->
            val routineId: Int = backStackEntry.arguments?.getInt("routineId") ?: -1
            CreateRoutineScreen(
                onAddExercise = { navController.navigate("pickExercise") },
                popBackStack = { navController.popBackStack() },
                viewModel = CreateRoutineViewModel(
                    KoinJavaComponent.get(Repository::class.java),
                    routineId
                ),
                sharedExerciseVM = sharedExerciseViewModel,
            )
        }
        composable("pickExercise") {
            PickExerciseScreen(
                viewModel = KoinJavaComponent.get(PickExerciseViewModel::class.java),
                sharedExerciseViewModel = sharedExerciseViewModel,
                popBackStack = { navController.popBackStack() }
            )
        }
        composable("exercises") {
            ExercisesScreen(
                addEditExercise = { exerciseId ->
                    navController.navigate("createExercise/$exerciseId")
                },
                viewModel = KoinJavaComponent.get(ExercisesViewModel::class.java)
            )
        }
        composable(
            route = "createExercise/{exerciseId}",
            arguments = listOf(navArgument("exerciseId") { type = NavType.IntType })
        ) { backStackEntry ->
            val exerciseId = backStackEntry.arguments?.getInt("exerciseId") ?: -1
            val createExerciseVM = CreateExerciseViewModel(
                KoinJavaComponent.get(Repository::class.java),
                exerciseId
            )
            CreateExerciseScreen(
                popBackStack = { navController.popBackStack() },
                viewModel = createExerciseVM
            )
        }
    }
}

sealed class Screen(val route: String, @StringRes val name: Int) {
    object Routines : Screen("routines", R.string.tab_routines)
    object Exercises : Screen("exercises", R.string.tab_exercises)
}

@Composable
fun MainScreenTopBar(
    navController: NavHostController,
) {
    val screens = listOf(
        Screen.Routines,
        Screen.Exercises
    )
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.arguments?.getString(KEY_ROUTE)
    if (currentRoute in screens.map { it.route }) {
        TabRow(
            selectedTabIndex = screens.map { it.route }.indexOf(currentRoute).takeIf { it > 0 }
                ?: 0
        ) {
            for (screen in screens)
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
