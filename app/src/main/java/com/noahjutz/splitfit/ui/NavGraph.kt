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

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.navigate
import com.noahjutz.splitfit.data.Repository
import com.noahjutz.splitfit.ui.exercises.ExercisesScreen
import com.noahjutz.splitfit.ui.exercises.create.CreateExerciseScreen
import com.noahjutz.splitfit.ui.routines.RoutinesScreen
import com.noahjutz.splitfit.ui.routines.create.CreateRoutineScreen
import com.noahjutz.splitfit.ui.routines.create.CreateRoutineViewModel
import com.noahjutz.splitfit.ui.routines.create.pick.PickExerciseScreen
import com.noahjutz.splitfit.ui.routines.create.pick.SharedExerciseViewModel
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent


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
                addEditRoutine = { routineId -> navController.navigate("createRoutine/$routineId") }
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
                sharedExerciseViewModel = sharedExerciseViewModel,
                popBackStack = { navController.popBackStack() }
            )
        }
        composable("exercises") {
            ExercisesScreen(
                addEditExercise = { exerciseId -> navController.navigate("createExercise/$exerciseId") }
            )
        }
        composable(
            route = "createExercise/{exerciseId}",
            arguments = listOf(navArgument("exerciseId") { type = NavType.IntType })
        ) { backStackEntry ->
            val exerciseId = backStackEntry.arguments?.getInt("exerciseId") ?: -1
            CreateExerciseScreen(
                popBackStack = { navController.popBackStack() },
                viewModel = getViewModel { parametersOf(exerciseId) }
            )
        }
    }
}
