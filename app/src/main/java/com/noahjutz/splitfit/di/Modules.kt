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
import com.noahjutz.splitfit.ui.exercises.ExerciseListViewModel
import com.noahjutz.splitfit.ui.exercises.editor.ExerciseEditorViewModel
import com.noahjutz.splitfit.ui.exercises.picker.ExercisePickerViewModel
import com.noahjutz.splitfit.ui.exercises.picker.SharedExercisePickerViewModel
import com.noahjutz.splitfit.ui.routines.RoutineListViewModel
import com.noahjutz.splitfit.ui.routines.editor.RoutineEditorViewModel
import com.noahjutz.splitfit.ui.settings.AppSettingsViewModel
import com.noahjutz.splitfit.ui.settings.about.AboutSplitfitViewModel
import com.noahjutz.splitfit.ui.workout.editor.WorkoutEditorViewModel
import com.noahjutz.splitfit.ui.workout.in_progress.CreateWorkoutViewModel
import com.noahjutz.splitfit.ui.workout.insights.WorkoutInsightsViewModel
import com.noahjutz.splitfit.util.datastore
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val koinModule = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "workout_routines_database")
            .fallbackToDestructiveMigration()
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
        SharedExercisePickerViewModel()
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

    viewModel { (id: Int) ->
        ExerciseEditorViewModel(repository = get(), exerciseId = id)
    }

    viewModel { (id: Int) ->
        RoutineEditorViewModel(
            exerciseRepository = get(),
            routineRepository = get(),
            routineId = id
        )
    }

    viewModel { (workoutId: Int, routineId: Int) ->
        CreateWorkoutViewModel(
            preferences = get(),
            workoutRepository = get(),
            routineRepository = get(),
            exerciseRepository = get(),
            workoutId = workoutId,
            routineId = routineId,
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
            database = get()
        )
    }

    viewModel {
        AboutSplitfitViewModel()
    }

    viewModel { (id: Int) ->
        WorkoutEditorViewModel(
            workoutId = id,
            workoutRepository = get(),
        )
    }
}
