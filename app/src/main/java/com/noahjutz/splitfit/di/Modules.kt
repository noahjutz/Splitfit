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

import androidx.datastore.preferences.createDataStore
import androidx.room.Room
import com.noahjutz.splitfit.data.AppDatabase
import com.noahjutz.splitfit.data.ExerciseRepository
import com.noahjutz.splitfit.data.RoutineRepository
import com.noahjutz.splitfit.data.WorkoutRepository
import com.noahjutz.splitfit.ui.exercises.ExercisesViewModel
import com.noahjutz.splitfit.ui.exercises.create.CreateExerciseViewModel
import com.noahjutz.splitfit.ui.routines.RoutinesViewModel
import com.noahjutz.splitfit.ui.routines.create.CreateRoutineViewModel
import com.noahjutz.splitfit.ui.routines.create.pick.PickExerciseViewModel
import com.noahjutz.splitfit.ui.routines.create.pick.SharedPickExerciseViewModel
import com.noahjutz.splitfit.ui.settings.AppSettingsViewModel
import com.noahjutz.splitfit.ui.workout.WorkoutsViewModel
import com.noahjutz.splitfit.ui.workout.create.CreateWorkoutViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val koinModule = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "workout_routines_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    single {
        androidContext().createDataStore(name = "settings")
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
        SharedPickExerciseViewModel()
    }

    viewModel {
        RoutinesViewModel(get())
    }

    viewModel {
        ExercisesViewModel(get())
    }

    viewModel {
        PickExerciseViewModel(exerciseRepository = get())
    }

    viewModel { (id: Int) ->
        CreateExerciseViewModel(repository = get(), exerciseId = id)
    }

    viewModel { (id: Int) ->
        CreateRoutineViewModel(
            exerciseRepository = get(),
            routineRepository = get(),
            routineId = id
        )
    }

    viewModel { (workoutId: Int, routineId: Int) ->
        CreateWorkoutViewModel(
            workoutRepository = get(),
            routineRepository = get(),
            exerciseRepository = get(),
            workoutId = workoutId,
            routineId = routineId
        )
    }

    viewModel {
        WorkoutsViewModel(repository = get())
    }

    viewModel {
        AppSettingsViewModel(
            application = androidApplication(),
            database = get()
        )
    }
}
