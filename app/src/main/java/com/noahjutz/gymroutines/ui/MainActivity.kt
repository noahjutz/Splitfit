/*
 * GymRoutines
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

package com.noahjutz.gymroutines.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.focus.ExperimentalFocus
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.viewinterop.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.noahjutz.gymroutines.ui.exercises.ExercisesViewModel
import com.noahjutz.gymroutines.ui.routines.RoutinesScreen
import com.noahjutz.gymroutines.ui.routines.RoutinesViewModel
import com.noahjutz.gymroutines.ui.routines.create.CreateRoutineScreen
import com.noahjutz.gymroutines.ui.routines.create.CreateRoutineViewModel
import com.noahjutz.gymroutines.ui.routines.create.pick.PickExercise
import com.noahjutz.gymroutines.ui.routines.create.pick.SharedExerciseViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val sharedExerciseVM: SharedExerciseViewModel by viewModels()

    @ExperimentalFoundationApi
    @ExperimentalFocus
    @ExperimentalMaterialApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colors = if (isSystemInDarkTheme()) darkColors() else lightColors()) {
                MainScreen(sharedExerciseVM)
            }
        }
    }
}

@ExperimentalFoundationApi
@ExperimentalFocus
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun MainScreen(
    sharedExerciseVM: SharedExerciseViewModel
) {
    val navController = rememberNavController()
    Scaffold {
        val routinesVM = viewModel<RoutinesViewModel>()
        val createRoutineVM = viewModel<CreateRoutineViewModel>()
        val exercisesVM = viewModel<ExercisesViewModel>()
        NavHost(navController, startDestination = "routines") {
            composable("routines") {
                RoutinesScreen(
                    addEditRoutine = { routineId ->
                        navController.navigate("createRoutine/$routineId")
                    },
                    viewModel = routinesVM
                )
            }
            composable(
                route = "createRoutine/{routineId}",
                arguments = listOf(navArgument("routineId") { type = NavType.IntType })
            ) { backStackEntry ->
                val routineId: Int = backStackEntry.arguments?.getInt("routineId") ?: -1
                createRoutineVM.routineId = routineId
                CreateRoutineScreen(
                    onAddExercise = { navController.navigate("pickExercise") },
                    popBackStack = { navController.popBackStack() },
                    viewModel = createRoutineVM
                )
            }
            composable("pickExercise") { // TODO: Fix exercises not being passed back by sharedVM
                PickExercise(
                    exercisesViewModel = exercisesVM,
                    sharedExerciseViewModel = sharedExerciseVM,
                    popBackStack = { navController.popBackStack() }
                )
            }
            //composable("exercises") {
            //    ExercisesScreen(
            //        addEditExercise = { navController.navigate("createExercise") },
            //        viewModel = viewModel()
            //    )
            //}
            //composable("createExercise") {
            //    CreateExerciseScreen(
            //        popBackStack = { navController.popBackStack() },
            //        viewModel = viewModel()
            //    )
            //}
        }
    }
}