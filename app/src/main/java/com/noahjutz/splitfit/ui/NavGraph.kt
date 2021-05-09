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
import com.noahjutz.splitfit.ui.exercises.editor.ExerciseEditor
import com.noahjutz.splitfit.ui.exercises.picker.ExercisePicker
import com.noahjutz.splitfit.ui.exercises.picker.SharedExercisePickerViewModel
import com.noahjutz.splitfit.ui.routines.RoutineList
import com.noahjutz.splitfit.ui.routines.editor.CreateRoutineScreen
import com.noahjutz.splitfit.ui.settings.AppSettings
import com.noahjutz.splitfit.ui.settings.about.AboutSplitfit
import com.noahjutz.splitfit.ui.workout.editor.WorkoutEditor
import com.noahjutz.splitfit.ui.workout.in_progress.WorkoutInProgress
import com.noahjutz.splitfit.ui.workout.insights.WorkoutInsights
import org.koin.androidx.compose.getViewModel
import kotlin.time.ExperimentalTime

sealed class Screen(val name: String) {
    object Insights : Screen("insights")

    object RoutineList : Screen("routineList")
    object RoutineEditor : Screen("routineEditor")

    object ExerciseList : Screen("exerciseList")
    object ExerciseEditor : Screen("exerciseEditor")
    object ExercisePicker : Screen("exercisePicker")

    object WorkoutInProgress : Screen("workoutInProgress")
    object WorkoutEditor : Screen("workoutEditor")

    object Settings : Screen("settings")
    object About : Screen("about")
}

@ExperimentalTime
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun NavGraph(
    navController: NavHostController,
) {
    val sharedExercisePickerViewModel: SharedExercisePickerViewModel = getViewModel()
    NavHost(navController, startDestination = Screen.RoutineList.name) {
        composable(Screen.Insights.name) {
            WorkoutInsights(
                navToWorkoutEditor = { workoutId -> navController.navigate("${Screen.WorkoutEditor.name}/$workoutId") }
            )
        }
        composable(
            route = "${Screen.WorkoutEditor.name}/{workoutId}",
            arguments = listOf(navArgument("workoutId") { type = NavType.IntType })
        ) { backStackEntry ->
            val workoutId = backStackEntry.arguments!!.getInt("workoutId")
            WorkoutEditor(
                workoutId = workoutId,
                popBackStack = { navController.popBackStack() },
            )
        }
        composable(Screen.RoutineList.name) {
            RoutineList(
                addEditRoutine = { routineId -> navController.navigate("${Screen.RoutineEditor.name}/$routineId") }
            )
        }
        composable(
            route = "${Screen.RoutineEditor.name}/{routineId}",
            arguments = listOf(navArgument("routineId") { type = NavType.IntType })
        ) { backStackEntry ->
            CreateRoutineScreen(
                routineId = backStackEntry.arguments!!.getInt("routineId"),
                onAddExercise = { navController.navigate("exercisePicker") },
                startWorkout = { routineId: Int ->
                    navController.navigate("${Screen.WorkoutInProgress.name}?routineId=$routineId")
                },
                popBackStack = { navController.popBackStack() },
                sharedExercisePickerViewModel = sharedExercisePickerViewModel,
            )
        }
        composable(Screen.ExercisePicker.name) {
            ExercisePicker(
                sharedExercisePickerViewModel = sharedExercisePickerViewModel,
                popBackStack = { navController.popBackStack() }
            )
        }
        composable(Screen.ExerciseList.name) {
            ExerciseList(
                addEditExercise = { exerciseId -> navController.navigate("${Screen.ExerciseEditor.name}/$exerciseId") }
            )
        }
        composable(
            route = "${Screen.ExerciseEditor.name}/{exerciseId}",
            arguments = listOf(navArgument("exerciseId") { type = NavType.IntType })
        ) { backStackEntry ->
            ExerciseEditor(
                exerciseId = backStackEntry.arguments!!.getInt("exerciseId"),
                popBackStack = { navController.popBackStack() },
            )
        }
        composable(
            "${Screen.WorkoutInProgress.name}?workoutId={workoutId}&routineId={routineId}",
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
            WorkoutInProgress(
                navToPickExercise = { navController.navigate(Screen.ExercisePicker.name) },
                popBackStack = { navController.popBackStack() },
                workoutId = backStackEntry.arguments!!.getInt("workoutId"),
                routineId = backStackEntry.arguments!!.getInt("routineId"),
                sharedExercisePickerViewModel = sharedExercisePickerViewModel,
            )
        }
        composable(Screen.Settings.name) {
            AppSettings(navToAbout = { navController.navigate(Screen.About.name) })
        }
        composable(Screen.About.name) {
            AboutSplitfit(popBackStack = { navController.popBackStack() })
        }
    }
}
