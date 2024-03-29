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

package com.noahjutz.splitfit.di

import androidx.room.Room
import com.noahjutz.splitfit.data.AppDatabase
import com.noahjutz.splitfit.data.ExerciseRepository
import com.noahjutz.splitfit.data.RoutineRepository
import com.noahjutz.splitfit.data.WorkoutRepository
import com.noahjutz.splitfit.data.datastore
import com.noahjutz.splitfit.ui.exercises.editor.ExerciseEditorViewModel
import com.noahjutz.splitfit.ui.exercises.list.ExerciseListViewModel
import com.noahjutz.splitfit.ui.exercises.picker.ExercisePickerViewModel
import com.noahjutz.splitfit.ui.routines.RoutineListViewModel
import com.noahjutz.splitfit.ui.routines.editor.RoutineEditorViewModel
import com.noahjutz.splitfit.ui.settings.AppSettingsViewModel
import com.noahjutz.splitfit.ui.workout.completed.WorkoutCompletedViewModel
import com.noahjutz.splitfit.ui.workout.in_progress.WorkoutInProgressViewModel
import com.noahjutz.splitfit.ui.workout.insights.WorkoutInsightsViewModel
import com.noahjutz.splitfit.ui.workout.viewer.WorkoutViewerViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val koinModule = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "workout_routines_database")
            .build()
    }

    factory {
        androidContext().datastore
    }

    factory {
        get<AppDatabase>().exerciseDao
    }

    factory {
        get<AppDatabase>().routineDao
    }

    factory {
        get<AppDatabase>().workoutDao
    }

    factory {
        WorkoutRepository(workoutDao = get())
    }

    factory {
        RoutineRepository(routineDao = get())
    }

    factory {
        ExerciseRepository(exerciseDao = get())
    }

    viewModel {
        RoutineListViewModel(get())
    }

    viewModel {
        ExerciseListViewModel(get())
    }

    viewModel {
        ExercisePickerViewModel(exerciseRepository = get())
    }

    viewModel { params ->
        ExerciseEditorViewModel(repository = get(), exerciseId = params.get())
    }

    viewModel { params ->
        RoutineEditorViewModel(
            exerciseRepository = get(),
            routineRepository = get(),
            routineId = params.get()
        )
    }

    viewModel { params ->
        WorkoutInProgressViewModel(
            preferences = get(),
            workoutRepository = get(),
            routineRepository = get(),
            exerciseRepository = get(),
            workoutId = params[0],
            routineId = params[1],
        )
    }

    viewModel {
        WorkoutInsightsViewModel(
            repository = get(),
            preferences = get(),
        )
    }

    viewModel {
        AppSettingsViewModel(
            application = androidApplication(),
            database = get(),
            preferences = get(),
        )
    }

    viewModel { params ->
        WorkoutViewerViewModel(
            workoutId = params.get(),
            workoutRepository = get(),
        )
    }

    viewModel { params ->
        WorkoutCompletedViewModel(
            routineId = params[0],
            workoutId = params[1],
            routineRepository = get(),
            workoutRepository = get(),
        )
    }
}
