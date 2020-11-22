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
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.focus.ExperimentalFocus
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.viewinterop.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.noahjutz.gymroutines.ui.exercises.ExercisesScreen
import com.noahjutz.gymroutines.ui.exercises.create.CreateExerciseScreen
import com.noahjutz.gymroutines.ui.routines.RoutinesScreen
import com.noahjutz.gymroutines.ui.routines.RoutinesViewModel
import com.noahjutz.gymroutines.ui.routines.create.CreateRoutineEditor
import com.noahjutz.gymroutines.ui.routines.create.CreateRoutinePresenter
import com.noahjutz.gymroutines.ui.routines.create.CreateRoutineScreen
import com.noahjutz.gymroutines.ui.routines.create.pick.PickExercise
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @ExperimentalFoundationApi
    @ExperimentalFocus
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
@ExperimentalFocus
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold {
        val routinesVM = viewModel<RoutinesViewModel>()
        //val createRoutineEditor = viewModel<CreateRoutineEditor>()
        //val createRoutinePresenter = viewModel<CreateRoutinePresenter>()
        NavHost(navController, startDestination = "routines") {
            composable("routines") {
                RoutinesScreen(
                    addEditRoutine = { navController.navigate("createRoutine") },
                    viewModel = routinesVM
                )
            }
            //composable("createRoutine") {
            //    CreateRoutineScreen(
            //        onAddExercise = { navController.navigate("pickExercise") },
            //        popBackStack = { navController.popBackStack() },
            //        presenter = createRoutinePresenter,
            //        editor = createRoutineEditor
            //    )
            //}
            //composable("pickExercise") {
            //    PickExercise(
            //        exercisesViewModel = viewModel(),
            //        sharedExerciseViewModel = viewModel(),
            //        popBackStack = { navController.popBackStack() }
            //    )
            //}
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