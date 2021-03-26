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
import com.noahjutz.splitfit.ui.exercises.ExerciseList
import com.noahjutz.splitfit.ui.exercises.create.CreateExerciseScreen
import com.noahjutz.splitfit.ui.exercises.picker.ExercisePicker
import com.noahjutz.splitfit.ui.exercises.picker.SharedExercisePickerViewModel
import com.noahjutz.splitfit.ui.routines.RoutineList
import com.noahjutz.splitfit.ui.routines.editor.CreateRoutineScreen
import com.noahjutz.splitfit.ui.settings.AppSettings
import com.noahjutz.splitfit.ui.settings.about.AboutSplitfit
import com.noahjutz.splitfit.ui.workout.WorkoutInsights
import com.noahjutz.splitfit.ui.workout.editor.WorkoutScreen
import org.koin.androidx.compose.getViewModel
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun NavGraph(
    navController: NavHostController,
) {
    val sharedExercisePickerViewModel: SharedExercisePickerViewModel = getViewModel()
    NavHost(navController, startDestination = "routines") {
        composable("workouts") {
            WorkoutInsights(
                navToCreateWorkoutScreen = { workoutId -> navController.navigate("createWorkout?workoutId=$workoutId") }
            )
        }
        composable("routines") {
            RoutineList(
                addEditRoutine = { routineId -> navController.navigate("createRoutine/$routineId") }
            )
        }
        composable(
            route = "createRoutine/{routineId}",
            arguments = listOf(navArgument("routineId") { type = NavType.IntType })
        ) { backStackEntry ->
            CreateRoutineScreen(
                routineId = backStackEntry.arguments!!.getInt("routineId"),
                onAddExercise = { navController.navigate("pickExercise") },
                startWorkout = { routineId: Int ->
                    navController.navigate("createWorkout?routineId=$routineId")
                },
                popBackStack = { navController.popBackStack() },
                sharedExercisePickerViewModel = sharedExercisePickerViewModel,
            )
        }
        composable("pickExercise") {
            ExercisePicker(
                sharedExercisePickerViewModel = sharedExercisePickerViewModel,
                popBackStack = { navController.popBackStack() }
            )
        }
        composable("exercises") {
            ExerciseList(
                addEditExercise = { exerciseId -> navController.navigate("createExercise/$exerciseId") }
            )
        }
        composable(
            route = "createExercise/{exerciseId}",
            arguments = listOf(navArgument("exerciseId") { type = NavType.IntType })
        ) { backStackEntry ->
            CreateExerciseScreen(
                exerciseId = backStackEntry.arguments!!.getInt("exerciseId"),
                popBackStack = { navController.popBackStack() },
            )
        }
        composable(
            "createWorkout?workoutId={workoutId}&routineId={routineId}",
            arguments = listOf(
                navArgument("workoutId") {
                    defaultValue = -1
                    type = NavType.IntType
                },
                navArgument("routineId") {
                    defaultValue = -1
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            WorkoutScreen(
                navToPickExercise = { navController.navigate("pickExercise") },
                popBackStack = { navController.popBackStack() },
                workoutId = backStackEntry.arguments!!.getInt("workoutId"),
                routineId = backStackEntry.arguments!!.getInt("routineId"),
                sharedExercisePickerViewModel = sharedExercisePickerViewModel,
            )
        }
        composable("settings") {
            AppSettings(navToAbout = { navController.navigate("about") })
        }
        composable("about") {
            AboutSplitfit(popBackStack = { navController.popBackStack() })
        }
    }
}
