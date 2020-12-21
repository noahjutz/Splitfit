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

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.noahjutz.splitfit.data.Repository
import com.noahjutz.splitfit.ui.exercises.ExercisesScreen
import com.noahjutz.splitfit.ui.exercises.ExercisesViewModel
import com.noahjutz.splitfit.ui.exercises.create.CreateExerciseScreen
import com.noahjutz.splitfit.ui.exercises.create.CreateExerciseViewModel
import com.noahjutz.splitfit.ui.routines.RoutinesScreen
import com.noahjutz.splitfit.ui.routines.RoutinesViewModel
import com.noahjutz.splitfit.ui.routines.create.CreateRoutineController
import com.noahjutz.splitfit.ui.routines.create.CreateRoutineScreen
import com.noahjutz.splitfit.ui.routines.create.pick.PickExerciseScreen
import com.noahjutz.splitfit.ui.routines.create.pick.PickExerciseViewModel
import com.noahjutz.splitfit.ui.routines.create.pick.SharedExerciseViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.koin.android.ext.android.inject
import org.koin.java.KoinJavaComponent.get

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val sharedExerciseVM: SharedExerciseViewModel by inject()

    @ExperimentalFoundationApi
    @ExperimentalMaterialApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colors = if (isSystemInDarkTheme()) darkColors() else lightColors()) {
                MainScreen()
            }
        }
    }
}

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
        MainScreenContent(navController = navController)
    }
}

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun MainScreenContent(
    navController: NavHostController,
) {
    val sharedExerciseViewModel = get(SharedExerciseViewModel::class.java)
    NavHost(navController, startDestination = "routines") {
        composable("routines") {
            RoutinesScreen(
                addEditRoutine = { routineId ->
                    navController.navigate("createRoutine/$routineId")
                },
                viewModel = get(RoutinesViewModel::class.java),
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
                controller = CreateRoutineController(get(Repository::class.java), routineId),
                sharedExerciseVM = sharedExerciseViewModel,
            )
        }
        composable("pickExercise") {
            PickExerciseScreen(
                viewModel = get(PickExerciseViewModel::class.java),
                sharedExerciseViewModel = sharedExerciseViewModel,
                popBackStack = { navController.popBackStack() }
            )
        }
        composable("exercises") {
            ExercisesScreen(
                addEditExercise = { exerciseId ->
                    navController.navigate("createExercise/$exerciseId")
                },
                viewModel = get(ExercisesViewModel::class.java)
            )
        }
        composable(
            route = "createExercise/{exerciseId}",
            arguments = listOf(navArgument("exerciseId") { type = NavType.IntType })
        ) { backStackEntry ->
            val exerciseId = backStackEntry.arguments?.getInt("exerciseId") ?: -1
            val createExerciseVM = CreateExerciseViewModel(get(Repository::class.java))
            createExerciseVM.setExercise(exerciseId)
            CreateExerciseScreen(
                popBackStack = { navController.popBackStack() },
                viewModel = createExerciseVM
            )
        }
    }
}

sealed class Screen(val route: String, val name: String) {
    object Routines : Screen("routines", "Routines")
    object Exercises : Screen("exercises", "Exercises")
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
                        screen.name,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
        }
    }
}
